package com.example.Balayage.regles.clientsTestResults;

import com.example.Balayage.client.Client;

import javax.persistence.*;
import java.util.Map;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
public class ClientTestResult{
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
    @Column(name="clientTestResultId",
            updatable = false,
            nullable = false
    )
    protected Long clientTestResultId;

    @ManyToOne
    @JoinColumn(name="id")
    private Client client;

    private boolean testsReussis;   // true si tous les tests ont été réussis, false sinon
    private int numTestRate;        // Si un test n'a pas été réussi, le numéro de ce test sera stocké ici
    private int batchNumber;
    private Long jobExecutionID;


    @Transient
    private static Integer nbrSuspectsDetectes;
    @Transient
    private static Integer nbrClientsTestes;


    //constructeur à appeler en cas de succes des tests
    public ClientTestResult(Client client, Long jobExecutionID, int batchNumber) {
        this.client = client;
        this.testsReussis = true;
        numTestRate = -1;
        nbrClientsTestes++;
        this.jobExecutionID = jobExecutionID;
        this.batchNumber = batchNumber;
    }


    // Constructeur à appeler en cas d'echec d'un test
    public ClientTestResult(Client client, int numTestRate, Long jobExecutionID, int batchNumber){
        this.client = client;
        this.testsReussis = false;
        this.numTestRate = numTestRate;
        nbrClientsTestes++;
        nbrSuspectsDetectes++;
        this.jobExecutionID = jobExecutionID;
        this.batchNumber = batchNumber;
    }

    //No-args constructor, needed for @Entity annotation
    public ClientTestResult() {
    }


    public static String getStatsReport() {
        String str = "Nombre de clients testes: " + nbrClientsTestes;
        str+="\nNombre de suspects detectes: " + nbrSuspectsDetectes + " (" + ((float)nbrSuspectsDetectes/nbrClientsTestes *100) + "%)";
        return str;
    }

    public static Integer getNbrSuspectsDetectes() {
        return nbrSuspectsDetectes;
    }

    public static void setNbrSuspectsDetectes(Integer nbrSuspectsDetectes) {
        ClientTestResult.nbrSuspectsDetectes = nbrSuspectsDetectes;
    }

    public static Integer getNbrClientsTestes() {
        return nbrClientsTestes;
    }

    public static void setNbrClientsTestes(Integer nbrClientsTestes) {
        ClientTestResult.nbrClientsTestes = nbrClientsTestes;
    }

    public Client getClient() {
        return client;
    }

    public void setId(Client client) {
        this.client = client;
    }

    public boolean isTestsReussis() {
        return testsReussis;
    }

    public void setTestsReussis(boolean testsReussis) {
        this.testsReussis = testsReussis;
    }

    public int getNumTestRate() {
        return numTestRate;
    }

    public void setNumTestRate(int numTestRate) {
        this.numTestRate = numTestRate;
    }

    @Override
    public String toString() {
        return "ClientTestResult{" +
                "id=" + client +
                ", testsReussis=" + testsReussis +
                ", numTestRate=" + numTestRate +
                '}';
    }
}
