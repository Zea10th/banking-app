package com.biezbardis.bankingapp.dto;

import com.biezbardis.bankingapp.entity.Currency;

import java.time.LocalDateTime;

public record TransactionResponse(String message, Currency currency, Double newBalance, LocalDateTime timestamp) {
}
