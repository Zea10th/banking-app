package com.biezbardis.bankingapp.controller;

import com.biezbardis.bankingapp.dto.AccountResponse;
import com.biezbardis.bankingapp.dto.BalanceRequest;
import com.biezbardis.bankingapp.dto.ClientResponse;
import com.biezbardis.bankingapp.dto.TransactionRequest;
import com.biezbardis.bankingapp.dto.TransactionResponse;
import com.biezbardis.bankingapp.entity.TransactionType;
import com.biezbardis.bankingapp.service.BankingService;
import com.biezbardis.bankingapp.service.BankingServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/banking")
@Tag(name = "Banking", description = "Banking Operations")
public class BankingController {

    private final BankingService bankingService;

    public BankingController(BankingServiceImpl bankingService) {
        this.bankingService = bankingService;
    }

    @Operation(summary = "Deposit", description = "Executes a transaction of the deposit type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deposit Completed",
                    content = @Content(schema = @Schema(implementation = TransactionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid Request"),
            @ApiResponse(responseCode = "404", description = "Client Not Found")
    })
    @PostMapping(value = "/deposit",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransactionResponse> deposit(@Valid @RequestBody TransactionRequest request) {
        TransactionResponse response = bankingService.execute(request, TransactionType.DEPOSIT);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Withdraw", description = "Executes a transaction of the withdrawal type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Withdraw Completed",
                    content = @Content(schema = @Schema(implementation = TransactionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid Request"),
            @ApiResponse(responseCode = "404", description = "Client Not Found")
    })
    @PostMapping(value = "/withdraw",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransactionResponse> withdraw(@Valid @RequestBody TransactionRequest request) {
        TransactionResponse response = bankingService.execute(request, TransactionType.WITHDRAWAL);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Balance", description = "Retrieves the current balance for the specified account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Balance Provided",
                    content = @Content(schema = @Schema(implementation = AccountResponse.class))),
            @ApiResponse(responseCode = "404", description = "Client Not Found")
    })
    @GetMapping(value = "/balance",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountResponse> getBalance(@Valid @RequestBody BalanceRequest request) {
        AccountResponse response = bankingService.getBalance(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Accounts", description = "Retrieves all accounts associated with a given client name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Accounts Provided", content = @Content(schema = @Schema(implementation = AccountResponse.class))),
            @ApiResponse(responseCode = "404", description = "Client Not Found")
    })
    @GetMapping(value = "/accounts",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AccountResponse>> getAllAccounts(@Valid @RequestBody ClientResponse response) {
        List<AccountResponse> accounts = bankingService.getAllAccounts(response.clientName());
        return ResponseEntity.ok(accounts);
    }
}
