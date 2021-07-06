package com.example.Balayage.regles;

public class StatsException {
    private String type;
    private String message;
    private Integer nombreOccurences;
    private String reglesConcernees;


    public StatsException(String type, String message, Integer nombreOccurences, String reglesConcernees) {
        this.type = type;
        this.message = message;
        this.nombreOccurences = nombreOccurences;
        this.reglesConcernees = reglesConcernees;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setNombreOccurences(Integer nombreOccurences) {
        this.nombreOccurences = nombreOccurences;
    }

    public Integer getNombreOccurences() {
        return nombreOccurences;
    }

    public String getReglesConcernees() {
        return reglesConcernees;
    }

    public void setReglesConcernees(String reglesConcernees) {
        this.reglesConcernees = reglesConcernees;
    }

    public boolean equals(Exception e){
        return (this.message.equals(e.getMessage()) && this.getType().equals( e.getClass().getCanonicalName()));
    }

    @Override
    public String toString() {
        return "StatsException{" +
                "Type='" + type + '\'' +
                ", message='" + message + '\'' +
                ", nombre=" + nombreOccurences +
                ", reglesConcernees='" + reglesConcernees + '\'' +
                '}';
    }
}