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
                ClientTestResult clientTestResult = new ClientTestResult(client.getId(),false, i);
                return clientTestResult;
            }
        }

        //Si tous les test ont été reussis
        return(new ClientTestResult(client.getId(), true));


    }

}

//Boolean message = (Boolean) parser.parseExpression("'Hello World'.concat('!')").getValue();
//Integer val = parser.parseExpression("16 * 5").getValue(Integer.class);
// System.out.println("Result of 16*5 = " + val);
//boolean boolVal = parser.parseExpression("5 < 9").getValue(Boolean.class);
//System.out.println("4. Mathematical operator value:\n" + boolVal);
// 5. Logical operator
//boolVal = parser.parseExpression("400 > 200 && 200 < 500").getValue(Boolean.class);
//System.out.println("5. Logical operator value:\n" + boolVal);

// 6. Ternary operator
//String strVal = parser.parseExpression("'some value' != null ? 'some value' : 'default'").getValue(String.class);
//System.out.println("6. Ternary operator value:\n" + strVal);


// 8. Regex/matches operator
//boolVal = parser.parseExpression("'UPPERCASE STRING' matches '[A-Z\\s]+'").getValue(Boolean.class);
//System.out.println("8. Regex/matches operator value:\n" + boolVal);