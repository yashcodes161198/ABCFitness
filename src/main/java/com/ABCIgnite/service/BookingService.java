package com.ABCIgnite.service;

import com.ABCIgnite.dto.request.BookingRequestDTO;
import com.ABCIgnite.dto.response.BookingResponseDTO;
import com.ABCIgnite.entities.BookingEntity;
import com.ABCIgnite.entities.ClassEntity;
import com.ABCIgnite.repo.BookingRepository;
import com.ABCIgnite.repo.ClassRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ClassRepository classRepository;

    public BookingService(BookingRepository bookingRepository, ClassRepository classRepository) {
        this.bookingRepository = bookingRepository;
        this.classRepository = classRepository;
    }

    @Transactional
    public BookingResponseDTO createBooking(BookingRequestDTO bookingRequestDTO) {
        // Fetch the class entity with a pessimistic lock
        Optional<ClassEntity> optionalClassEntity = classRepository.findByClassIdWithLock(bookingRequestDTO.getClassId());
        if (optionalClassEntity.isEmpty()) {
            throw new IllegalArgumentException("Class not found: " + bookingRequestDTO.getClassId());
        }

        if (bookingRequestDTO.getParticipationDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Participation date must be in the future");
        }
        ClassEntity classEntity = optionalClassEntity.get();
        if (bookingRequestDTO.getParticipationDate().isBefore(classEntity.getStartDate()) ||
                bookingRequestDTO.getParticipationDate().isAfter(classEntity.getEndDate())) {
            throw new IllegalArgumentException("Participation date must be between startDate and endDate of the class (inclusive)");
        }

        // Check current bookings for the class on the specified date
        long currentBookings = bookingRepository.findByClassEntityAndParticipationDate(
                        classEntity,
                        bookingRequestDTO.getParticipationDate()).size();

        if (currentBookings >= classEntity.getCapacity()) {
            throw new IllegalArgumentException("Class capacity exceeded for date: " + bookingRequestDTO.getParticipationDate());
        }

        // Create and save the booking
        BookingEntity bookingEntity = new BookingEntity(
                bookingRequestDTO.getMemberName(),
                classEntity,
                bookingRequestDTO.getParticipationDate()
        );

        BookingEntity savedBookingEntity = bookingRepository.save(bookingEntity);

        return new BookingResponseDTO(
                savedBookingEntity.getBookingId(),
                savedBookingEntity.getMemberName(),
                savedBookingEntity.getClassEntity().getName(),
                savedBookingEntity.getParticipationDate()
        );
    }

    public List<BookingResponseDTO> searchBookings(String memberName, String startDate, String endDate) {
        LocalDate start = startDate != null ? LocalDate.parse(startDate) : null;
        LocalDate end = endDate != null ? LocalDate.parse(endDate) : null;

        List<BookingEntity> results;
        if (memberName != null && start != null && end != null) {
            results = bookingRepository.findByMemberNameAndParticipationDateBetween(memberName, start, end);
        } else if (memberName != null) {
            results = bookingRepository.findByMemberName(memberName);
        } else if (start != null && end != null) {
            results = bookingRepository.findByParticipationDateBetween(start, end);
        } else {
            throw new IllegalArgumentException("Invalid search parameters. Please provide memberName, date range, or both.");
        }

        return results.stream().map(bookingEntity -> new BookingResponseDTO(
                bookingEntity.getBookingId(),
                bookingEntity.getMemberName(),
                bookingEntity.getClassEntity().getName(),
                bookingEntity.getParticipationDate()
        )).collect(Collectors.toList());
    }
}

