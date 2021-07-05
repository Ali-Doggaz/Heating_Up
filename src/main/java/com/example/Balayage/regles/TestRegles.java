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

@Component
public class TestRegles {

    @Autowired
    private ClientService clientService;

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
        //Boucle sur toutes les règles et c
        for (int i=1; i<=regles.length; i++) {
            String regle = regles[i-1];
            Boolean boolTestResult = parser.parseExpression(regle)
                    .getValue(clientContext, Boolean.class);
            //Si un test a echoué, on creer le clientTestResult et on arrete le traitement
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

}
