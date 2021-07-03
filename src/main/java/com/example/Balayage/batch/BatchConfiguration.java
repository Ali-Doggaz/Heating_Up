
package com.example.Balayage.batch;

import com.example.Balayage.client.Client;
import com.example.Balayage.regles.ClientTestResult;
import com.example.Balayage.regles.TestRegles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;


@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(BatchConfiguration.class);

    @Autowired
    private DataSource dataSource;  //TODO CHECK THIS

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private ItemReader<Client> clientReader;

    @Autowired
    private ItemWriter<ClientTestResult> clientProcessingWriter;

    @Autowired
    private ItemProcessor<Client, ClientTestResult> clientProcessor;

    //@Autowired
    //private DataSource dataSource;

    @Bean
    public Job ScanJob() {
        JobExecutionListener listener = myjoblistener();
        Step step = stepBuilderFactory.get("Traitement-donnees-client")
                .<Client, ClientTestResult>chunk(1000)
                .reader(clientReader)
                .writer(clientProcessingWriter)
                .processor(clientProcessor)
                .allowStartIfComplete(true)
                .build();
        return jobBuilderFactory.get("Scan_Clients")
                .start(step)
                .listener(listener)
                .build();
    }

    @Bean
    public JobExecutionListener myjoblistener() {

        JobExecutionListener listener = new JobExecutionListener() {

            @Override
            public void beforeJob(JobExecution jobExecution) {
                //TODO Change the 5 with an injected variable containing the number of rules
                System.out.println("Initialisation de ClientTestResult");
                ClientTestResult.setNbrDeclenchementRegles(new ArrayList<>(Collections.nCopies(5, 0)));
                ClientTestResult.setNbrSuspectsDetectes(0);
                ClientTestResult.setNbrClientsTestes(0);
            }

            @Override
            public void afterJob(JobExecution jobExecution) {

                System.out.println("Job has been completed, generating report");
                //TODO Generate Jasper Report

            }
        };

        return listener ;
    }

    @Bean
    public ItemReader<Client> reader() {
        return new JdbcCursorItemReaderBuilder<Client>().name("the-reader")
                .sql("select id, nationalite, age, revenus, suspect from client").dataSource(dataSource)
                .rowMapper((ResultSet resultSet, int rowNum) -> {
                    if (!(resultSet.isAfterLast()) && !(resultSet.isBeforeFirst())) {
                        Client client = new Client();
                        client.setId(resultSet.getLong("id"));
                        client.setNationalite(resultSet.getString("nationalite"));
                        client.setAge(resultSet.getInt("age"));
                        client.setRevenus(resultSet.getDouble("revenus"));
                        client.setSuspect(resultSet.getBoolean("suspect"));
                        return client;

                    } else {
                        return null;
                    }
                }).build();
    }

    @Bean
    public ItemProcessor<Client, ClientTestResult> processor() {
        return new ItemProcessor<Client, ClientTestResult>() {
            @Override
            public ClientTestResult process(Client client) throws Exception {
                //on effectue les tests et on retourne le resultat sous forme d'instance de
                //la classe ClientTestResult
                return(TestRegles.fireAll(client));
            }
        };
    }

    @Bean
    public ItemWriter<ClientTestResult> writer() {
        return new ItemWriter<ClientTestResult>() {
            @Override
            public void write(List<? extends ClientTestResult> testResults) throws Exception {
                // affiche le nombre de declenchement de chaque regle a la console
                System.out.println(ClientTestResult.getStatsReport());

                // TODO check if this is necessary, maybe we just have to print the general stats after each chunk
                // TODO append a la fin du fichier log.txt, et non pas overwrite
                // on commence par ne selectionner que les clients suspectés
                // testResults = testResults.stream().filter(clientTestResult -> !clientTestResult.isTestsReussis()).collect(Collectors.toList());;
                // Convertit les instances de ClientTestResult selectionné en des strings, qu'on ecrira dans un fichier "log.txt"
                // String[] resultStrings = Stream.of(testResults).map(result -> result.toString()).toArray(String[]::new);

            }
        };
    };
}



