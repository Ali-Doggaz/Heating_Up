package com.example.Balayage.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class ClientService {

    @Autowired
    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Transactional
    public void updateClientSuspicionStatus(Client client, boolean suspect) {
        client.setSuspect(suspect);
        clientRepository.save(client);
    }


}
