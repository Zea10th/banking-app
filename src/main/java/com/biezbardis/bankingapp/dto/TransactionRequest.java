package com.biezbardis.bankingapp.dto;

import com.biezbardis.bankingapp.entity.Currency;
import jakarta.validation.constraints.NotNull;

public record TransactionRequest(@NotNull String clientName, @NotNull Currency currency, Double amount, String description) {
}
