package com.ABCIgnite.controller;

import com.ABCIgnite.dto.request.BookingRequestDTO;
import com.ABCIgnite.dto.response.BookingResponseDTO;
import com.ABCIgnite.service.BookingService;
//import jakarta.validation.Valid;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<BookingResponseDTO> createBooking(@Valid @RequestBody BookingRequestDTO bookingRequestDTO) {
        BookingResponseDTO bookingResponseDTO = bookingService.createBooking(bookingRequestDTO);
        return ResponseEntity.ok(bookingResponseDTO);
    }

    @GetMapping
    public ResponseEntity<List<BookingResponseDTO>> searchBookings(
            @RequestParam(required = false) String memberName,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        List<BookingResponseDTO> bookings = bookingService.searchBookings(memberName, startDate, endDate);
        return ResponseEntity.ok(bookings);
    }
}