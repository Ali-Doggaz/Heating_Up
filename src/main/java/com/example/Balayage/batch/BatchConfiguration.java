
package com.example.Balayage.batch;

import com.example.Balayage.client.Client;
import com.example.Balayage.regles.ClientTestResult;
import com.example.Balayage.regles.TestRegles;
import com.example.Balayage.report.ScanReportGenerator;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    private TestRegles testRegles;

    private static ArrayList<ClientTestResult> clientSuspects;

    @Autowired
    private DataSource dataSource;

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
                clientSuspects = new ArrayList<>();
            }

            @Override
            public void afterJob(JobExecution jobExecution) {

                System.out.println("Job has been completed, generating report");
                //TODO Generate Jasper Report
                try {
                    ScanReportGenerator scanReportGenerator = new ScanReportGenerator();
                    scanReportGenerator.generateReport(clientSuspects);
                }
                catch(IOException e){
                    e.printStackTrace();
                }
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
                return(testRegles.fireAll(client));
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


                // ajoute les nouveaux clients suspectés a la liste
                testResults = testResults.stream().filter(clientTestResult -> !clientTestResult.isTestsReussis()).collect(Collectors.toList());
                clientSuspects.addAll(testResults);

            }
        };
    };
}



