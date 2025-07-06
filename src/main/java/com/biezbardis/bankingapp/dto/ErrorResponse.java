package com.biezbardis.bankingapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record ErrorResponse(
        @Schema(description = "HTTP Status Code", example = "400")
        String code,
        @Schema(description = "HTTP Status Message", example = "Bad Request")
        String message,
        @Schema(description = "Timestamp", example = "2007-12-03T10:15:30")
        LocalDateTime timestamp
) {
    public ErrorResponse(String code, String message) {
        this(code, message, LocalDateTime.now());
    }
}