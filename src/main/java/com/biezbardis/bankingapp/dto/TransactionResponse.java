package com.biezbardis.bankingapp.dto;

import com.biezbardis.bankingapp.entity.Currency;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record TransactionResponse(
        @Schema(description = "Transaction Message", example = "Deposit successful")
        String message,
        @Schema(description = "Currency Type", example = "EUR")
        Currency currency,
        @Schema(description = "Updated Balance", example = "123.45")
        Double newBalance,
        @Schema(description = "Timestamp", example = "2007-12-03T10:15:30")
        LocalDateTime timestamp
) {
}
