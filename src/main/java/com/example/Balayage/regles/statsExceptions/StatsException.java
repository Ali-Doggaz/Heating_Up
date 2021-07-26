package com.example.Balayage.regles.statsExceptions;

import org.hibernate.annotations.Type;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
public class StatsException {
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

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String type;
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String message;
    private Integer nombreOccurences;
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String reglesConcernees;
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String idsClientsConcernees;

    private Long jobExecutionID;
    private int batchNumber;

    public StatsException() {
    }

    public StatsException( Long jobExecutionID, int batchNumber, String type, String message, Integer nombreOccurences, String reglesConcernees,
                          String idsClientsConcernees) {
        this.type = type;
        this.message = message;
        this.nombreOccurences = nombreOccurences;
        this.reglesConcernees = reglesConcernees;
        this.idsClientsConcernees = idsClientsConcernees;
        this.jobExecutionID = jobExecutionID;
        this.batchNumber = batchNumber;
    }

    public StatsException(String type, String message, Integer nombreOccurences, String reglesConcernees, String idsClientsConcernees) {
        this.type = type;
        this.message = message;
        this.nombreOccurences = nombreOccurences;
        this.reglesConcernees = reglesConcernees;
        this.idsClientsConcernees = idsClientsConcernees;
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

    public String getIdsClientsConcernees() {
        return idsClientsConcernees;
    }

    public void setIdsClientsConcernees(String idsClientsConcernees) {
        this.idsClientsConcernees = idsClientsConcernees;
    }

    public boolean equals(Exception e){
        return (this.message.equals(e.getMessage()) && this.getType().equals( e.getClass().getCanonicalName()));
    }

    @Override
    public String toString() {
        return "StatsException{" +
                "type='" + type + '\'' +
                ", message='" + message + '\'' +
                ", nombreOccurences=" + nombreOccurences +
                ", reglesConcernees='" + reglesConcernees + '\'' +
                ", idsClientsConcernees='" + idsClientsConcernees + '\'' +
                '}';
    }
}
