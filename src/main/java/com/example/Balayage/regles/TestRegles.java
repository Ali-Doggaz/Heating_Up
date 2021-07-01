package com.example.Balayage;

import com.example.Balayage.client.Client;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

public class TestRegles {

    public static void fireAll(Client client) {
        // 1. Method invocation
        ExpressionParser parser = new SpelExpressionParser();
        String message = (String) parser.parseExpression("'Hello World'.concat('!')").getValue();

        // 2. invokes 'getBytes()'
        byte[] bytes = (byte[]) parser.parseExpression("'Hello World'.concat('!')").getValue();

        // 3. Mathematical operator
        Integer val = parser.parseExpression("16 * 5").getValue(Integer.class);
        System.out.println("Result of 16*5 = " + val);

        // 4. Relational operator
        boolean boolVal = parser.parseExpression("5 < 9").getValue(Boolean.class);
        System.out.println("4. Mathematical operator value:\n" + boolVal);

        // 5. Logical operator
        boolVal = parser.parseExpression("400 > 200 && 200 < 500").getValue(Boolean.class);
        System.out.println("5. Logical operator value:\n" + boolVal);

        // 6. Ternary operator
        String strVal = parser.parseExpression("'some value' != null ? 'some value' : 'default'").getValue(String.class);
        System.out.println("6. Ternary operator value:\n" + strVal);


        // 8. Regex/matches operator
        boolVal = parser.parseExpression("'UPPERCASE STRING' matches '[A-Z\\s]+'").getValue(Boolean.class);
        System.out.println("8. Regex/matches operator value:\n" + boolVal);
    }

}

