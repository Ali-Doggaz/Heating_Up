package com.example.Balayage;

import com.example.Balayage.client.Client;
import com.example.Balayage.client.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class BalayageApplication {

	public static void main(String[] args) {
		SpringApplication.run(BalayageApplication.class, args);
	}

}
