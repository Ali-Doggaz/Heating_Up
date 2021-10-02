package com.example.Balayage;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.json.JSONParser;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;


@SpringBootApplication
@EnableScheduling
public class BalayageApplication {

	public static void main(String[] args) {
		SpringApplication.run(BalayageApplication.class, args);
	}




	@Bean
	CommandLineRunner commandLineRunner() {
		return args -> {
			String country = "Paris"; //Todo change this
			//Initialize the post request parameters
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

			System.out.println(response.body());

			//Map json answer to variables
			JSONObject obj = new JSONObject(response.body());


			Integer humidity = obj.getInt("humidity");
			Integer temp_c = obj.getInt("temp_c");
			Integer feels_like_c = obj.getInt("feels_like_c");
			Integer cloud_percentage = obj.getInt("cloud");
			String condition = obj.getString("condition");
			Float wind_kph = Float.parseFloat(obj.getString("wind_kph"));


		};
	}

	@Bean
	public CorsFilter corsFilter() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowCredentials(true);
		corsConfiguration.setAllowedOriginPatterns(Collections.singletonList("*"));
		corsConfiguration.setAllowedHeaders(Arrays.asList("Origin", "Access-Control-Allow-Origin", "Content-Type",
				"Accept", "Authorization", "Origin, Accept", "X-Requested-With",
				"Access-Control-Request-Method", "Access-Control-Request-Headers"));
		corsConfiguration.setExposedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization",
				"Access-Control-Allow-Origin", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));
		corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
		urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
		return new CorsFilter(urlBasedCorsConfigurationSource);
	}

}
