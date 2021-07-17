package com.example.Balayage.regles.statsExceptions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StatsExceptionRepository extends JpaRepository<StatsException, Long> {

    @Query("SELECT sE FROM StatsException sE " +
            "WHERE sE.jobExecutionID = :jobExecutionID " +
            "AND sE.batchNumber = :batchNumber " +
            "AND sE.message = :msg "+
            "AND sE.type = :type"
    )
    List<StatsException> getStatsException(Long jobExecutionID, int batchNumber, String msg, String type);
}
