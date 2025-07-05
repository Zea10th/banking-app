package com.biezbardis.bankingapp.service;

import com.biezbardis.bankingapp.dto.ClientRequest;
import com.biezbardis.bankingapp.dto.ClientResponse;
import com.biezbardis.bankingapp.entity.Client;
import com.biezbardis.bankingapp.repository.ClientRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public ClientResponse create(ClientRequest clientRequest) {
        var client = clientRepository.save(new Client(clientRequest.getClientName()));
        return new ClientResponse(client.getId(), clientRequest.getClientName());
    }

    @Override
    public List<ClientResponse> getClients() {
        var clients = clientRepository.findAll();
        return clients.stream()
                .map(client -> new ClientResponse(client.getId(), client.getName()))
                .toList();
    }
}
