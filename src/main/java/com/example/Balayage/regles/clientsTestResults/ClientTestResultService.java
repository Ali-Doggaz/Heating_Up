package com.example.Balayage.regles.clientsTestResults;

import com.example.Balayage.client.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientTestResultService {

    @Autowired
    private final ClientTestResultRepository clientTestResultRepository;

    @Autowired
    public ClientTestResultService(ClientTestResultRepository clientTestResultRepository) {
        this.clientTestResultRepository = clientTestResultRepository;
    }

}
