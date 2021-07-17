package com.example.Balayage.regles.statsRegles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class StatsRegleService {

    @Autowired
    private final StatsRegleRepository statsRegleRepository;

    @Autowired
    public StatsRegleService(StatsRegleRepository statsRegleRepository) {
        this.statsRegleRepository = statsRegleRepository;
    }

    public ArrayList<StatsRegle> getBatchStats(Long jobExecutionID, int batchNumber){
        return statsRegleRepository.getBatchStats(jobExecutionID, batchNumber);
    }

    public void incrementNbrExceptionsRegle(Long jobExecutionID, int batchNumber, int numRegle){
        statsRegleRepository.incrementNbrExceptionsRegle(jobExecutionID, batchNumber, numRegle);
    }

    public void incrementNbrDeclenchementRegle(Long jobExecutionID, int batchNumber, int numRegle){
        statsRegleRepository.incrementNbrDeclenchementRegle(jobExecutionID, batchNumber, numRegle);
    }

    public void initRow(Long jobExecutionId, int batch, int ruleNumber){
        statsRegleRepository.save(new StatsRegle(jobExecutionId, batch, ruleNumber, 0, 0));
    }
}
