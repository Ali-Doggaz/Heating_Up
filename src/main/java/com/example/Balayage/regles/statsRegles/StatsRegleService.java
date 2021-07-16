package com.example.Balayage.regles.statsRegles;

import com.example.Balayage.regles.clientsTestResults.ClientTestResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatsRegleService {

    @Autowired
    private final StatsRegleRepository statsRegleRepository;

    @Autowired
    public StatsRegleService(StatsRegleRepository statsRegleRepository) {
        this.statsRegleRepository = statsRegleRepository;
    }

}
