package com.biezbardis.bankingapp.dto;

import com.biezbardis.bankingapp.entity.Currency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {
    private String message;
    private Currency currency;
    private Double newBalance;
    private LocalDateTime timestamp;
}
