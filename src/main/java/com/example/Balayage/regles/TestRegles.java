package com.example.Balayage.regles;

import com.example.Balayage.client.Client;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.ArrayList;
import java.util.List;

public class TestRegles {

    public static ClientTestResult fireAll(Client client) {

        ExpressionParser parser = new SpelExpressionParser();
        //create EvaluationContext
        StandardEvaluationContext clientContext = new StandardEvaluationContext(client);

        ArrayList<String> regles = new ArrayList<>();
        regles.addAll(List.of("revenus < 850000 || (revenus <= 800000 && age<60)", "age > 19 || revenus > 100000",
                "nationalite!='Tunisie'", "age < 90 && age > 20", "age != 35"));
        for (int i=0; i<regles.size(); i++) {
            String regle = regles.get(i);
            System.out.println("Regle:" + regle);
            Boolean boolTestResult = parser.parseExpression(regle)
                    .getValue(clientContext, Boolean.class);
            System.out.println("Result of test= " + boolTestResult);
            //Si le test a echoué, on creer le clientTestResult et on arrete le traitement
            if (!boolTestResult) {
                ClientTestResult clientTestResult = new ClientTestResult(client.getId(), i);
                client.setSuspect(true);
                return clientTestResult;
            }
        }

        //Si tous les test ont été reussis
        client.setSuspect(false);
        return(new ClientTestResult(client.getId()));

    }

}
