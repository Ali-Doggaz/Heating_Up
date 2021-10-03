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

public class FireAndAirQualityScraper {
    public static String getAirQuality(String city) throws JSONException, IOException, InterruptedException {

        String api_endpoint = "https://api.ambeedata.com/latest/by-city?city=" + city;

        //Create the GET request
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(api_endpoint))
                .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .setHeader("x-api-key", "31160d5bc60bef9d951b8f3dee130c2b197b2dd0c35f45a0c58554cde03ce2ef")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        //Map json answer to variables
        //Extract the AQI (air quality index)
        String AQI = new JSONObject(response.body()).getJSONArray("stations").getJSONObject(0).getString("AQI");
        return AQI;
    }

}
