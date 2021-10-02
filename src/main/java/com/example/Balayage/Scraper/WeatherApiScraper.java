package com.example.Balayage.Scraper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class WeatherApiScraper {

    // Calls the WeatherAPI endpoint and collects all climatic data related to your city/country
    public Map<String, String> call_api(String country) throws JSONException {

        Map<String, String> values = new HashMap<String, String>() {{
            put("location", country);
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
        climatic_variables.put("feels_like_c", obj.getString("feels_like_c"));
        climatic_variables.put("cloud", obj.getString("cloud"));
        climatic_variables.put("condition", obj.getString("condition"));
        climatic_variables.put("wind_kph", obj.getString("wind_kph"));

        return climatic_variables;
    }

    public WeatherApiScraper() throws IOException, InterruptedException, JSONException {
    }


}
