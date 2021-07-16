package com.example.Balayage.regles.clientsTestResults;

import com.example.Balayage.client.Client;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Transient;
import java.util.Map;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class ClientTestResult extends Client{

    private boolean testsReussis;   // true si tous les tests ont été réussis, false sinon
    private int numTestRate;        // Si un test n'a pas été réussi, le numéro de ce test sera stocké ici

    //TODO check if possible to add one-to-many relationship
    private Long jobID;

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
    public ClientTestResult(Long id, String nationalite, int age, double revenus, Long jobID) {
        super(id, nationalite, age, revenus);
        this.testsReussis = true;
        numTestRate = -1;
        nbrClientsTestes++;
        this.jobID = jobID;
    }


    // Constructeur à appeler en cas d'echec d'un test
    public ClientTestResult(Long id, String nationalite, int age, double revenus, int numTestRate){
        super(id, nationalite, age, revenus);
        this.testsReussis = false;
        this.numTestRate = numTestRate;
        nbrDeclenchementRegles.put(numTestRate, nbrDeclenchementRegles.get(numTestRate)+1);
        nbrClientsTestes++;
        nbrSuspectsDetectes++;
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

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
                "id=" + id +
                ", testsReussis=" + testsReussis +
                ", numTestRate=" + numTestRate +
                '}';
    }
}
