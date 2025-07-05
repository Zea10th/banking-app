package com.biezbardis.bankingapp.service;

import com.biezbardis.bankingapp.dto.ClientRequest;
import com.biezbardis.bankingapp.dto.ClientResponse;

import java.util.List;

public interface ClientService {
    ClientResponse create(ClientRequest clientRequest);

    List<ClientResponse> getClients();
}
