package com.example.Balayage.Risk_assessment_tests;

public class Test_AirQuality {
    public static String AssessAirQuality(int AQI){
        if (AQI < 32) {
            return "";
        }
        else if (AQI < 50){
            return "Alert: The air quality index is " + AQI +". The quality of the air in your region is currently bad. Visit http://www.airqualityontario.com/science/aqi_description.php for more details";
        }
        else if (AQI < 100){
            return "Alert: The air quality index is \" + AQI +\". The quality of the air in your region is very bad. Visit http://www.airqualityontario.com/science/aqi_description.php for more details";
        }
        else{
            return "Alert: The air quality index is \" + AQI +\". The quality of the air in your region is extremely bad and can cause you several illnesses. Visit http://www.airqualityontario.com/science/aqi_description.php for more details";
        }
    }
}
