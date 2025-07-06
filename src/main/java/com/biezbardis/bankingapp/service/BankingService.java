package com.biezbardis.bankingapp.service;

import com.biezbardis.bankingapp.dto.AccountResponse;
import com.biezbardis.bankingapp.dto.BalanceRequest;
import com.biezbardis.bankingapp.dto.TransactionRequest;
import com.biezbardis.bankingapp.dto.TransactionResponse;
import com.biezbardis.bankingapp.entity.TransactionType;
import com.biezbardis.bankingapp.exception.ClientNotFoundException;
import com.biezbardis.bankingapp.exception.InvalidAmountException;
import jakarta.transaction.Transactional;

import java.util.List;

/**
 * Service interface for handling banking operations such as executing deposit/withdrawal
 * transactions and retrieving account balances.
 */
public interface BankingService {

    /**
     * Executes a transaction of the specified type for the given request.
     *
     * @param request the transaction request containing details such as source account,
     *                destination account, and amount.
     * @param type    the type of transaction to perform (DEPOSIT, WITHDRAWAL).
     * @return a response indicating the result of the transaction, including status and any relevant metadata.
     * @throws ClientNotFoundException if the request was initiated by a non-existent client.
     * @throws InsufficientFundsException if the source account lacks sufficient balance for the transaction.
     * @throws InvalidAmountException if the request contains a negative amount of funds.
     */
    TransactionResponse execute(TransactionRequest request, TransactionType type);

    /**
     * Retrieves the current balance for the specified account.
     * This method is transactional to ensure consistency if it involves any stateful computation or access.
     * Creates new account with zero balance if such is not exists.
     *
     * @param request the balance request containing account identification information.
     * @return the account response including current balance and account metadata.
     * @throws ClientNotFoundException if the request was initiated by a non-existent client.
     */
    @Transactional
    AccountResponse getBalance(BalanceRequest request);

    /**
     * Retrieves all accounts associated with a given client name.
     * This operation is transactional to ensure consistency during read operations.
     *
     * @param clientName the name of the client whose accounts should be retrieved.
     * @return a list of account responses for the specified client, or an empty list if none found.
     * @throws ClientNotFoundException if the request was initiated by a non-existent client.
     */
    @Transactional
    List<AccountResponse> getAllAccounts(String clientName);
}
