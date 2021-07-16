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
    //TODO check if possible to add one-to-many relationship
    private Long jobExecutionID;

    //TODO remove these 2 static variables - replace with table

    // Contient le nombre de declenchement totale de chaque regle durant le balayage
    @Transient
    private static Map<Integer, Integer> nbrDeclenchementRegles;
    // Contient le nombre d'exception declenchées par chaque règle
    @Transient
    private static Map<Integer, Integer> nbrExceptionsRegles;
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
        nbrDeclenchementRegles.put(numTestRate, nbrDeclenchementRegles.get(numTestRate)+1);
        nbrClientsTestes++;
        nbrSuspectsDetectes++;
        this.jobExecutionID = jobExecutionID;
        this.batchNumber = batchNumber;
    }

    //No-args constructor, needed for @Entity annotation
    public ClientTestResult() {
    }

    public static Map<Integer, Integer> getNbrDeclenchementRegles() {
        return nbrDeclenchementRegles;
    }

    public static String getStatsReport() {
        String str = "Nombre de clients testes: " + nbrClientsTestes;
        str+="\nNombre de suspects detectes: " + nbrSuspectsDetectes + " (" + ((float)nbrSuspectsDetectes/nbrClientsTestes *100) + "%)";
        return str;
    }

    public static void setNbrDeclenchementRegles(Map<Integer, Integer> nbrDeclenchementRegles) {
        ClientTestResult.nbrDeclenchementRegles = nbrDeclenchementRegles;
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

    public static Map<Integer, Integer> getNbrExceptionsRegles() {
        return nbrExceptionsRegles;
    }

    public static void setNbrExceptionsRegles(Map<Integer, Integer> nbrExceptionsRegles) {
        ClientTestResult.nbrExceptionsRegles = nbrExceptionsRegles;
    }

    public static void incrementNbrExceptionsRegle(int numRegle){
        nbrExceptionsRegles.put(numRegle, nbrExceptionsRegles.get(numRegle)+1);
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
