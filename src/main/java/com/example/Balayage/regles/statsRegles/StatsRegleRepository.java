package com.example.Balayage.regles.statsRegles;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;

public interface StatsRegleRepository extends JpaRepository<StatsRegle, Long> {

    @Modifying
    @Query( "UPDATE StatsRegle s SET s.triggerNumber = s.triggerNumber + 1 " +
            "WHERE s.jobExecutionID = :jobExecutionID " +
            "AND s.batchNumber = :batchNumber " +
            "AND s.ruleNumber = :ruleNumber")
    int incrementNbrDeclenchementRegle(Long jobExecutionID, int batchNumber, int ruleNumber);

    @Modifying
    @Query("UPDATE StatsRegle s SET s.exceptionsTriggeredNumber = s.exceptionsTriggeredNumber + 1 " +
            "WHERE s.jobExecutionID = :jobExecutionID " +
            "AND s.batchNumber = :batchNumber " +
            "AND s.ruleNumber = :ruleNumber")
    int incrementNbrExceptionsRegle(Long jobExecutionID, int batchNumber, int ruleNumber);

    @Query("SELECT SR FROM StatsRegle SR " +
            "WHERE SR.jobExecutionID = :jobExecutionID " +
            "AND SR.batchNumber = :batchNumber "
    )
    ArrayList<StatsRegle> getBatchStats(Long jobExecutionID, int batchNumber);
}
