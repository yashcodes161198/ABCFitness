package com.ABCIgnite.repo;


import com.ABCIgnite.entities.BookingEntity;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;


@Repository
public class BookingRepository {

//    Using interface instead of implementation
    private final ConcurrentMap<String, BookingEntity> bookingStorage = new ConcurrentHashMap<>();

    public void save(String bookingId, BookingEntity bookingEntity) {
        bookingStorage.put(bookingId, bookingEntity);
    }

    public List<BookingEntity> findByMemberName(String memberName) {
        return bookingStorage.values().stream()
                .filter(booking -> booking.getMemberName().equals(memberName))
                .collect(Collectors.toList());
    }

    public List<BookingEntity> findByDateRange(LocalDate startDate, LocalDate endDate) {
        return bookingStorage.values().stream()
                .filter(booking -> !booking.getParticipationDate().isBefore(startDate) &&
                        !booking.getParticipationDate().isAfter(endDate))
                .collect(Collectors.toList());
    }

    public List<BookingEntity> findByMemberNameAndDateRange(String memberName, LocalDate startDate, LocalDate endDate) {
        return bookingStorage.values().stream()
                .filter(booking -> booking.getMemberName().equals(memberName) &&
                        !booking.getParticipationDate().isBefore(startDate) &&
                        !booking.getParticipationDate().isAfter(endDate))
                .collect(Collectors.toList());
    }
}
