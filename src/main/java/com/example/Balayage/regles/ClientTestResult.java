package com.example.Balayage.regles;

import com.example.Balayage.client.Client;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.ArrayList;
import java.util.Collections;

public class ClientTestResult{
    //TODO change 5 for an injected variable containing the number of rules

    // Contient le nombre de declenchement totale de chaque regle durant le balayage
    private static ArrayList<Integer> nbrDeclenchementRegles;
    private static Integer nbrSuspectsDetectes;
    private static Integer nbrClientsTestes;

    private long id;                // id du client
    private boolean testsReussis;   // true si tous les tests ont été réussis, false sinon
    private int numTestRate;        // Si un test n'a pas été réussi, le numéro de ce test sera stocké ici

    //constructeur à appeler en cas de succes
    ClientTestResult(Long id){
        this.id = id;
        this.testsReussis = true;
        numTestRate = -1;
        nbrClientsTestes++;
    }

    // Constructeur à appeler en cas d'echec d'un test
    ClientTestResult(Long id, int numTestRate){
        this.id = id;
        this.testsReussis = false;
        this.numTestRate = numTestRate;
        nbrDeclenchementRegles.set(numTestRate, nbrDeclenchementRegles.get(numTestRate)+1);
        nbrClientsTestes++;
        nbrSuspectsDetectes++;
    }

    public static ArrayList<Integer> getNbrDeclenchementRegles() {
        return nbrDeclenchementRegles;
    }

    public static String getStatsReport() {
        String str = "Nombre de clients testes: " + nbrClientsTestes;
        str+="\nNombre de suspects detectes: " + nbrSuspectsDetectes + " (" + ((float)nbrSuspectsDetectes/nbrClientsTestes *100) + "%)";
        str += "\nNombre de declenchement de chaque regle: ";
        for (int i=0; i< nbrDeclenchementRegles.size(); i++) {
            str += "\nRegle " + (i+1) + " declenchee " + nbrDeclenchementRegles.get(i) + " fois";
        }
        return str;
    }

    public static void setNbrDeclenchementRegles(ArrayList<Integer> nbrDeclenchementRegles) {
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

    public long getId() {
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
