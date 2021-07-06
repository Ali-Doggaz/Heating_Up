package com.example.Balayage.regles;

import com.example.Balayage.client.Client;
import com.example.Balayage.client.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

@Component
public class TestRegles {

    @Autowired
    private ClientService clientService;

    // Contient la liste de toutes les exceptions declenchées
    private static ArrayList<StatsException> statsExceptions;

    private String[] regles;
    private final String RULES_FOLDER = "src/main/resources/rules.txt";

    /**
     * Lis toutes les règles métiers à partir du fichier
     * "RULES_FOLDER" (attribut statique)
     */
    public void readRulesFromFile() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(RULES_FOLDER));
        String line;
        StringBuilder sb = new StringBuilder();
        while(( line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        regles = sb.toString().split("---------------------RULE---------------------");
    }


    /**
     * Fais passer tous les tests (règles metier) au client passé
     * en parametre, et génére une instance de ClientTestResult
     * contenant les résultats de ces tests
     *
     * @param client -- client à tester
     * @return ClientTestResult -- Instance contenant les stats du balayage/Scan
     */
    public ClientTestResult fireAll(Client client) {

        ExpressionParser parser = new SpelExpressionParser();
        //create EvaluationContext
        StandardEvaluationContext clientContext = new StandardEvaluationContext(client);
        Boolean boolTestResult;
        //Boucle sur toutes les règles
        outLoop:
        for (int i=1; i<=regles.length; i++) {
            String regle = regles[i-1];
            try {
                boolTestResult = parser.parseExpression(regle)
                        .getValue(clientContext, Boolean.class);
            }
            catch(Exception e) {
                // On est dans le cas ou la règle testée a provoqué une erreur imprévue

                //Incrementer le nombre d'exceptions provoquée par la régle actuelle (numéro i)
                ClientTestResult.incrementNbrExceptionsRegle(i);
                for(StatsException statsException: statsExceptions) {
                    // Si la meme exception (meme type et meme message) existe deja dans notre tableau de StatsException,
                    // on incremente son nombre d'occurences
                    if (statsException.equals(e)) {
                        //On incremente le nbr de declenchement de cette exception
                        statsException.setNombreOccurences(statsException.getNombreOccurences()+1);
                        //Si la règle declenche cette exception pour la premiere fois, le signal a l'instance statsException
                        if (!statsException.getReglesConcernees().contains(" "+i +" ")) {
                            statsException.setReglesConcernees(statsException.getReglesConcernees()+", "+i +" ");
                        }
                        continue outLoop;
                    }
                }
                //Si l'exception est provoquée pour la première fois, on l'ajoute a notre liste
                statsExceptions.add(new StatsException(e.getClass().getCanonicalName(), e.getMessage(), 1, " "+i+" "));
                continue;
            }
            // On est dans le cas ou le test a eu lieu sans exceptions/imprévus
            //Si un test a echoué, on créer le clientTestResult et on arrete le traitement
            if (!boolTestResult) {
                ClientTestResult clientTestResult = new ClientTestResult(client.getId(), client.getNationalite(), client.getAge(), client.getRevenus(), i);
                if(!client.isSuspect()) {
                    // Update le client dans la BD
                    clientService.updateClientSuspicionStatus(client, true);
                }
                return clientTestResult;
            }
        }

        //Si tous les test ont été reussis
        if(client.isSuspect()) {
            clientService.updateClientSuspicionStatus(client, false);
        }

        return(new ClientTestResult(client.getId(), client.getNationalite(), client.getAge(), client.getRevenus()));
    }


    public String[] getRegles() {
        return regles;
    }

    public static ArrayList<StatsException> getStatsExceptions() {
        return statsExceptions;
    }

    public static void setStatsExceptions(ArrayList<StatsException> statsExceptions) {
        TestRegles.statsExceptions = statsExceptions;
    }
}
