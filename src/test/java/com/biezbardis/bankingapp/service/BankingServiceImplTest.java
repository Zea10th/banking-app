package com.biezbardis.bankingapp.service;

import com.biezbardis.bankingapp.dto.TransactionRequest;
import com.biezbardis.bankingapp.entity.Account;
import com.biezbardis.bankingapp.entity.Client;
import com.biezbardis.bankingapp.entity.Currency;
import com.biezbardis.bankingapp.entity.Transaction;
import com.biezbardis.bankingapp.entity.TransactionType;
import com.biezbardis.bankingapp.exception.ClientNotFoundException;
import com.biezbardis.bankingapp.exception.InvalidAmountException;
import com.biezbardis.bankingapp.repository.AccountRepository;
import com.biezbardis.bankingapp.repository.ClientRepository;
import com.biezbardis.bankingapp.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BankingServiceImplTest {
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
    private final double amount = 10.00;
    private final String description = "testDescription";
    private final long accountId = 100L;

    // deposit method
    @Test
    void shouldReturnTransactionResponseWhenRequestIsValid() {
        double depositAmount = 10.0;
        var request = new TransactionRequest(clientName, currency, depositAmount, description);
        var client = new Client(clientId, clientName, Collections.emptyList());
        var account = new Account(accountId, client, currency, amount);
        var transaction = new Transaction(account,
                TransactionType.DEPOSIT,
                request.amount(),
                account.getBalance() + depositAmount,
                request.description());

        when(clientRepository.findByName(clientName)).thenReturn(Optional.of(client));
        when(accountRepository.findByClientIdAndCurrency(clientId, currency)).thenReturn(Optional.of(account));
        when(transactionRepository.save(transaction)).thenReturn(transaction);

        var actual = service.deposit(request);
        assertEquals("Deposit successful", actual.message());
        assertEquals(Currency.EUR, actual.currency());
        assertEquals(amount + depositAmount, actual.newBalance());
        assertInstanceOf(LocalDateTime.class, actual.timestamp());
        assertTrue(actual.timestamp().isBefore(LocalDateTime.now()));

        verify(clientRepository).findByName(clientName);
        verify(accountRepository).findByClientIdAndCurrency(clientId, currency);
        verify(transactionRepository).save(transaction);
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -100.0})
    void shouldThrowInvalidAmountExceptionWhenInvalidAmount(double invalidAmount) {
        var request = new TransactionRequest(clientName, currency, invalidAmount, description);

        var thrown = assertThrows(InvalidAmountException.class,
                () -> service.deposit(request));
        assertTrue(thrown.getMessage().contains("Amount must be positive"));
    }

    @Test
    void shouldThrowClientNotFoundExceptionWhenClientNotFound() {
        var request = new TransactionRequest(clientName, currency, amount, description);

        when(clientRepository.findByName(clientName)).thenReturn(Optional.empty());

        var thrown = assertThrows(ClientNotFoundException.class,
                () -> service.deposit(request));
        assertTrue(thrown.getMessage().contains("Client not found: " + clientName));

        verify(clientRepository).findByName(clientName);
    }

    @Test
    void withdraw() {
    }

    @Test
    void getBalance() {
    }

    @Test
    void getAllAccounts() {
    }
}