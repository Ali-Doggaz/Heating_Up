package com.example.Balayage.Scraper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeatherApiScraper {

    // Calls the WeatherAPI endpoint and collects all climatic data related to your city/country, for the current time
    public Map<String, String> getCurrentWeatherDetails(String city) throws JSONException, IOException, InterruptedException {

        Map<String, String> values = new HashMap<String, String>() {{
            put("location", city);
        }};

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper
                .writeValueAsString(values);

        //Create the POST request
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.m3o.com/v1/weather/Now"))
                .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .setHeader("Authorization", "Bearer Mjc2OGVmMjUtZTIxNi00NWQ2LWIzMDctYzk2ODA2MWFjYjlh")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        //Map json answer to variables
        JSONObject obj = new JSONObject(response.body());

        Map<String,String> climatic_variables = new HashMap<String,String>();
        climatic_variables.put("humidity", obj.getString("humidity"));
        climatic_variables.put("temp_c", obj.getString("temp_c"));
        climatic_variables.put("temp_f", obj.getString("temp_f"));
        climatic_variables.put("feels_like_c", obj.getString("feels_like_c"));
        climatic_variables.put("cloud", obj.getString("cloud"));
        climatic_variables.put("condition", obj.getString("condition"));
        climatic_variables.put("wind_kph", obj.getString("wind_kph"));

        return climatic_variables;
    }

    // Calls the WeatherAPI endpoint and collects all climatic data related to your city/country, for the current time
    public List<Map<String, String>> getFutureWeatherDetails(String city) throws JSONException, IOException, InterruptedException {
        List<Map<String, String>> list_upcoming_weathers = new ArrayList<Map<String, String>>();
        Map<String, String> values = new HashMap<String, String>() {{
            put("location", city);
            put("days", "5");
        }};

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper
                .writeValueAsString(values);

        //Create the POST request
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.m3o.com/v1/weather/Forecast"))
                .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .setHeader("Authorization", "Bearer Mjc2OGVmMjUtZTIxNi00NWQ2LWIzMDctYzk2ODA2MWFjYjlh")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        //Map json answer to variables
        JSONObject obj = new JSONObject(response.body());

        JSONArray upcoming_weather_list = obj.getJSONArray("forecast");
        for (int i = 0; i < upcoming_weather_list.length(); i++)
        {
            Map<String,String> temp_climatic_variables = new HashMap<String,String>();
            JSONObject temp_weather = upcoming_weather_list.getJSONObject(i);
            temp_climatic_variables.put("max_temp_c", temp_weather.getString("max_temp_c"));
            temp_climatic_variables.put("min_temp_c", temp_weather.getString("min_temp_c"));
            temp_climatic_variables.put("max_temp_f", temp_weather.getString("max_temp_f"));
            temp_climatic_variables.put("min_temp_f", temp_weather.getString("min_temp_f"));
            temp_climatic_variables.put("chance_of_rain", temp_weather.getString("chance_of_rain"));
            list_upcoming_weathers.add(temp_climatic_variables);
        }
        return list_upcoming_weathers;
    }

    public WeatherApiScraper() throws IOException, InterruptedException, JSONException {
    }


}
