package com.example.Balayage.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Controller
public class ScanController {

    @Autowired
    private ScheduledConfiguration scheduledConfiguration;

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

    @PostMapping("Scan/Start")
    @ResponseBody
    public ResponseEntity<String> launchJob(@RequestBody BatchConfigParams tempConfig){
        try {

            for(String jobName: scheduledConfiguration.getScheduledJobsNames()) {
                if (jobExplorer.findRunningJobExecutions(jobName).size() >= 1) {
                    return new ResponseEntity<>("Veuillez attendre la fin du balayage en cours...", HttpStatus.OK);
                }
            }
            //TODO get new scan's params and inject them to the new job
            Job scanJob = (Job) context.getBean("ScanJob", tempConfig.getChunkSize(),
                    tempConfig.getPageSize(), tempConfig.getNbrClientsParRapport());

            jobLauncher.run(scanJob, new JobParametersBuilder()
                    .addDate("date", new Date())
                    .toJobParameters());

            //Enregistrer le nom du nouveau job pour pouvoir l'arreter si besoin (via la route "Scan/Stop")
            scheduledConfiguration.addJobName(scanJob.getName());

            return new ResponseEntity<>("Succès: Le scan a commencé", HttpStatus.OK);
        }
        catch(Exception e1){
            return new ResponseEntity<>("Erreur: une erreur inattendue a eu lieu...", HttpStatus.OK);
        }
    }

    @GetMapping("Scan/Stop")
    public ResponseEntity<String> stopScanJob(){
            //TODO check if this works
            Boolean boolScanTrouves = false;

            Set<JobExecution> newJobExecutions = new HashSet<>();
            //Parcourir tous les jobs programmés et verifier si ils sont en cours d'execution
            for(String jobName: scheduledConfiguration.getScheduledJobsNames()) {
                newJobExecutions = jobExplorer.findRunningJobExecutions(jobName);

                //Si on trouve des balayages en cours d'execution, on update la variable booléene
                //et on arrete tous les jobs trouvés
                if (newJobExecutions.size()>0) {
                    boolScanTrouves = true;
                    for (JobExecution jobExecution : newJobExecutions) {
                        try {
                            jobOperator.stop(jobExecution.getId());
                        }
                        catch (JobExecutionNotRunningException e1){
                            e1.printStackTrace();
                            return new ResponseEntity<>("Erreur: Le balayage a deja ete complete",
                                    HttpStatus.OK);
                        }
                        catch (Exception e2){
                            e2.printStackTrace();
                        }
                    }
                }
            }

            if (!boolScanTrouves) return new ResponseEntity<>("Erreur: Aucun balayage n'est en cours",
                    HttpStatus.OK);

            return new ResponseEntity<>("Succès: Balayage en cours d'arret", HttpStatus.OK);
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
            //add config to db
            batchConfigParamsService.addConfig(batchConfigParams);
            //schedule the new scanJob
            Job scanJob = (Job) context.getBean("ScanJob", chunkSize, pageSize, nbrClientsParRapport);
            scheduledConfiguration.scheduleScanJob(scanJob, batchConfigParams);
            return new ResponseEntity<>("Succès: La configuration a été ajoutée avec succès", HttpStatus.OK);
        }
        catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<>("Erreur: une erreur inattendue a eu lieu...", HttpStatus.OK);
        }
    }

    @GetMapping("Scan/getConfigs")
    @ResponseBody
    public List<BatchConfigParams> getConfigs(){
            return batchConfigParamsService.getConfigs();
    }

    @PostMapping("Scan/deleteConfig")
    @ResponseBody
    public ResponseEntity<String> deleteConfig(@RequestBody Long id) {
        try{
            scheduledConfiguration.deleteScheduledJob(id);
            return new ResponseEntity<>("Succès: Configuration supprimée avec succès", HttpStatus.OK);
        }
        catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>("Erreur: une erreur inattendue a eu lieu...", HttpStatus.OK);
        }
    }

}
