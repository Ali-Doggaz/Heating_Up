package com.example.Balayage.Risk_assessment_tests;

import java.util.List;
import java.util.Map;

public class TestBigTemperatureIncrease {

    public static String checkBigTemperatureIncrease(List<Map<String, String>> list_upcoming_weathers) {
        float max_temp =  Float.valueOf(list_upcoming_weathers.get(0).get("max_temp_c"));
        int highest_temp_index = 0;
        //Get highest upcoming temperature
        for(int i = 0; i < list_upcoming_weathers.size();i++){
            if ( Float.valueOf(list_upcoming_weathers.get(i).get("max_temp_c")) > max_temp) {
                max_temp = Float.valueOf(list_upcoming_weathers.get(i).get("max_temp_c"));
                highest_temp_index = i + 2;
            }
        }

        if (max_temp - Float.valueOf(list_upcoming_weathers.get(0).get("max_temp_c")) > 3)
            return "Alert: A big increase of temperature is expected in "+highest_temp_index+" days! We expect the temperature to be " +
                    "around " + max_temp + " Celsius degrees.";
        return "";
    }
}
