package com.biezbardis.bankingapp.service;

import com.biezbardis.bankingapp.dto.ClientRequest;
import com.biezbardis.bankingapp.dto.ClientResponse;
import com.biezbardis.bankingapp.entity.Client;
import com.biezbardis.bankingapp.exception.ClientAlreadyExistsException;
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
        validateClient(clientRequest.clientName());

        var client = clientRepository.save(new Client(clientRequest.clientName()));
        return new ClientResponse(client.getId(), clientRequest.clientName());
    }

    @Override
    public List<ClientResponse> getClients() {
        var clients = clientRepository.findAll();
        return clients.stream()
                .map(client -> new ClientResponse(client.getId(), client.getName()))
                .toList();
    }

    private void validateClient(String clientName) {
        if (clientRepository.findByName(clientName).isPresent()) {
            throw new ClientAlreadyExistsException("Client with name '" + clientName + "' already exists.");
        }
    }
}
