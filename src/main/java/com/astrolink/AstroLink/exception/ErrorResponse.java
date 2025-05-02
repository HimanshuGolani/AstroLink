package com.astrolink.AstroLink.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private String errorCode;
    private String errorMessage;
    private LocalDateTime timestamp;
}
