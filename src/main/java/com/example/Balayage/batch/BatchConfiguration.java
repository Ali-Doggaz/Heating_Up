
package com.example.Balayage.batch;

import com.example.Balayage.client.Client;
import com.example.Balayage.regles.ClientTestResult;
import com.example.Balayage.regles.StatsRegle;
import com.example.Balayage.regles.TestRegles;
import com.example.Balayage.report.ScanReportGenerator;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
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

import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Créer la configuration initiale de Spring Batch en créant le Job de scan
 * (reader, processor, writer, listener)
 */
@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    private TestRegles testRegles;

    private static ArrayList<ClientTestResult> clientSuspects;
    private static ArrayList<StatsRegle> statsRegles;

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
            /**
             * S'occupe de l'initialisation du job de scan
             * 1. Lecture des règles metiers depuis le fichier texte specifié (voir classe TestRegles)
             * 2. Initialiser le nombre de declenchement de chaque règle à 0
             * 3. Initialiser les stats (nbr total de clients testés, nbr de clients suspects identifiés, etc...) à 0
             */
            @Override
            public void beforeJob(JobExecution jobExecution) {
                System.out.println("Initialisation du scan...");
                try {
                    testRegles.readRulesFromFile();
                }
                catch(IOException e){
                    final JobOperator jobOperator = BatchRuntime.getJobOperator();
                    jobOperator.stop(jobExecution.getId());
                    System.out.println("Le fichier contenant les règles metiers est introuvable...");
                }
                //Initialise le nombre de declenchement de chaque regle à 0
                Map<Integer, Integer> nbrDeclenchementRegles = new HashMap<>();
                for (int i = 1; i <= testRegles.getRegles().length; i++) {
                    nbrDeclenchementRegles.put(i, 0);
                }
                ClientTestResult.setNbrDeclenchementRegles(nbrDeclenchementRegles);
                //Initialise le reste des variables statiques à 0
                ClientTestResult.setNbrSuspectsDetectes(0);
                ClientTestResult.setNbrClientsTestes(0);
                //Initialise le nombre de suspects detectés a 0
                clientSuspects = new ArrayList<>();
            }

            /**
             * S'occupe de l'initialisation du job de scan
             * 1. Lecture des règles metiers depuis le fichier texte specifié (voir classe TestRegles)
             * 2. Initialiser le nombre de declenchement de chaque règle à 0
             * 3. Initialiser les stats (nbr total de clients testés, nbr de clients suspects identifiés, etc...) à 0
             */

            @Override
            public void afterJob(JobExecution jobExecution) {

                System.out.println("Job has been completed, generating report");
                //On genere la collection "statsRegles" qui contient les statistiques de toutes les regles
                //Et qui sera utilisée pour la generation du rapport (JasperReport)
                statsRegles = new ArrayList<StatsRegle>();
                for (Map.Entry<Integer, Integer> statRegle : ClientTestResult.getNbrDeclenchementRegles().entrySet()) {
                    statsRegles.add(new StatsRegle(statRegle.getKey(), statRegle.getValue()));
                }
                Collections.sort(statsRegles);
                Collections.reverse(statsRegles);
                //TODO Generate Jasper Report
                try {
                    ScanReportGenerator scanReportGenerator = new ScanReportGenerator();
                    scanReportGenerator.generateReport(clientSuspects, statsRegles);
                }
                catch(IOException e){
                    e.printStackTrace();
                }
            }
        };

        return listener ;
    }

    /**
     * Récupére les clients un par un depuis la base de données
     */
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


    /**
     * Declenche la méthode "fireAll(Client c)" qui va tester toutes les
     * règles métiers sur le client à traiter, et retourne une instance de ClientTestResult
     * qui contient les résultat de ces tests
     */
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

    /**
     * Tous les 1000(chunk_size) clients traités, ce writer va s'occuper de
     * 1. Generer un rapport sommaire des tests efféctués
     * 2. Ajouter les nouveaux clients identifiés comme "suspects" à la liste "clientSuspects"
     */
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



