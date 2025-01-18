package com.ABCIgnite;

import com.ABCIgnite.dto.request.BookingRequestDTO;
import com.ABCIgnite.dto.request.ClassRequestDTO;
import com.ABCIgnite.dto.response.BookingResponseDTO;
import com.ABCIgnite.dto.response.ClassResponseDTO;
import com.ABCIgnite.entities.BookingEntity;
import com.ABCIgnite.entities.ClassEntity;
import com.ABCIgnite.repo.BookingRepository;
import com.ABCIgnite.repo.ClassRepository;
import com.ABCIgnite.service.BookingService;
import com.ABCIgnite.service.ClassService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ServiceTests {

    @Mock
    private ClassRepository classRepository;

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private ClassService classService;

    @InjectMocks
    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ClassService Test
    @Test
    void createClass_ValidRequest_ReturnsClassResponse() {
        ClassRequestDTO request = new ClassRequestDTO("Yoga", LocalDate.now().plusDays(1), LocalDate.now().plusDays(10), LocalTime.of(10, 0), 60, 20);
        ClassEntity savedEntity = new ClassEntity("Yoga", LocalDate.now().plusDays(1), LocalDate.now().plusDays(10), LocalTime.of(10, 0), 60, 20);
        savedEntity.setClassId("unique-class-id");

        when(classRepository.save(any(ClassEntity.class))).thenReturn(savedEntity);

        ClassResponseDTO response = classService.createClass(request);

        assertNotNull(response);
        assertEquals("Yoga", response.getName());
        assertEquals(60, response.getDuration());
        verify(classRepository, times(1)).save(any(ClassEntity.class));
    }

    @Test
    void createClass_EndDateInPast_ThrowsException() {
        ClassRequestDTO request = new ClassRequestDTO("Yoga", LocalDate.now().plusDays(1), LocalDate.now().minusDays(1), LocalTime.of(10, 0), 60, 20);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> classService.createClass(request));

        assertEquals("End date must be in the future", exception.getMessage());
        verify(classRepository, never()).save(any(ClassEntity.class));
    }

    @Test
    void createClass_CapacityLessThanOne_ThrowsException() {
        ClassRequestDTO request = new ClassRequestDTO("Yoga", LocalDate.now().plusDays(1), LocalDate.now().plusDays(10), LocalTime.of(10, 0), 60, 0);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> classService.createClass(request));

        assertEquals("Capacity must be at least 1", exception.getMessage());
        verify(classRepository, never()).save(any(ClassEntity.class));
    }

    @Test
    void getAllClasses_ReturnsClassList() {
        ClassEntity classEntity = new ClassEntity("Yoga", LocalDate.now().plusDays(1), LocalDate.now().plusDays(10), LocalTime.of(10, 0), 60, 20);
        classEntity.setClassId("unique-class-id");

        when(classRepository.findAll()).thenReturn(List.of(classEntity));

        List<ClassResponseDTO> response = classService.getAllClasses();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Yoga", response.get(0).getName());
        verify(classRepository, times(1)).findAll();
    }

    // BookingService Tests
    @Test
    void createBooking_ValidRequest_ReturnsBookingResponse() {
        ClassEntity classEntity = new ClassEntity("Yoga", LocalDate.now().plusDays(1), LocalDate.now().plusDays(10), LocalTime.of(10, 0), 60, 20);
        classEntity.setClassId("unique-class-id");

        BookingRequestDTO request = new BookingRequestDTO("John Doe", "unique-class-id", LocalDate.now().plusDays(2));
        BookingEntity savedBooking = new BookingEntity("John Doe", classEntity, request.getParticipationDate());
        savedBooking.setBookingId("unique-booking-id");

        when(classRepository.findByClassIdWithLock("unique-class-id")).thenReturn(Optional.of(classEntity));
        when(bookingRepository.findByClassEntityAndParticipationDate(any(ClassEntity.class), any(LocalDate.class))).thenReturn(Collections.emptyList());
        when(bookingRepository.save(any(BookingEntity.class))).thenReturn(savedBooking);

        BookingResponseDTO response = bookingService.createBooking(request);

        assertNotNull(response);
        assertEquals("John Doe", response.getMemberName());
        assertEquals("unique-booking-id", response.getBookingId());
        verify(classRepository, times(1)).findByClassIdWithLock("unique-class-id");
        verify(bookingRepository, times(1)).save(any(BookingEntity.class));
    }

    @Test
    void createBooking_ClassNotFound_ThrowsException() {
        BookingRequestDTO request = new BookingRequestDTO("John Doe", "nonexistent-class-id", LocalDate.now().plusDays(2));

        when(classRepository.findByClassIdWithLock("nonexistent-class-id")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> bookingService.createBooking(request));

        assertEquals("Class not found: nonexistent-class-id", exception.getMessage());
        verify(bookingRepository, never()).save(any(BookingEntity.class));
    }

    @Test
    void createBooking_DateOutOfClassRange_ThrowsException() {
        ClassEntity classEntity = new ClassEntity("Yoga", LocalDate.now().plusDays(1), LocalDate.now().plusDays(10), LocalTime.of(10, 0), 60, 20);
        classEntity.setClassId("unique-class-id");

        BookingRequestDTO request = new BookingRequestDTO("John Doe", "unique-class-id", LocalDate.now().plusDays(11));

        when(classRepository.findByClassIdWithLock("unique-class-id")).thenReturn(Optional.of(classEntity));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> bookingService.createBooking(request));

        assertEquals("Participation date must be between startDate and endDate of the class (inclusive)", exception.getMessage());
        verify(bookingRepository, never()).save(any(BookingEntity.class));
    }

    @Test
    void createBooking_ClassCapacityExceeded_ThrowsException() {
        ClassEntity classEntity = new ClassEntity("Yoga", LocalDate.now().plusDays(1), LocalDate.now().plusDays(10), LocalTime.of(10, 0), 60, 1);
        classEntity.setClassId("unique-class-id");

        BookingRequestDTO request = new BookingRequestDTO("John Doe", "unique-class-id", LocalDate.now().plusDays(2));
        BookingEntity existingBooking = new BookingEntity("Jane Doe", classEntity, LocalDate.now().plusDays(2));

        when(classRepository.findByClassIdWithLock("unique-class-id")).thenReturn(Optional.of(classEntity));
        when(bookingRepository.findByClassEntityAndParticipationDate(any(ClassEntity.class), any(LocalDate.class))).thenReturn(List.of(existingBooking));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> bookingService.createBooking(request));

        assertEquals("Class capacity exceeded for date: " + request.getParticipationDate(), exception.getMessage());
        verify(bookingRepository, never()).save(any(BookingEntity.class));
    }

    @Test
    void searchBookings_ValidParameters_ReturnsBookingList() {
        ClassEntity classEntity = new ClassEntity("Yoga", LocalDate.now().plusDays(1), LocalDate.now().plusDays(10), LocalTime.of(10, 0), 60, 20);
        classEntity.setClassId("unique-class-id");
        BookingEntity bookingEntity = new BookingEntity("John Doe", classEntity, LocalDate.now().plusDays(2));
        bookingEntity.setBookingId("unique-booking-id");

        when(bookingRepository.findByMemberNameAndParticipationDateBetween("John Doe", LocalDate.now().plusDays(1), LocalDate.now().plusDays(3))).thenReturn(List.of(bookingEntity));

        List<BookingResponseDTO> response = bookingService.searchBookings("John Doe", LocalDate.now().plusDays(1).toString(), LocalDate.now().plusDays(3).toString());

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("John Doe", response.get(0).getMemberName());
        verify(bookingRepository, times(1)).findByMemberNameAndParticipationDateBetween(anyString(), any(LocalDate.class), any(LocalDate.class));
    }
}
