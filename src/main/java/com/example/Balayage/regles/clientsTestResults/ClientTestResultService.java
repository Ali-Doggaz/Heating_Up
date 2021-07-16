package com.example.Balayage.regles.clientsTestResults;

import com.example.Balayage.client.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClientTestResultService {

    @Autowired
    private final ClientTestResultRepository clientTestResultRepository;

    @Autowired
    public ClientTestResultService(ClientTestResultRepository clientTestResultRepository) {
        this.clientTestResultRepository = clientTestResultRepository;
    }

    public void add(ClientTestResult clientTestResult){
        clientTestResultRepository.save(clientTestResult);
    }

    public void addAll(List<? extends ClientTestResult> clientTestResults){
        clientTestResultRepository.saveAll(clientTestResults);
    }
}
