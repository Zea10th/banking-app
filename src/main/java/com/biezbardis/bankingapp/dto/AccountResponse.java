package com.biezbardis.bankingapp.dto;

import com.biezbardis.bankingapp.entity.Currency;

public record AccountResponse(Long accountId, Currency currency, Double balance) {
}