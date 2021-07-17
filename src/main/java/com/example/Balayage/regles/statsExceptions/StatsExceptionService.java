package com.example.Balayage.regles.statsExceptions;

import com.example.Balayage.client.ClientRepository;
import com.example.Balayage.regles.statsRegles.StatsRegle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class StatsExceptionService {

    @Autowired
    private final StatsExceptionRepository statsExceptionRepository;

    @Autowired
    public StatsExceptionService(StatsExceptionRepository statsExceptionRepository) {
        this.statsExceptionRepository = statsExceptionRepository;
    }

    public void addRow(StatsException statsException){
        statsExceptionRepository.save(statsException);
    }


}
