package com.example.Balayage.batch;

import com.example.Balayage.client.Client;
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
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;


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
    private ItemWriter<String> clientProcessingWriter;

    @Autowired
    private ItemProcessor<Client, String> clientProcessor;

    //@Autowired
    //private DataSource dataSource;

    @Bean
    public Job MyJob() {
        Step step = stepBuilderFactory.get("Traitement-donnees-client")
                .<Client, String>chunk(1000)
                .reader(clientReader)
                .writer(clientProcessingWriter)
                .processor(clientProcessor)
                .listener(myjoblistener())
                .allowStartIfComplete(true)
                .build();
        return jobBuilderFactory.get("Scan_Clients").start(step).build();
    }

    @Bean
    public JobExecutionListener myjoblistener() {

        JobExecutionListener listener = new JobExecutionListener() {

            @Override
            public void beforeJob(JobExecution jobExecution) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterJob(JobExecution jobExecution) {

                try {
                    Files.write(Paths.get(""), Arrays.asList("foo"),
                            StandardOpenOption.APPEND);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                System.out.println("Job has been completed");
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

                        LOGGER.info("RowMapper record : {}", client);
                        return client;
                    } else {
                        LOGGER.info("Returning null from rowMapper");
                        return null;
                    }
                }).build();
    }

    @Bean
    public ItemProcessor<Client, String> processor() {
        return new ItemProcessor<Client, String>() {
            @Override
            public String process(Client item) throws Exception {
                System.out.println("Client " + item.getId() + " Processed");
                return "Client " + item.getId() + " Processed"; //TODO Change this, adapt it to business rules scan
            }
        };
    }

    @Bean
    public ItemWriter<String> writer() {
        return new ItemWriter<String>() {
            @Override
            public void write(List<? extends String> items) throws Exception {

                BufferedWriter outputWriter = null;
                outputWriter = new BufferedWriter(new FileWriter("D:\\MyDesktop\\Algebre\\Vneuron-Balayage_Regles_Metier\\src\\main\\java\\com\\example\\Balayage\\batch\\report"));
                for (int i = 0; i < items.size(); i++) {
                    // Maybe:
                    outputWriter.write(items.get(i)+"");
                    outputWriter.newLine();
                }
                outputWriter.flush();
                outputWriter.close();
            }
        };
    };
}


