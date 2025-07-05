package com.biezbardis.bankingapp.controller;

import com.biezbardis.bankingapp.dto.AccountResponse;
import com.biezbardis.bankingapp.dto.BalanceRequest;
import com.biezbardis.bankingapp.dto.ClientResponse;
import com.biezbardis.bankingapp.dto.TransactionRequest;
import com.biezbardis.bankingapp.dto.TransactionResponse;
import com.biezbardis.bankingapp.service.BankingService;
import com.biezbardis.bankingapp.service.BankingServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/banking")
public class BankingController {

    private final BankingService bankingService;

    public BankingController(BankingServiceImpl bankingService) {
        this.bankingService = bankingService;
    }

    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> deposit(@Valid @RequestBody TransactionRequest request) {
        TransactionResponse response = bankingService.deposit(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(@Valid @RequestBody TransactionRequest request) {
        TransactionResponse response = bankingService.withdraw(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/balance")
    public ResponseEntity<AccountResponse> getBalance(@Valid @RequestBody BalanceRequest request) {
        AccountResponse response = bankingService.getBalance(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<AccountResponse>> getAllAccounts(@Valid @RequestBody ClientResponse response) {
        List<AccountResponse> accounts = bankingService.getAllAccounts(response.getClientName());
        return ResponseEntity.ok(accounts);
    }
}
