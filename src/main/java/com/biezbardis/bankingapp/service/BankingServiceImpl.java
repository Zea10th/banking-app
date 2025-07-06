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
    protected static final String AMOUNT_MUST_BE_POSITIVE = "Amount must be positive";
    protected static final String CLIENT_NOT_FOUND_STARTING = "Client not found: ";
    protected static final String DEPOSIT_SUCCESSFUL = "Deposit successful";
    protected static final String INSUFFICIENT_FUNDS_STARTING = "Insufficient funds. Current balance: ";
    protected static final String WITHDRAWAL_SUCCESSFUL = "Withdrawal successful";

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
    public TransactionResponse execute(TransactionRequest request, TransactionType type) {
        validateAmount(request.amount());

        Account account = getOrCreateAccount(request.clientName(), request.currency());

        if (type == TransactionType.WITHDRAWAL && account.getBalance().compareTo(request.amount()) < 0) {
            throw new InsufficientFundsException(
                    INSUFFICIENT_FUNDS_STARTING + account.getBalance() + " " + request.currency()
            );
        }

        double balanceAfter = (type == TransactionType.WITHDRAWAL) ?
                account.getBalance() - request.amount() :
                account.getBalance() + request.amount();
        account.setBalance(balanceAfter);
        accountRepository.save(account);

        Transaction transaction = new Transaction(
                account,
                type,
                request.amount(),
                account.getBalance(),
                request.description()
        );
        transactionRepository.save(transaction);

        String message = (type == TransactionType.DEPOSIT) ? DEPOSIT_SUCCESSFUL : WITHDRAWAL_SUCCESSFUL;

        return new TransactionResponse(
                message,
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
        Client client = recieveClient(clientName);

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
        Client client = recieveClient(clientName);

        return accountRepository.findByClientIdAndCurrency(client.getId(), currency)
                .orElseGet(() -> {
                    Account newAccount = new Account(client, currency);
                    return accountRepository.save(newAccount);
                });
    }

    private Client recieveClient(String clientName) {
        return clientRepository.findByName(clientName)
                .orElseThrow(() -> new ClientNotFoundException(CLIENT_NOT_FOUND_STARTING + clientName));
    }

    private void validateAmount(Double amount) {
        if (amount <= 0) {
            throw new InvalidAmountException(AMOUNT_MUST_BE_POSITIVE);
        }
    }
}
