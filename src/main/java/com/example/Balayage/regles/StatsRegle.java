package com.example.Balayage.regles;

public class StatsRegle implements Comparable<StatsRegle>{
    private int id;
    private int triggerNumber;
    private int exceptionsTriggeredNumber;

    public StatsRegle(int id, int triggerNumber, int exceptionsTriggeredNumber) {
        this.id = id;
        this.triggerNumber = triggerNumber;
        this.exceptionsTriggeredNumber = exceptionsTriggeredNumber;
    }

    @Override
    public int compareTo(StatsRegle statsRegle) {
        return -Integer.compare(this.triggerNumber, statsRegle.triggerNumber);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
