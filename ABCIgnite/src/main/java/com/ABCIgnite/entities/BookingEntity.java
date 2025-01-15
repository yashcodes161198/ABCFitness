package com.ABCIgnite.entities;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingEntity {

    @NotNull
    private String memberName;

    @NotNull
    private String className;

    @NotNull
    private LocalDate participationDate;

    public BookingEntity(String memberName, String className, LocalDate participationDate) {
        this.memberName = memberName;
        this.className = className;
        this.participationDate = participationDate;
    }

    // Getters and setters omitted for brevity
}