package com.ABCIgnite.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
public class ClassResponseDTO {
    private String classId;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private Integer duration;
    private Integer capacity;

    public ClassResponseDTO(String classId, String name, LocalDate startDate, LocalDate endDate, LocalTime startTime, Integer duration, Integer capacity) {
        this.classId = classId;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.duration = duration;
        this.capacity = capacity;
    }

}

