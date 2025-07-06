package com.biezbardis.bankingapp.service;

import com.biezbardis.bankingapp.dto.TransactionRequest;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static com.biezbardis.bankingapp.service.BankingServiceImpl.AMOUNT_MUST_BE_POSITIVE;
import static com.biezbardis.bankingapp.service.BankingServiceImpl.CLIENT_NOT_FOUND_STARTING;
import static com.biezbardis.bankingapp.service.BankingServiceImpl.DEPOSIT_SUCCESSFUL;
import static com.biezbardis.bankingapp.service.BankingServiceImpl.INSUFFICIENT_FUNDS_STARTING;
import static com.biezbardis.bankingapp.service.BankingServiceImpl.WITHDRAWAL_SUCCESSFUL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BankingServiceTest {
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private TransactionRepository transactionRepository;
    @InjectMocks
    private BankingServiceImpl service;

    // Some test values
    private final long clientId = 1L;
    private final String clientName = "testName";
    private final Currency currency = Currency.EUR;
    private final double currentBalance = 10.00;
    private final String description = "testDescription";
    private final long accountId = 100L;

    // deposit method
    @Test
    void shouldReturnDepositTransactionResponseWhenRequestIsValid() {
        double depositAmount = 10.0;
        var request = new TransactionRequest(clientName, currency, depositAmount, description);
        var client = new Client(clientId, clientName, Collections.emptyList());
        var account = new Account(accountId, client, currency, currentBalance);
        var transaction = new Transaction(account,
                TransactionType.DEPOSIT,
                request.amount(),
                account.getBalance() + depositAmount,
                request.description());

        when(clientRepository.findByName(clientName)).thenReturn(Optional.of(client));
        when(accountRepository.findByClientIdAndCurrency(clientId, currency)).thenReturn(Optional.of(account));
        when(transactionRepository.save(transaction)).thenReturn(transaction);

        var actual = service.execute(request, TransactionType.DEPOSIT);
        assertEquals(DEPOSIT_SUCCESSFUL, actual.message());
        assertEquals(Currency.EUR, actual.currency());
        assertEquals(currentBalance + depositAmount, actual.newBalance());
        assertInstanceOf(LocalDateTime.class, actual.timestamp());
        assertTrue(actual.timestamp().isBefore(LocalDateTime.now()));

        verify(clientRepository).findByName(clientName);
        verify(accountRepository).findByClientIdAndCurrency(clientId, currency);
        verify(transactionRepository).save(transaction);
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -100.0})
    void shouldThrowInvalidAmountExceptionWhenDepositInvalidAmount(double invalidAmount) {
        var request = new TransactionRequest(clientName, currency, invalidAmount, description);

        var thrown = assertThrows(InvalidAmountException.class,
                () -> service.execute(request, TransactionType.DEPOSIT));
        assertTrue(thrown.getMessage().contains(AMOUNT_MUST_BE_POSITIVE));
    }

    @ParameterizedTest
    @EnumSource(TransactionType.class)
    void shouldThrowClientNotFoundExceptionWhenClientNotFound(TransactionType type) {
        var request = new TransactionRequest(clientName, currency, 5.0, description);

        when(clientRepository.findByName(clientName)).thenReturn(Optional.empty());

        var thrown = assertThrows(ClientNotFoundException.class,
                () -> service.execute(request, type));
        assertTrue(thrown.getMessage().contains(CLIENT_NOT_FOUND_STARTING + clientName));

        verify(clientRepository).findByName(clientName);
    }

    // deposit method
    @Test
    void shouldReturnWithdrawalTransactionResponseWhenRequestIsValid() {
        double withdrawAmount = 5.0;
        var request = new TransactionRequest(clientName, currency, withdrawAmount, description);
        var client = new Client(clientId, clientName, Collections.emptyList());
        var account = new Account(accountId, client, currency, currentBalance);
        var transaction = new Transaction(account,
                TransactionType.WITHDRAWAL,
                request.amount(),
                account.getBalance() - withdrawAmount,
                request.description());

        when(clientRepository.findByName(clientName)).thenReturn(Optional.of(client));
        when(accountRepository.findByClientIdAndCurrency(clientId, currency)).thenReturn(Optional.of(account));
        when(transactionRepository.save(transaction)).thenReturn(transaction);

        var actual = service.execute(request, TransactionType.WITHDRAWAL);
        assertEquals(WITHDRAWAL_SUCCESSFUL, actual.message());
        assertEquals(Currency.EUR, actual.currency());
        assertEquals(currentBalance - withdrawAmount, actual.newBalance());
        assertInstanceOf(LocalDateTime.class, actual.timestamp());
        assertTrue(actual.timestamp().isBefore(LocalDateTime.now()));

        verify(clientRepository).findByName(clientName);
        verify(accountRepository).findByClientIdAndCurrency(clientId, currency);
        verify(transactionRepository).save(transaction);
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -5.0})
    void shouldThrowInvalidAmountExceptionWhenWithdrawInvalidAmount(double invalidAmount) {
        var request = new TransactionRequest(clientName, currency, invalidAmount, description);

        var thrown = assertThrows(InvalidAmountException.class,
                () -> service.execute(request, TransactionType.WITHDRAWAL));
        assertTrue(thrown.getMessage().contains(AMOUNT_MUST_BE_POSITIVE));
    }

    @Test
    void shouldThrowInsufficientFundsExceptionWhenWithdrawInvalidAmount() {
        double withdrawAmount = 20.0;
        var request = new TransactionRequest(clientName, currency, withdrawAmount, description);
        var client = new Client(clientId, clientName, Collections.emptyList());
        var account = new Account(accountId, client, currency, currentBalance);

        when(clientRepository.findByName(clientName)).thenReturn(Optional.of(client));
        when(accountRepository.findByClientIdAndCurrency(clientId, currency)).thenReturn(Optional.of(account));

        var thrown = assertThrows(InsufficientFundsException.class,
                () -> service.execute(request, TransactionType.WITHDRAWAL));
        assertTrue(thrown.getMessage().contains(INSUFFICIENT_FUNDS_STARTING + currentBalance + " " + currency));

        verify(clientRepository).findByName(clientName);
        verify(accountRepository).findByClientIdAndCurrency(clientId, currency);
    }

    @Test
    void getBalance() {
    }

    @Test
    void getAllAccounts() {
    }
}