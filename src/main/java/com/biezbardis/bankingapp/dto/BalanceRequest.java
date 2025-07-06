package com.biezbardis.bankingapp.dto;

import com.biezbardis.bankingapp.entity.Currency;
import jakarta.validation.constraints.NotNull;

public record BalanceRequest(@NotNull String clientName, @NotNull Currency currency) {
}