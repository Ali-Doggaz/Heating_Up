package com.example.Balayage.regles;

public class StatsRegle implements Comparable<StatsRegle>{
    private int id;
    private int triggerNumber;

    public StatsRegle(int id, int triggerNumber) {
        this.id = id;
        this.triggerNumber = triggerNumber;
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
}
