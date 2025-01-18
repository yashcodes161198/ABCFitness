package com.ABCIgnite.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class BookingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String bookingId;

    @NotNull
    private String memberName;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private ClassEntity classEntity;

    @NotNull
    private LocalDate participationDate;

    public BookingEntity(String memberName, ClassEntity classEntity, LocalDate participationDate) {
        this.bookingId = UUID.randomUUID().toString(); // Generate unique bookingId
        this.classEntity = classEntity;
        this.memberName = memberName;
        this.participationDate = participationDate;
    }
}
