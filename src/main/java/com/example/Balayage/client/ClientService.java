package com.example.Balayage.client;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ClientService {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> getClients(){
        return clientRepository.findAll();
    }

}
