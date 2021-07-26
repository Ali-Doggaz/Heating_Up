package com.example.Balayage.batch;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name="batch_config_params")
public class BatchConfigParams {

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

    private int chunkSize;
    private int pageSize;
    private int nbrClientsParRapport;
    private String cronExpression;

    public int getChunkSize() {
        return chunkSize;
    }

    public int getPageSize() {
        return pageSize;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public int getNbrClientsParRapport() {
        return nbrClientsParRapport;
    }

    public void setNbrClientsParRapport(int nbrClientsParRapport) {
        this.nbrClientsParRapport = nbrClientsParRapport;
    }

    public BatchConfigParams(int chunkSize, int pageSize, int nbrClientsParRapport, String cronExpression) {
        this.chunkSize = chunkSize;
        this.pageSize = pageSize;
        this.nbrClientsParRapport = nbrClientsParRapport;
        this.cronExpression = cronExpression;
    }

    public BatchConfigParams() {
    }
}
