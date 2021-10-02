package com.example.Balayage.Util;

import com.example.Balayage.Alert.Alert;
import com.example.Balayage.Risk_assessment_tests.TestBigTemperatureIncrease;
import com.example.Balayage.Risk_assessment_tests.Test_Dehydration;
import com.example.Balayage.Scraper.WeatherApiScraper;
import com.example.Balayage.client.Client;
import org.codehaus.jettison.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.Balayage.client.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin()
@RequestMapping("/Heating/")
public class Controller {

    @Autowired
    public ClientRepository clientRepository;

    @Autowired
    public WeatherApiScraper weatherApiScraper;

    @Autowired
    public Controller(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
        this.weatherApiScraper = weatherApiScraper;
    }

    //inscription client REST API
    @PostMapping("/user")
    public Client createUser (@RequestBody Client client){
        System.out.println(client.getCity());
        return clientRepository.save(client);
    }

    @GetMapping("/getNews")
    public ResponseEntity<List<Alert>> getAlertsForRegion(@RequestParam String city, @RequestParam String country, @RequestParam String email) {
        List<Alert> alerts = new ArrayList<>(); //TODO change 10 for the total number of tests
        String temp_test_result;
        try {
            Map<String, String> CurrentWeatherDetails = weatherApiScraper.getCurrentWeatherDetails(city);
            temp_test_result = Test_Dehydration.calculateRisk(Integer.parseInt(CurrentWeatherDetails.get("temp_f")), Integer.parseInt(CurrentWeatherDetails.get("humidity")));
            if (temp_test_result.contains("Alert")) {
                alerts.add(new Alert("High heat index!", temp_test_result));
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        try {
            List<Map<String, String>> FutureWeatherDetails = weatherApiScraper.getFutureWeatherDetails(city);
            temp_test_result = TestBigTemperatureIncrease.checkBigTemperatureIncrease(FutureWeatherDetails);
            if (temp_test_result.contains("Alert")) {
                alerts.add(new Alert("High upcoming temperature increase!", temp_test_result));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        //TODO add tests

        return ResponseEntity.ok(alerts);
    }
}
