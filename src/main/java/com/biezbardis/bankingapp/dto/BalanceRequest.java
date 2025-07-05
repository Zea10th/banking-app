package com.biezbardis.bankingapp.dto;

import com.biezbardis.bankingapp.entity.Currency;

public record BalanceRequest(String clientName, Currency currency) {
}