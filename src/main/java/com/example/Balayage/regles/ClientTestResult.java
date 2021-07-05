package com.example.Balayage.regles;

import com.example.Balayage.client.Client;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class ClientTestResult extends Client{
    //TODO change 5 for an injected variable containing the number of rules

    // Contient le nombre de declenchement totale de chaque regle durant le balayage
    private static Map<Integer, Integer> nbrDeclenchementRegles;
    private static Integer nbrSuspectsDetectes;
    private static Integer nbrClientsTestes;

    private boolean testsReussis;   // true si tous les tests ont été réussis, false sinon
    private int numTestRate;        // Si un test n'a pas été réussi, le numéro de ce test sera stocké ici

    //constructeur à appeler en cas de succes
    public ClientTestResult(Long id, String nationalite, int age, double revenus) {
        super(id, nationalite, age, revenus);
        this.testsReussis = true;
        numTestRate = -1;
        nbrClientsTestes++;
    }


    // Constructeur à appeler en cas d'echec d'un test
    public ClientTestResult(Long id, String nationalite, int age, double revenus, int numTestRate){
        super(id, nationalite, age, revenus);
        this.testsReussis = false;
        this.numTestRate = numTestRate+1;
        nbrDeclenchementRegles.put(numTestRate, nbrDeclenchementRegles.get(numTestRate)+1);
        nbrClientsTestes++;
        nbrSuspectsDetectes++;
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

    @Override
    public String toString() {
        return "ClientTestResult{" +
                "id=" + id +
                ", testsReussis=" + testsReussis +
                ", numTestRate=" + numTestRate +
                '}';
    }
}
