package com.ABCIgnite.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequestDTO {

    @NotBlank(message = "Member name is required")
    private String memberName;

    @NotNull(message = "Class Id is required")
    private String classId;

    @NotNull(message = "Participation date is required")
    private LocalDate participationDate;

}
