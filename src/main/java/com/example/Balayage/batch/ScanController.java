package com.example.Balayage.batch;

import org.hibernate.engine.jdbc.batch.spi.Batch;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    public String launchJob(Model model){
        try {
            jobLauncher.run(scanJob, new JobParametersBuilder()
                    .addDate("date", new Date())
                    .toJobParameters());
            model.addAttribute("boolResult", true);
            model.addAttribute("successMessage","Scan demarré avec succès");
        }
        catch(Exception e){
            e.printStackTrace();
            //TODO modify
            model.addAttribute("boolResult", false);
            model.addAttribute("errorMessage",e.getMessage());
        }
        finally{
            return "index";
        }
    }

    @GetMapping("Scan/Status")
    @ResponseBody
    public boolean isJobRunning(){
        if (jobExplorer.findRunningJobExecutions(scanJobName).size() >= 1) return true;
        return false;
    }

    @GetMapping("Scan/Stop")
    @ResponseBody
    public boolean stopScanJob(){
        try {
            Set<JobExecution> jobExecutions = jobExplorer.findRunningJobExecutions(scanJobName);
            for (JobExecution jobExecution : jobExecutions) {
                jobOperator.stop(jobExecution.getId());
               }
            return true;
            }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @PostMapping("Scan/setConfig")
    @ResponseBody
    public boolean setConfig(@RequestParam int chunkSize, @RequestParam int pageSize){
        if(chunkSize<1 || pageSize<1) return false;
        try {
            BatchConfiguration.setChunkSize(chunkSize);
            BatchConfiguration.setPageSize(pageSize);
            return true;
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @GetMapping("/")
    public String greeting(@RequestParam(name="boolResult", required=false) Boolean boolResult,
                           @RequestParam(name="operationResult", required=false) String operationResult,
                           Model model) {
        if(boolResult == null || operationResult == null || operationResult=="") return "index";
        if(!boolResult) model.addAttribute("errorMessage",operationResult);
        else model.addAttribute("successMessage", operationResult);
        return "index";
    }


}
