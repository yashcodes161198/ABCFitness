package com.ABCIgnite.entities;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ClassEntity {

    @NotNull
    private String name;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotNull
    private LocalTime startTime;

    @Min(1)
    private int capacity;

    public ClassEntity(String name, LocalDate startDate, LocalDate endDate, LocalTime startTime, int capacity) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.capacity = capacity;
    }

    // Getters and setters omitted for brevity
}
