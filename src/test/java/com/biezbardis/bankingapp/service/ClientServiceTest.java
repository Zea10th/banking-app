package com.biezbardis.bankingapp.service;

import com.biezbardis.bankingapp.dto.ClientRequest;
import com.biezbardis.bankingapp.dto.ClientResponse;
import com.biezbardis.bankingapp.entity.Client;
import com.biezbardis.bankingapp.exception.ClientAlreadyExistsException;
import com.biezbardis.bankingapp.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.biezbardis.bankingapp.service.ClientServiceImpl.CLIENT_ALREADY_EXISTS_ENDING;
import static com.biezbardis.bankingapp.service.ClientServiceImpl.CLIENT_ALREADY_EXISTS_STARTING;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {
    @Mock
    private ClientRepository clientRepository;
    @InjectMocks
    private ClientServiceImpl service;

    // Some test values
    private final long clientId = 1L;
    private final String clientName = "testName";

    @Test
    void shouldReturnClientResponseWhenRequestIsValid() {
        var initialClient = new Client(clientName);
        var storedClient = new Client(clientId, clientName, Collections.emptyList());

        when(clientRepository.findByName(clientName)).thenReturn(Optional.empty());
        when(clientRepository.save(initialClient)).thenReturn(storedClient);

        var request = new ClientRequest(clientName);

        var actual = service.create(request);

        assertEquals(clientId, actual.id());
        assertEquals(clientName, actual.clientName());

        verify(clientRepository).findByName(clientName);
        verify(clientRepository).save(initialClient);
    }

    @Test
    void shouldThrowClientAlreadyExistsExceptionWhenClientAlreadyExists() {
        when(clientRepository.findByName(clientName)).thenReturn(Optional.of(new Client()));

        var request = new ClientRequest(clientName);

        var thrown = assertThrows(ClientAlreadyExistsException.class,
                () -> service.create(request));
        assertTrue(thrown.getMessage().contains(CLIENT_ALREADY_EXISTS_STARTING + clientName + CLIENT_ALREADY_EXISTS_ENDING));

        verify(clientRepository).findByName(clientName);
    }

    @Test
    void getClients() {
        var client1 = new Client(clientId, clientName + "1", Collections.emptyList());
        var client2 = new Client(2L, clientName + "2", Collections.emptyList());
        var client3 = new Client(3L, clientName + "3", Collections.emptyList());

        when(clientRepository.findAll()).thenReturn(List.of(client1, client2, client3));

        var actual = service.getClients();
        var expectedClient1 = new ClientResponse(clientId, clientName + "1");
        var expectedClient2 = new ClientResponse(2L, clientName + "2");
        var expectedClient3 = new ClientResponse(3L, clientName + "3");


        assertEquals(3, actual.size());
        assertTrue(actual.contains(expectedClient1));
        assertTrue(actual.contains(expectedClient2));
        assertTrue(actual.contains(expectedClient3));

        verify(clientRepository).findAll();
    }
}