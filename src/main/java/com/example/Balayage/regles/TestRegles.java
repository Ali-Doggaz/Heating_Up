package com.example.Balayage.regles;

import com.example.Balayage.client.Client;
import com.example.Balayage.client.ClientRepository;
import com.example.Balayage.client.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class TestRegles {

    @Autowired
    private ClientService clientService;

    private String[] regles;
    private final String RULES_FOLDER = "src/main/resources/rules.txt";

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

    public ClientTestResult fireAll(Client client) {

        ExpressionParser parser = new SpelExpressionParser();
        //create EvaluationContext
        StandardEvaluationContext clientContext = new StandardEvaluationContext(client);

        //ArrayList<String> regles = new ArrayList<>();
        //regles.addAll(List.of("revenus < 850000 || (revenus <= 800000 && age<60)", "age > 19 || revenus > 100000",
                //"nationalite!='Tunisie'", "age < 90 && age > 20", "age != 35"));
        for (int i=0; i<regles.length; i++) {
            String regle = regles[i];
            Boolean boolTestResult = parser.parseExpression(regle)
                    .getValue(clientContext, Boolean.class);
            //Si un test a echoué, on creer le clientTestResult et on arrete le traitement
            if (!boolTestResult) {
                ClientTestResult clientTestResult = new ClientTestResult(client.getId(), client.getNationalite(), client.getAge(), client.getRevenus(), i);
                if(!client.isSuspect()) {
                    clientService.updateClientSuspicionStatus(client, true); // Update le client dans la BD
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

}
