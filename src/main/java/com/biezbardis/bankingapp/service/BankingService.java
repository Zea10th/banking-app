package com.biezbardis.bankingapp.service;

import com.biezbardis.bankingapp.dto.AccountResponse;
import com.biezbardis.bankingapp.dto.BalanceRequest;
import com.biezbardis.bankingapp.dto.TransactionRequest;
import com.biezbardis.bankingapp.dto.TransactionResponse;
import com.biezbardis.bankingapp.entity.TransactionType;
import jakarta.transaction.Transactional;

import java.util.List;

public interface BankingService {
    TransactionResponse execute(TransactionRequest request, TransactionType type);

    @Transactional
    AccountResponse getBalance(BalanceRequest request);

    @Transactional
    List<AccountResponse> getAllAccounts(String clientName);
}
