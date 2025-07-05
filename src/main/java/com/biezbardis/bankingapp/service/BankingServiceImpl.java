package com.biezbardis.bankingapp.service;

import com.biezbardis.bankingapp.dto.AccountResponse;
import com.biezbardis.bankingapp.dto.BalanceRequest;
import com.biezbardis.bankingapp.dto.TransactionRequest;
import com.biezbardis.bankingapp.dto.TransactionResponse;
import com.biezbardis.bankingapp.entity.Account;
import com.biezbardis.bankingapp.entity.Client;
import com.biezbardis.bankingapp.entity.Currency;
import com.biezbardis.bankingapp.entity.Transaction;
import com.biezbardis.bankingapp.entity.TransactionType;
import com.biezbardis.bankingapp.exception.ClientNotFoundException;
import com.biezbardis.bankingapp.exception.InsufficientFundsException;
import com.biezbardis.bankingapp.exception.InvalidAmountException;
import com.biezbardis.bankingapp.repository.AccountRepository;
import com.biezbardis.bankingapp.repository.ClientRepository;
import com.biezbardis.bankingapp.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BankingServiceImpl implements BankingService {

    private final ClientRepository clientRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public BankingServiceImpl(ClientRepository clientRepository,
                              AccountRepository accountRepository,
                              TransactionRepository transactionRepository) {
        this.clientRepository = clientRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public TransactionResponse deposit(TransactionRequest request) {
        validateAmount(request.amount());

        Account account = getOrCreateAccount(request.clientName(), request.currency());

        account.setBalance(account.getBalance() + request.amount());
        accountRepository.save(account);

        Transaction transaction = new Transaction(
                account,
                TransactionType.DEPOSIT,
                request.amount(),
                account.getBalance(),
                request.description()
        );
        transactionRepository.save(transaction);

        return new TransactionResponse(
                "Deposit successful",
                account.getCurrency(),
                account.getBalance(),
                LocalDateTime.now()
        );
    }

    @Override
    public TransactionResponse withdraw(TransactionRequest request) {
        validateAmount(request.amount());

        Account account = getOrCreateAccount(request.clientName(), request.currency());

        if (account.getBalance().compareTo(request.amount()) < 0) {
            throw new InsufficientFundsException(
                    "Insufficient funds. Current balance: " + account.getBalance() +
                            " " + request.currency()
            );
        }

        account.setBalance(account.getBalance() - request.amount());
        accountRepository.save(account);

        Transaction transaction = new Transaction(
                account,
                TransactionType.WITHDRAWAL,
                request.amount(),
                account.getBalance(),
                request.description()
        );
        transactionRepository.save(transaction);

        return new TransactionResponse(
                "Withdrawal successful",
                account.getCurrency(),
                account.getBalance(),
                LocalDateTime.now()
        );
    }

    @Transactional
    @Override
    public AccountResponse getBalance(BalanceRequest request) {
        Account account = getOrCreateAccount(request.clientName(), request.currency());

        return new AccountResponse(
                account.getId(),
                account.getCurrency(),
                account.getBalance()
        );
    }

    @Transactional
    @Override
    public List<AccountResponse> getAllAccounts(String clientName) {
        Client client = validateClient(clientName);

        return accountRepository.findByClientId(client.getId())
                .stream()
                .map(account -> new AccountResponse(
                        account.getId(),
                        account.getCurrency(),
                        account.getBalance()
                ))
                .collect(Collectors.toList());
    }

    private Account getOrCreateAccount(String clientName, Currency currency) {
        Client client = validateClient(clientName);

        return accountRepository.findByClientIdAndCurrency(client.getId(), currency)
                .orElseGet(() -> {
                    Account newAccount = new Account(client, currency);
                    return accountRepository.save(newAccount);
                });
    }

    private Client validateClient(String clientName) {
        return clientRepository.findByName(clientName)
                .orElseThrow(() -> new ClientNotFoundException("Client not found: " + clientName));
    }

    private void validateAmount(Double amount) {
        if (amount <= 0) {
            throw new InvalidAmountException("Amount must be positive");
        }
    }

}
