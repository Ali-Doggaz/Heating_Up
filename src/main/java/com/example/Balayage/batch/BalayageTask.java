package com.example.Balayage.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.dao.MapExecutionContextDao;
import org.springframework.batch.core.repository.dao.MapJobExecutionDao;
import org.springframework.batch.core.repository.dao.MapJobInstanceDao;
import org.springframework.batch.core.repository.dao.MapStepExecutionDao;
import org.springframework.batch.core.repository.support.SimpleJobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import java.util.Date;

//Task qui sera utilisée pour replannifier les date du balayager
public class BalayageTask implements Runnable {


    public static BatchConfiguration batchConfiguration;

    @Autowired
    Job scanJob;

    public void run() {
        try {
            batchConfiguration.getJobLauncher().run(batchConfiguration.ScanJob(), new JobParametersBuilder()
                    .addDate("date", new Date())
                    .toJobParameters());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setBatchConfiguration(BatchConfiguration batchConfiguration) {
        BalayageTask.batchConfiguration = batchConfiguration;
    }
}
