package com.example.Balayage.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Set;


@Controller
public class ScanController {

    private final String scanJobName = "Scan_Clients";

    @Autowired
    Job scanJob;

    @Autowired
    @Qualifier("asyncJobLauncher")
    private JobLauncher jobLauncher;

    @Autowired
    JobExplorer jobExplorer;

    @Autowired
    JobOperator jobOperator;

    @GetMapping("Scan/Start")
    public ResponseEntity<String> launchJob(){
        try {
            if (jobExplorer.findRunningJobExecutions(scanJobName).size() >= 1){
                return new ResponseEntity<>("Veuillez attendre la fin du balayage en cours...", HttpStatus.OK);
            }
            jobLauncher.run(scanJob, new JobParametersBuilder()
                    .addDate("date", new Date())
                    .toJobParameters());
            return new ResponseEntity<>("Succès: Le scan a commencé", HttpStatus.OK);
        }
        catch(Exception e1){
            return new ResponseEntity<>("Erreur: une erreur inattendue a eu lieu...", HttpStatus.OK);
        }
    }

    @GetMapping("Scan/Status")
    public boolean isJobRunning(){
        if (jobExplorer.findRunningJobExecutions(scanJobName).size() >= 1) return true;
        return false;
    }

    @GetMapping("Scan/Stop")
    public ResponseEntity<String> stopScanJob(){
        try {
            Set<JobExecution> jobExecutions = jobExplorer.findRunningJobExecutions(scanJobName);
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
        String cronExpression = batchConfigParams.getCronExpression();
        //TODO add this check in the form
        //if(chunkSize<1 || pageSize<1) return "";
        try {
            BatchConfiguration.setChunkSize(chunkSize);
            BatchConfiguration.setPageSize(pageSize);
            BatchConfiguration.setCronExpression(cronExpression);
            //TODO trigger rescheduling
            return new ResponseEntity<>("Succès: La configuration a été modifiée avec succès", HttpStatus.OK);
        }
        catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<>("Erreur: une erreur inattendue a eu lieu...", HttpStatus.OK);
        }
    }



}
