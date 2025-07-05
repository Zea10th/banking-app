package com.biezbardis.bankingapp.dto;

import com.biezbardis.bankingapp.entity.Currency;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {
    @NotNull
    private String clientName;
    @NotNull
    private Currency currency;
    private Double amount;
    private String description;
}
