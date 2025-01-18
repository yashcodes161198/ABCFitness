package com.ABCIgnite.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
public class ClassEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String classId;

    @NotNull
    private String name;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotNull
    private LocalTime startTime;

    @Min(1)
    private Integer duration; // Duration in minutes

    @Min(1)
    private int capacity;

//    @OneToMany(mappedBy = "classEntity", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<BookingEntity> bookingEntities;

    public ClassEntity(String name, LocalDate startDate, LocalDate endDate, LocalTime startTime, Integer duration, int capacity) {
        this.classId = UUID.randomUUID().toString(); // Generate unique classId
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.duration = duration;
        this.capacity = capacity;
    }
}
