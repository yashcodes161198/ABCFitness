package com.ABCIgnite.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class BookingResponseDTO {

    private String bookingId;
    private String memberName;
    private String className;
    private LocalDate participationDate;

    public BookingResponseDTO(String bookingId, String memberName, String className, LocalDate participationDate) {
        this.bookingId = bookingId;
        this.memberName = memberName;
        this.className = className;
        this.participationDate = participationDate;
    }

}

