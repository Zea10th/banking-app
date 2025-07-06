package com.biezbardis.bankingapp.dto;

import com.biezbardis.bankingapp.entity.Currency;
import io.swagger.v3.oas.annotations.media.Schema;

public record AccountResponse(
        @Schema(description = "Account Id", example = "123")
        Long accountId,
        @Schema(description = "Currency Type", example = "EUR")
        Currency currency,
        @Schema(description = "Balance Amount", example = "123.45")
        Double balance
) {
}