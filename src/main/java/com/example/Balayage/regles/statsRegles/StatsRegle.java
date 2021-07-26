package com.example.Balayage.regles.statsRegles;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
public class StatsRegle implements Comparable<StatsRegle>{
    @Id
    @SequenceGenerator(
            name="client_sequence",
            sequenceName = "client_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "client_sequence"
    )
    @Column(name="id",
            updatable = false,
            nullable = false
    )
    private Long id;

    private int ruleNumber;                 //Numéro de la règle
    private int triggerNumber;              //Nombre de clients suspects identifiés par cette regle
    private int exceptionsTriggeredNumber; // Nombre d'execptions delenchées par cette regle
    private Long jobExecutionID;
    private int batchNumber;

    public StatsRegle() {
    }

    public StatsRegle( Long jobExecutionID, int batchNumber, int ruleNumber, int triggerNumber, int exceptionsTriggeredNumber) {
        this.ruleNumber = ruleNumber;
        this.triggerNumber = triggerNumber;
        this.exceptionsTriggeredNumber = exceptionsTriggeredNumber;
        this.jobExecutionID = jobExecutionID;
        this.batchNumber = batchNumber;
    }

    public StatsRegle(int ruleNumber, int triggerNumber, int exceptionsTriggeredNumber) {
        this.ruleNumber = ruleNumber;
        this.triggerNumber = triggerNumber;
        this.exceptionsTriggeredNumber = exceptionsTriggeredNumber;
    }

    @Override
    public int compareTo(StatsRegle statsRegle) {
        return -Integer.compare(this.triggerNumber, statsRegle.triggerNumber);
    }

    public int getRuleNumber() {
        return ruleNumber;
    }

    public void setRuleNumber(int id) {
        this.ruleNumber = ruleNumber;
    }

    public int getTriggerNumber() {
        return triggerNumber;
    }

    public void setTriggerNumber(int triggerNumber) {
        this.triggerNumber = triggerNumber;
    }

    public int getExceptionsTriggeredNumber() {
        return exceptionsTriggeredNumber;
    }

    public void setExceptionsTriggeredNumber(int exceptionsTriggeredNumber) {
        this.exceptionsTriggeredNumber = exceptionsTriggeredNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
