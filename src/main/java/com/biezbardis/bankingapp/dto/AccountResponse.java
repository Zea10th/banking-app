package com.biezbardis.bankingapp.dto;

import com.biezbardis.bankingapp.entity.Currency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse {
    private Long accountId;
    private Currency currency;
    private Double balance;
}
