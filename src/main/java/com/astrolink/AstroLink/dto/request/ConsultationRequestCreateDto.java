package com.astrolink.AstroLink.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsultationRequestCreateDto {
    @NotBlank(message = "Title cannot be empty")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;

    @NotBlank(message = "Birth date cannot be empty")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Birth date must be in format yyyy-MM-dd")
    private String birthDate;

    @NotBlank(message = "Birth time cannot be empty")
    @Pattern(regexp = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$", message = "Birth time must be in format HH:mm")
    private String birthTime;

    @NotBlank(message = "Birth place cannot be empty")
    @Size(min = 2, max = 100, message = "Birth place must be between 2 and 100 characters")
    private String birthPlace;

    // Custom validation to ensure birth date is not in the future
    @AssertTrue(message = "Birth date cannot be in the future")
    private boolean isBirthDateValid() {
        if (birthDate == null || !birthDate.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
            return true; // Skip validation if format is already invalid
        }

        try {
            LocalDate date = LocalDate.parse(birthDate);
            return !date.isAfter(LocalDate.now());
        } catch (Exception e) {
            return false;
        }
    }
}