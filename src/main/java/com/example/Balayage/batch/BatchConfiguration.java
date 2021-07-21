
package com.example.Balayage.batch;

import com.example.Balayage.client.Client;
import com.example.Balayage.client.ClientService;
import com.example.Balayage.regles.TestRegles;
import com.example.Balayage.regles.clientsTestResults.ClientTestResult;
import com.example.Balayage.regles.clientsTestResults.ClientTestResultService;
import com.example.Balayage.regles.statsExceptions.StatsException;
import com.example.Balayage.regles.statsExceptions.StatsExceptionService;
import com.example.Balayage.regles.statsRegles.StatsRegle;
import com.example.Balayage.regles.statsRegles.StatsRegleService;
import com.example.Balayage.report.ScanReportGenerator;
import org.springframework.batch.core.*;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.persistence.EntityManagerFactory;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Créer la configuration initiale de Spring Batch en créant le Job de scan
 * (reader, processor, writer, listener)
 */
@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    private ClientTestResultService clientTestResultService;

    @Autowired
    private TestRegles testRegles;
    //Nombre de règle à tester:
    int rulesNumber;


    @Autowired
    private ApplicationContext context;

    @Autowired
    ScheduledConfiguration scheduledConfiguration;

    //initialisation de la configuration des balayages depuis la BD
    @Bean
    CommandLineRunner commandLineRunner() {
        return args -> {
            List<BatchConfigParams> batchConfigsParams = batchConfigParamsService.getConfig();
            int chunkSize = 0; int pageSize = 0; String cronExpression = ""; int nbrClientsParRapport = 0;
            for(BatchConfigParams batchConfigParams : batchConfigsParams) {
                chunkSize = batchConfigParams.getChunkSize();
                pageSize = batchConfigParams.getPageSize();
                cronExpression = batchConfigParams.getCronExpression();
                nbrClientsParRapport = batchConfigParams.getNbrClientsParRapport();
                //TODO check if scheduling works
                scheduledConfiguration.scheduleScanJob(
                        (Job) context.getBean("ScanJob", chunkSize, pageSize, nbrClientsParRapport),
                        cronExpression);
                System.out.println("Configuration initialisee...");
                System.out.println("Chunksize: " + chunkSize + " , Pagesize= " + pageSize + " , nbr_clients_par_rapport= " +
                        nbrClientsParRapport + ", cronExpression= " + cronExpression);
            }
        };
    }

    private static String  uniqueJobName;

    @Autowired
    private ClientService clientService;

    @Autowired
    private StatsRegleService statsRegleService;

    @Autowired
    private StatsExceptionService statsExceptionService;

    @Autowired
    private JobExplorer jobExplorer;

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    JobRegistry jobRegistry;


    @Autowired
    BatchConfigParamsService batchConfigParamsService;

    @Autowired
    Job scanJob;

    //utilisé pour les websockets
    private final SimpMessagingTemplate template ;

    @Autowired
    BatchConfiguration(SimpMessagingTemplate template){
        this.template = template;
        BalayageTask.setBatchConfiguration(this);

    }

    //on a besoin d'une bean de type integer pour initialiser la bean "ScanJob" lors de l'initialisation de l'application.
    //lors de la création des scanJob (scope.Prototype), on créera de nouvelle bean "Job" avec
    //de vrais parametres au lieu de cette bean int.
    @Bean
    Integer initInt(){
        return 1;
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Job ScanJob(Integer chunkSize, Integer pageSize, Integer nbrClientsParRapport) {
        System.out.println("chunkSize = " + chunkSize);
        JobExecutionListener listener = myjoblistener(nbrClientsParRapport);
        ItemProcessor<Client, ClientTestResult> clientProcessor = processor(nbrClientsParRapport);
        ItemWriter<ClientTestResult> clientProcessingWriter = writer(chunkSize, nbrClientsParRapport);
        ItemReader<Client> clientReader = reader(pageSize);

        Step step = stepBuilderFactory.get("Traitement-donnees-client")
                .<Client, ClientTestResult>chunk(chunkSize)
                .reader(clientReader)
                .writer(clientProcessingWriter)
                .processor(clientProcessor)
                .allowStartIfComplete(true)
                .throttleLimit(1)
                .build();
        //Genere un Job avec un nom unique
        uniqueJobName = "Scan_Clients"+UUID.randomUUID().toString();
        return jobBuilderFactory.get(uniqueJobName)
                .start(step)
                .listener(listener)
                .build();
    }


    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public JobExecutionListener myjoblistener(Integer nbrClientsParRapport) {
        //TODO delete this
        System.out.println("Listener - nbrClientParRapport = " + nbrClientsParRapport);

        return new JobExecutionListener() {
            /**
             * S'occupe de l'initialisation du job de scan
             * 1. Lecture des règles metiers depuis le fichier texte specifié (voir classe TestRegles)
             * 2. Initialiser le nombre de declenchement de chaque règle à 0
             * 3. Initialiser les stats (nbr total de clients testés, nbr de clients suspects identifiés, etc...) à 0
             */
            @Override
            public void beforeJob(JobExecution jobExecution) {


                // Si un scan est deja en cours, annule le declenchement du nouveau Job en levant une exception
                int runningJobsCount = jobExplorer.findRunningJobExecutions(jobExecution.getJobInstance().getJobName()).size();
                if(runningJobsCount > 1){
                    throw new RuntimeException("Veuillez attendre la fin du balayage en cours");
                }


                System.out.println("Initialisation du scan...");
                try {
                    rulesNumber = testRegles.readRulesFromFile();
                }
                catch(IOException e){
                    JobOperator jobOperator = BatchRuntime.getJobOperator();
                    jobOperator.stop(jobExecution.getId());
                    System.out.println("Le fichier contenant les règles metiers est introuvable...");
                }

                int nbrOfBatches = (int) Math.ceil(clientService.getNumberOfClients()/(float) nbrClientsParRapport);
                Long jobExecutionId = jobExecution.getId();
                for(int batch=0;batch<nbrOfBatches;batch++){
                    for(int ruleNumber=1; ruleNumber<=rulesNumber;ruleNumber++)
                    statsRegleService.initRow(jobExecutionId, batch, ruleNumber);
                }


                //Envoyer une socket a l'UI pour l'informer que le job a demarré
                String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
                template.convertAndSend("/JobStatus", "Status: Balayage en cours - Heure de démarrage du balayge: " + timeStamp);
            }


            @Override
            public void afterJob(JobExecution jobExecution) {
                //Envoyer une socket a l'UI pour l'informer que le job est terminé
                String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
                template.convertAndSend("/JobStatus", "Status: Balayage terminé - Etat de sortie: " + jobExecution.getExitStatus().getExitCode() +" - Heure: " + timeStamp);
            }
        };
    }

    /**
     * Récupére les clients un par un depuis la base de données
     */
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ItemReader<Client> reader(Integer pageSize) {
        //TODO delete this
        System.out.println("Reader - pagesize = " + pageSize);

        String Query = "FROM client ORDER BY id";
        return new JpaPagingItemReaderBuilder<Client>().name("scan-reader")
                .queryString(Query)
                .entityManagerFactory(entityManagerFactory)
                .pageSize(pageSize)
                .build();
    }

    /**
     * Declenche la méthode "fireAll(Client c)" qui va tester toutes les
     * règles métiers sur le client à traiter, et retourne une instance de ClientTestResult
     * qui contient les résultat de ces tests
     */
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ItemProcessor<Client, ClientTestResult> processor(Integer nbrClientsParRapport) {
        //TODO delete this
        System.out.println("Processor - nbrClientsParRapport = " + nbrClientsParRapport);

        return new ItemProcessor<Client, ClientTestResult>() {
            private Long jobExecutionID;
            private StepExecution stepExecution;

            @BeforeStep
            public void saveStepExecution(StepExecution stepExecution) {
                jobExecutionID = stepExecution.getJobExecutionId();
                this.stepExecution = stepExecution;
            }
            @Override
            public ClientTestResult process(Client client) throws Exception {
                //on effectue les tests et on retourne le resultat sous forme d'instance de
                //la classe ClientTestResult
                int batchNumber = stepExecution.getWriteCount() / nbrClientsParRapport;
                return(testRegles.fireAll(client, jobExecutionID, batchNumber));
            }
        };
    }

    /**
     * Tous les (int)chunk_size clients traités, ce writer va s'occuper de generer
     * un rapport sommaire des tests efféctués
     */
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ItemWriter<ClientTestResult> writer(int chunkSize, int nbrClientsParRapport) {
        return new ItemWriter<ClientTestResult>() {

            private StepExecution stepExecution;
            private Long totalNbrClientsAnalyzed = Long.valueOf(0);

            @BeforeStep
            public void saveStepExecution(StepExecution stepExecution) {
                this.stepExecution = stepExecution;
            }

            @AfterStep
            public void writeLastClients() {
                //Si certains clients n'ont pas encore été écrit dans un rapport
                if (stepExecution.getWriteCount() % nbrClientsParRapport != 0) {
                    generateReport(0);
                }
                //reinitalize number of analyzed customers
                totalNbrClientsAnalyzed = Long.valueOf(0);
            }

            @Override
            public void write(List<? extends ClientTestResult> testResults) throws Exception {
                //Update la DB avec les nouveaux clientTestResults
                clientTestResultService.addAll(testResults);

                //Si on a traité "nbrClientsParRapport"(int) nouveaux clients, on genere un rapport
                if ((stepExecution.getWriteCount() + chunkSize) % nbrClientsParRapport < chunkSize) {
                    generateReport(chunkSize);
                }
            }

            public void generateReport(int numberNewProcessedClient){
                try {
                    ScanReportGenerator scanReportGenerator = new ScanReportGenerator();
                    int batchNumber = stepExecution.getWriteCount() / nbrClientsParRapport;
                    ArrayList<StatsRegle> statsRegles = statsRegleService.getBatchStats(stepExecution.getJobExecutionId(), batchNumber);
                    Collections.sort(statsRegles);
                    ArrayList<StatsException> statsExceptions = statsExceptionService.getBatchStats(stepExecution.getJobExecutionId(), batchNumber);
                    Long nbrClientsAnalysed = (stepExecution.getWriteCount()+numberNewProcessedClient) - totalNbrClientsAnalyzed;
                    totalNbrClientsAnalyzed = Long.valueOf(stepExecution.getWriteCount()+chunkSize);
                    scanReportGenerator.generateReport(statsRegles, statsExceptions, batchNumber, nbrClientsAnalysed);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };
    }

    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor() {
        JobRegistryBeanPostProcessor postProcessor = new JobRegistryBeanPostProcessor();
        postProcessor.setJobRegistry(jobRegistry);
        return postProcessor;
    }

    @Bean(name = "asyncJobLauncher")
    public JobLauncher simpleJobLauncher(JobRepository jobRepository) throws Exception {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }

    public JobLauncher getJobLauncher() {
        return jobLauncher;
    }


    public static String getUniqueJobName() {
        return uniqueJobName;
    }
}



