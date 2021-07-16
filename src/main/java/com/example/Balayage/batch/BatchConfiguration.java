
package com.example.Balayage.batch;

import com.example.Balayage.client.Client;
import com.example.Balayage.regles.clientsTestResults.ClientTestResult;
import com.example.Balayage.regles.statsExceptions.StatsException;
import com.example.Balayage.regles.statsRegles.StatsRegle;
import com.example.Balayage.regles.TestRegles;
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
    //Cette configuration sera modifiée quelques secondes après le lancement du programme
    //grace au commandLineRunner (voir ci-dessous)
    // La configuration stockée dans la table "batch_config_parameters" sera alors utilisée.
    private static int chunkSize=1000;
    private static int pageSize=1000;
    private static int nbrClientsParRapport=2000;
    private static String cronExpression="* 10 2 2 2 2 ";
    //initialisation de la configuration des balayages depuis la BD
    @Bean
    CommandLineRunner commandLineRunner() {
        return args -> {
            BatchConfigParams batchConfigParams = batchConfigParamsService.getConfig();
            chunkSize = batchConfigParams.getChunkSize();
            pageSize = batchConfigParams.getPageSize();
            cronExpression = batchConfigParams.getCronExpression();
            System.out.println("Configuration initialisee...");
            System.out.println("Chunksize: " + chunkSize+" , Pagesize= "+pageSize+" , cronExpression= "+cronExpression);
        };
    }

    private static String  uniqueJobName;

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
    private ItemReader<Client> clientReader;

    @Autowired
    JobRegistry jobRegistry;

    @Autowired
    private ItemWriter<ClientTestResult> clientProcessingWriter;

    @Autowired
    private ItemProcessor<Client, ClientTestResult> clientProcessor;

    @Autowired
    BatchConfigParamsService batchConfigParamsService;

    @Autowired
    Job scanJob;

    //utilisé pour les websockets
    private final SimpMessagingTemplate template;

    @Autowired
    BatchConfiguration(SimpMessagingTemplate template){
        this.template = template;
        BalayageTask.setBatchConfiguration(this);
        ScanController.setBatchConfiguration(this);
    }


    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Job ScanJob() {
        JobExecutionListener listener = myjoblistener();
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
                // Si un scan est deja en cours, annule le declenchement du nouveau Job en levant une exception
                int runningJobsCount = jobExplorer.findRunningJobExecutions(jobExecution.getJobInstance().getJobName()).size();
                if(runningJobsCount > 1){
                    //TODO remove comment
                    //throw new RuntimeException("Veuillez attendre la fin du balayage en cours");
                }


                System.out.println("Initialisation du scan...");
                try {
                    testRegles.readRulesFromFile();
                }
                catch(IOException e){
                    JobOperator jobOperator = BatchRuntime.getJobOperator();
                    jobOperator.stop(jobExecution.getId());
                    System.out.println("Le fichier contenant les règles metiers est introuvable...");
                }

                //Initialise le nombre de declenchement de chaque regle à 0
                Map<Integer, Integer> nbrDeclenchementRegles = new HashMap<>();
                Map<Integer, Integer> nbrDeclenchementExceptionsRegle = new HashMap<>();
                for (int i = 1; i <= testRegles.getRegles().length; i++) {
                    nbrDeclenchementRegles.put(i, 0);
                    nbrDeclenchementExceptionsRegle.put(i, 0);
                }
                ClientTestResult.setNbrDeclenchementRegles(nbrDeclenchementRegles);
                ClientTestResult.setNbrExceptionsRegles(nbrDeclenchementExceptionsRegle);
                //Initialise le reste des variables statiques à 0
                ClientTestResult.setNbrSuspectsDetectes(0);
                ClientTestResult.setNbrClientsTestes(0);
                //Initialise le nombre de suspects detectés a 0
                TestRegles.setStatsExceptions(new ArrayList<StatsException>());

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

        return listener ;
    }

    /**
     * Récupére les clients un par un depuis la base de données
     */
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ItemReader<Client> reader() {
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
     * Tous les (int)chunk_size clients traités, ce writer va s'occuper de generer
     * un rapport sommaire des tests efféctués
     */
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ItemWriter<ClientTestResult> writer() {
        return new ItemWriter<ClientTestResult>() {

            private StepExecution stepExecution;

            @BeforeStep
            public void saveStepExecution(StepExecution stepExecution) {
                this.stepExecution = stepExecution;
                //initaliser les collections
                clientSuspects = new ArrayList<>();
                statsRegles = new ArrayList<StatsRegle>();
            }

            @AfterStep
            public void writeLastClients() {
                //Si certains clients n'ont pas encore été écrit dans un rapport
                if (stepExecution.getWriteCount() % nbrClientsParRapport != 0) {
                    generateReport();
                }
            }

            @Override
            public void write(List<? extends ClientTestResult> testResults) throws Exception {
                //Ajouter les nouveaux clients suspects detectés
                testResults = testResults.stream().filter(client -> !client.isTestsReussis()).collect(Collectors.toList());
                clientSuspects.addAll(testResults);
                //Si on a traité "nbrClientsParRapport"(int) nouveaux clients, on genere un rapport
                if ((stepExecution.getWriteCount() + chunkSize) % nbrClientsParRapport < chunkSize) {
                    generateReport();
                }
            }

            public void generateReport(){
                //On genere la collection "statsRegles" qui contient les statistiques de toutes les regles
                //Et qui sera utilisée pour la generation du rapport (JasperReport)
                for (Map.Entry<Integer, Integer> statRegle : ClientTestResult.getNbrDeclenchementRegles().entrySet()) {
                    int numRegle = statRegle.getKey();
                    statsRegles.add(new StatsRegle(
                                    numRegle,
                                    statRegle.getValue(),
                                    ClientTestResult.getNbrExceptionsRegles().get(numRegle)
                            )
                    );
                }
                Collections.sort(statsRegles);
                try {
                    ScanReportGenerator scanReportGenerator = new ScanReportGenerator();
                    int batchNumber = stepExecution.getWriteCount() / nbrClientsParRapport;
                    scanReportGenerator.generateReport(clientSuspects, statsRegles, TestRegles.getStatsExceptions(), batchNumber);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //Reinitialise tous les parametres pour generer le rapport du prochain batch
                //Reinitialise le nombre de declenchement de chaque regle à 0, ainsi que le
                //nombre d'exception generé par chaque règle à 0
                Map<Integer, Integer> nbrDeclenchementRegles = new HashMap<>();
                Map<Integer, Integer> nbrDeclenchementExceptionsRegle = new HashMap<>();
                for (int i = 1; i <= testRegles.getRegles().length; i++) {
                    nbrDeclenchementRegles.put(i, 0);
                    nbrDeclenchementExceptionsRegle.put(i, 0);
                }
                ClientTestResult.setNbrDeclenchementRegles(nbrDeclenchementRegles);
                ClientTestResult.setNbrExceptionsRegles(nbrDeclenchementExceptionsRegle);

                //Initialise le reste des variables statiques à 0
                ClientTestResult.setNbrSuspectsDetectes(0);
                ClientTestResult.setNbrClientsTestes(0);
                TestRegles.setStatsExceptions(new ArrayList<StatsException>());
                clientSuspects = new ArrayList<>();
                statsRegles = new ArrayList<StatsRegle>();
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

    public static int getChunkSize() {
        return chunkSize;
    }

    public static void setChunkSize(int chunkSize) {
        BatchConfiguration.chunkSize = chunkSize;
    }

    public static int getPageSize() {
        return pageSize;
    }

    public static void setPageSize(int pageSize) {
        BatchConfiguration.pageSize = pageSize;
    }

    public static int getNbrClientsParRapport() {
        return nbrClientsParRapport;
    }

    public static void setNbrClientsParRapport(int nbrClientsParRapport) {
        BatchConfiguration.nbrClientsParRapport = nbrClientsParRapport;
    }

    public static String getCronExpression() {
        return cronExpression;
    }

    public static void setCronExpression(String cronExpression) {
        BatchConfiguration.cronExpression = cronExpression;
    }

    public JobLauncher getJobLauncher() {
        return jobLauncher;
    }

    public ItemReader<Client> getClientReader() {
        return clientReader;
    }

    public void setClientReader(ItemReader<Client> clientReader) {
        this.clientReader = clientReader;
    }

    public ItemWriter<ClientTestResult> getClientProcessingWriter() {
        return clientProcessingWriter;
    }

    public void setClientProcessingWriter(ItemWriter<ClientTestResult> clientProcessingWriter) {
        this.clientProcessingWriter = clientProcessingWriter;
    }

    public ItemProcessor<Client, ClientTestResult> getClientProcessor() {
        return clientProcessor;
    }

    public void setClientProcessor(ItemProcessor<Client, ClientTestResult> clientProcessor) {
        this.clientProcessor = clientProcessor;
    }

    public static String getUniqueJobName() {
        return uniqueJobName;
    }
}



