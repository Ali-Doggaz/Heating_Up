package com.example.Balayage.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.Date;


@Controller
public class ScanController {

    @Autowired
    Job scanJob;

    @Autowired
    JobLauncher jobLauncher;

    @GetMapping("/startScan")
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


}
