package com.biezbardis.bankingapp.service;

import com.biezbardis.bankingapp.dto.ClientRequest;
import com.biezbardis.bankingapp.dto.ClientResponse;

import java.util.List;

/**
 * Service interface for managing client-related operations such as creation and retrieval.
 */
public interface ClientService {

    /**
     * Creates a new client based on the provided request data.
     *
     * @param clientRequest the request object containing client details (name).
     * @return the created client information wrapped in a response object.
     * @throws ClientAlreadyExistsException if a client with the same unique identifier already exists.
     */
    ClientResponse create(ClientRequest clientRequest);

    /**
     * Retrieves a list of all registered clients.
     *
     * @return a list of client responses; returns an empty list if no clients exist.
     */
    List<ClientResponse> getClients();
}
