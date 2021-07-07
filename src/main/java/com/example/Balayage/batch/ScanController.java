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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.Date;
import java.util.Set;


@Controller
public class ScanController {

    private final String scanJobName = "scanJob";

    @Autowired
    Job scanJob;

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    JobExplorer jobExplorer;

    @Autowired
    JobOperator jobOperator;

    @GetMapping("Scan/Start")
    public boolean launchJob(){
        try {
            jobLauncher.run(scanJob, new JobParametersBuilder()
                    .addDate("date", new Date())
                    .toJobParameters());
            return true;
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @GetMapping("Scan/Status")
    public boolean isJobRunning(){
        if (jobExplorer.findRunningJobExecutions("Scan_Clients").size() >= 1) return true;
        return false;
    }

    @GetMapping("Scan/Stop")
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

    @PutMapping("Scan/setConfig")
    public boolean setConfig(@RequestParam int chunkSize,@RequestParam int pageSize){
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


}
