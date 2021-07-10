/*package com.example.Balayage.client;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Random;

@Configuration
public class ClientConfig {
    @Bean
    CommandLineRunner commandLineRunner(ClientRepository clientRepository) {
        return args -> {
            clientRepository.deleteAll();
            String[] pays = {"Tunisie","Allemagne","Algerie","USA","France","Maroc","Congo","Italie",
            "Espagne","Mexique","Canada","Australie","Norvege","Chine","Japon","Russie"};

            ArrayList<Client> clients = new ArrayList<>();

            for(int i=0;i<11000;i++){
                Random rng = new Random();
                int min = 18;
                int max = 100;
                int upperBound = max - min + 1;
                int age = min + rng.nextInt(upperBound); // Generer un age aleatoire
                int rnd = rng.nextInt(pays.length); // Generer un indice de pays aleatoire
                int minrevenus = 0;int maxrevenus=1000000;
                double revenus = minrevenus + (maxrevenus - minrevenus) * rng.nextDouble();
                clients.add(new Client(pays[rnd],age,revenus));
            }

            clientRepository.saveAll(clients);
        };
    }
}
*/
