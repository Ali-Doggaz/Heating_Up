package com.example.Balayage.batch;

import com.example.Balayage.BalayageApplication;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.Set;


@Controller
public class ScanController {


    private final ScheduledConfiguration scheduledConfiguration = new ScheduledConfiguration();

    private static BatchConfiguration batchConfiguration;

    public static void setBatchConfiguration(BatchConfiguration batchConfiguration) {
        ScanController.batchConfiguration = batchConfiguration;
    }

    @Autowired
    @Qualifier("asyncJobLauncher")
    private JobLauncher jobLauncher;

    @Autowired
    JobExplorer jobExplorer;

    @Autowired
    JobOperator jobOperator;

    @Autowired
    BatchConfigParamsService batchConfigParamsService;

    @Autowired
    private ApplicationContext context;

    @GetMapping("Scan/Start")
    public ResponseEntity<String> launchJob(){
        try {
            //TODO remove this
            if (jobExplorer.findRunningJobExecutions(BatchConfiguration.getUniqueJobName()).size() >= 1){
                return new ResponseEntity<>("Veuillez attendre la fin du balayage en cours...", HttpStatus.OK);
            }
            //TODO get new scan's params and inject them to the new job
            Job scanJob = (Job) context.getBean("ScanJob", 2);

            jobLauncher.run(scanJob, new JobParametersBuilder()
                    .addDate("date", new Date())
                    .toJobParameters());
            return new ResponseEntity<>("Succès: Le scan a commencé", HttpStatus.OK);
        }
        catch(Exception e1){
            return new ResponseEntity<>("Erreur: une erreur inattendue a eu lieu...", HttpStatus.OK);
        }
    }

    @GetMapping("Scan/Stop")
    public ResponseEntity<String> stopScanJob(){
        try {
            Set<JobExecution> jobExecutions = jobExplorer.findRunningJobExecutions(BatchConfiguration.getUniqueJobName());
            if (jobExecutions.size() == 0) return new ResponseEntity<>("Erreur: Aucun balayage n'est en cours",
                    HttpStatus.OK);
            for (JobExecution jobExecution : jobExecutions) {
                    jobOperator.stop(jobExecution.getId());
               }
            return new ResponseEntity<>("Succès: Balayage arrêté", HttpStatus.OK);
            }
        catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<>("Erreur: une erreur inattendue a eu lieu...", HttpStatus.OK);
        }
    }

    @PostMapping("/Scan/SetConfig")
    @ResponseBody
    public ResponseEntity<String> setConfig(@RequestBody BatchConfigParams batchConfigParams){
        int chunkSize = batchConfigParams.getChunkSize();
        int pageSize = batchConfigParams.getPageSize();
        int nbrClientsParRapport = batchConfigParams.getNbrClientsParRapport();
        String cronExpression = batchConfigParams.getCronExpression();
        //TODO add this check in the form
        //if(chunkSize<1 || pageSize<1) return "";
        try {
            BatchConfiguration.setChunkSize(chunkSize);
            BatchConfiguration.setPageSize(pageSize);
            BatchConfiguration.setCronExpression(cronExpression);
            BatchConfiguration.setNbrClientsParRapport(nbrClientsParRapport);
            batchConfigParamsService.updateConfig(batchConfigParams);
            scheduledConfiguration.refreshCronSchedule();
            return new ResponseEntity<>("Succès: La configuration a été modifiée avec succès", HttpStatus.OK);
        }
        catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<>("Erreur: une erreur inattendue a eu lieu...", HttpStatus.OK);
        }
    }



}
