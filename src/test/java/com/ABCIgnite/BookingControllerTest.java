package com.ABCIgnite;

import com.ABCIgnite.controller.BookingController;
import com.ABCIgnite.dto.request.BookingRequestDTO;
import com.ABCIgnite.dto.response.BookingResponseDTO;
import com.ABCIgnite.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookingControllerTest {

    private BookingService bookingService;
    private BookingController bookingController;

    @BeforeEach
    void setUp() {
        bookingService = mock(BookingService.class);
        bookingController = new BookingController(bookingService);
    }

    @Test
    void createBooking_ValidRequest_ReturnsCreatedBooking() {
        BookingRequestDTO request = new BookingRequestDTO(
                "John Doe",
                "class123",
                LocalDate.now().plusDays(1)
        );

        BookingResponseDTO response = new BookingResponseDTO(
                "booking123",
                "John Doe",
                "Yoga",
                request.getParticipationDate()
        );

        when(bookingService.createBooking(request)).thenReturn(response);

        ResponseEntity<BookingResponseDTO> result = bookingController.createBooking(request);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals("John Doe", result.getBody().getMemberName());
        verify(bookingService, times(1)).createBooking(request);
    }

    @Test
    void createBooking_PastParticipationDate_ThrowsException() {
        BookingRequestDTO request = new BookingRequestDTO(
                "John Doe",
                "class123",
                LocalDate.now().minusDays(1) // Invalid past date
        );

        when(bookingService.createBooking(request))
                .thenThrow(new IllegalArgumentException("Participation date must be in the future"));

        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> bookingController.createBooking(request)
        );

        assertEquals("Participation date must be in the future", exception.getMessage());
        verify(bookingService, times(1)).createBooking(request);
    }

    @Test
    void searchBookings_EmptyResponse_ReturnsEmptyList() {
        when(bookingService.searchBookings("John Doe", null, null))
                .thenReturn(Collections.emptyList());

        ResponseEntity<List<BookingResponseDTO>> result = bookingController.searchBookings("John Doe", null, null);

        assertEquals(200, result.getStatusCodeValue());
        assertTrue(result.getBody().isEmpty());
        verify(bookingService, times(1)).searchBookings("John Doe", null, null);
    }

    @Test
    void searchBookings_ValidParameters_ReturnsListOfBookings() {
        List<BookingResponseDTO> responseList = Arrays.asList(
                new BookingResponseDTO("booking1", "John Doe", "Yoga", LocalDate.now().plusDays(1)),
                new BookingResponseDTO("booking2", "Jane Doe", "Pilates", LocalDate.now().plusDays(2))
        );

        when(bookingService.searchBookings("John Doe", null, null)).thenReturn(responseList);

        ResponseEntity<List<BookingResponseDTO>> result = bookingController.searchBookings("John Doe", null, null);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals(2, result.getBody().size());
        verify(bookingService, times(1)).searchBookings("John Doe", null, null);
    }

    @Test
    void searchBookings_InvalidDateRange_ThrowsException() {
        when(bookingService.searchBookings(null, "2023-01-01", "2022-12-31"))
                .thenThrow(new IllegalArgumentException("Invalid search parameters. Please provide memberName, date range, or both."));

        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> bookingController.searchBookings(null, "2023-01-01", "2022-12-31")
        );

        assertEquals("Invalid search parameters. Please provide memberName, date range, or both.", exception.getMessage());
        verify(bookingService, times(1)).searchBookings(null, "2023-01-01", "2022-12-31");
    }
}

