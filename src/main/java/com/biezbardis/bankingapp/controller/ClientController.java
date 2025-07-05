package com.biezbardis.bankingapp.controller;

import com.biezbardis.bankingapp.dto.ClientRequest;
import com.biezbardis.bankingapp.dto.ClientResponse;
import com.biezbardis.bankingapp.service.ClientService;
import com.biezbardis.bankingapp.service.ClientServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientServiceImpl clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/create")
    public ResponseEntity<ClientResponse> create(@Valid @RequestBody ClientRequest request) {
        ClientResponse response = clientService.create(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping()
    public ResponseEntity<List<ClientResponse>> getClients() {
        List<ClientResponse> response = clientService.getClients();
        return ResponseEntity.ok(response);
    }
}
