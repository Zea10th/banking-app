package com.biezbardis.bankingapp.dto;

import com.biezbardis.bankingapp.entity.Currency;

public record TransactionRequest(String clientName, Currency currency, Double amount, String description) {
}
