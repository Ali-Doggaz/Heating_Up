package com.example.Balayage.regles.statsExceptions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class StatsExceptionService {

    @Autowired
    private final StatsExceptionRepository statsExceptionRepository;

    @Autowired
    public StatsExceptionService(StatsExceptionRepository statsExceptionRepository) {
        this.statsExceptionRepository = statsExceptionRepository;
    }

    public List<StatsException> getStatsException(Long jobExecutionID, int batchNumber, String msg, String type){
        return statsExceptionRepository.getStatsException(jobExecutionID, batchNumber, msg, type);
    }

    public void updateStatsException(StatsException statsException){
        statsExceptionRepository.save(statsException);
    }

    public void addStatsException(StatsException statsException){
        statsExceptionRepository.save(statsException);
    }

    public ArrayList<StatsException> getBatchStats(Long jobExecutionID, int batchNumber){
        return statsExceptionRepository.getBatchStats(jobExecutionID, batchNumber);
    }

}
