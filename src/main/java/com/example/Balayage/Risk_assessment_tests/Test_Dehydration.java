package com.example.Balayage.Risk_assessment_tests;

public class Test_Dehydration {

    private static double calculateHeatIndex ( int currentTempF, int currentHumidity ) {
        //Setting parameters for Function
        int temperature = currentTempF;
        double humidity = currentHumidity;
        final double C1 = -42.379;
        final double C2 = 2.04901523;
        final double C3 = 10.14333127;
        final double C4 = -0.22475541;
        final double C5 = -.00683783;
        final double C6 = -5.481717E-2;
        final double C7 = 1.22874E-3;
        final double C8 = 8.5282E-4;
        final double C9 = -1.99E-6;
        int T = temperature;
        double R = humidity;
        double T2 = temperature * temperature;
        double R2 = humidity * humidity;

        //Function of Calculating Heat Index
        double answer = C1 + (C2 * T) + (C3 * R) + (C4 * T * R) + (C5 * T2) + (C6 * R2) + (C7 * T2 * R) + (C8 * T * R2) + (C9 * T2 * R2);

        return answer;
    }

    String calculateRisk(int Temperature, int humidity){
        double heatindex = calculateHeatIndex(Temperature,humidity);
        if (heatindex < 91) return "";
        else if (heatindex < 105) return "Alert: heatindex is currently " + heatindex+ ". Be cautious and remember to drink water!";
        else if (heatindex < 130) return "Alert: heatindex is currently " + heatindex + ". You have to be extremely cautious! Remember to drink water!";
        else return "Alert: Heatindex is currently " + heatindex + ". This is extremely high and requires you to be as vigilent as possible. Stay home and don't forget to drink water! ";
    }

}
