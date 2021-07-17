package com.example.Balayage.regles;

import com.example.Balayage.client.Client;
import com.example.Balayage.client.ClientService;
import com.example.Balayage.regles.clientsTestResults.ClientTestResult;
import com.example.Balayage.regles.clientsTestResults.ClientTestResultService;
import com.example.Balayage.regles.statsExceptions.StatsException;
import com.example.Balayage.regles.statsExceptions.StatsExceptionService;
import com.example.Balayage.regles.statsRegles.StatsRegleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class TestRegles {

    @Autowired
    private ClientService clientService;
    @Autowired
    private ClientTestResultService clientTestResultService;
    @Autowired
    private StatsRegleService statsRegleService;
    @Autowired
    private StatsExceptionService statsExceptionService;

    // Contient la liste de toutes les exceptions declenchées
    private static ArrayList<StatsException> statsExceptions;

    private String[] regles;
    private final String RULES_FOLDER = "src/main/resources/rules.txt";

    /**
     * Lis toutes les règles métiers à partir du fichier
     * "RULES_FOLDER" (attribut statique)
     */
    public int readRulesFromFile() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(RULES_FOLDER));
        String line;
        StringBuilder sb = new StringBuilder();
        while(( line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        regles = sb.toString().split("---------------------RULE---------------------");
        return regles.length;
    }


    /**
     * Fais passer tous les tests (règles metier) au client passé
     * en parametre, et génére une instance de ClientTestResult
     * contenant les résultats de ces tests
     *
     * @param client -- client à tester
     * @return ClientTestResult -- Instance contenant les stats du balayage/Scan
     */
    public ClientTestResult fireAll(Client client, Long jobExecutionID, int batchNumber) {

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
                statsRegleService.incrementNbrExceptionsRegle(jobExecutionID, batchNumber, i);


                List<StatsException> statsExceptionList = statsExceptionService.getStatsException(jobExecutionID, batchNumber, e.getMessage(), e.getClass().getCanonicalName());
                //if exception already exists
                if (statsExceptionList.size()>0) {
                    StatsException statsException = statsExceptionService.getStatsException(jobExecutionID, batchNumber, e.getMessage(), e.getClass().getCanonicalName()).get(0);
                    //On incremente le nbr de declenchement de cette exception
                    statsException.setNombreOccurences(statsException.getNombreOccurences()+1);
                    //Ajoute le client qui a declenché l'exception a la liste
                    statsException.setIdsClientsConcernees(statsException.getIdsClientsConcernees() + ", "+client.getId());
                    //Si la règle declenche cette exception pour la premiere fois, le signal a l'instance statsException
                    if (!statsException.getReglesConcernees().contains(" "+i +" ")) {
                        statsException.setReglesConcernees(statsException.getReglesConcernees()+", "+i +" ");
                    }
                    //enregister les modification des stats de l'exception dans la BD
                    statsExceptionService.updateStatsException(statsException);
                    continue;
                }
                else{
                    statsExceptionService.addStatsException(new StatsException(jobExecutionID, batchNumber, e.getClass().getCanonicalName(), e.getMessage(), 1, " "+i+" ", " "+client.getId()));
                    continue;
                }

            }

            // On est dans le cas ou le test a eu lieu sans exceptions/imprévus
            //Si un test a echoué, on créer le clientTestResult et on arrete le traitement
            if (!boolTestResult) {
                ClientTestResult clientTestResult = new ClientTestResult(client, i, jobExecutionID, batchNumber);
                statsRegleService.incrementNbrDeclenchementRegle(jobExecutionID, batchNumber, i);

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

        ClientTestResult clientTestResult = new ClientTestResult(client, jobExecutionID, batchNumber);

        return clientTestResult;

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
