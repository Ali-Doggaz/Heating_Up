package com.ram.config;

import com.example.Balayage.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.ResultSet;
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
                .build();
        return jobBuilderFactory.get("Scan_Clients").start(step).build();
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
        System.out.println("Inside processor method");
        return new RecordProcessor();
    }

    @Bean
    public ItemWriter<String> writer() {
        return new ItemWriter<String>() {
            @Override
            public void write(List<? extends String> items) throws Exception {
                System.out.println("Result of chunk: ");
                for (String str : items){
                    System.out.println(str);
                }
            }
        };
    }
}

/*    *//**
     * The reader() method is used to read the data from the CSV file
     *//*
    @Bean
    public FlatFileItemReader<Client> reader()
    {
        System.out.println("-----------Inside reader() method--------");
        FlatFileItemReader<Client> reader = new FlatFileItemReader<Client>();
        reader.setResource();//TODO COMPLETE);
        reader.setLineMapper(new DefaultLineMapper<Client>()
        {
            {
                setLineTokenizer(new DelimitedLineTokenizer()
                {
                    {
                        setNames(new String[] { "name", "age", "salary" });
                    }
                });
                setFieldSetMapper(new BeanWrapperFieldSetMapper<Client>()
                {
                    {
                        setTargetType(Client.class);
                    }
                });
            }
        });
        return reader;
    }

    *//**
     * Intermediate processor to do the operations after the reading the data from the CSV file and
     * before writing the data into SQL.
     *//*
    @Bean
    public ClientItemProcessor processor()
    {
        System.out.println("-----------Inside  processor() method--------");
        return new ClientItemProcessor();
    }

    *//**
     * The writer() method is used to write a data into the SQL.
     *//*
    @Bean
    public JdbcBatchItemWriter<Client> writer()
    {
        System.out.println("-----------Inside writer() method--------");
        JdbcBatchItemWriter<Client> writer = new JdbcBatchItemWriter<Client>();
        writer.setItemSqlParameterSourceProvider(
                new BeanPropertyItemSqlParameterSourceProvider<Client>());
        writer.setSql("INSERT INTO Employee (NAME, AGE, SALARY) VALUES (:name, :age, :salary)");
        writer.setDataSource(dataSource);
        return writer;
    }

