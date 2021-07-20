package com.example.Balayage.batch;

import org.springframework.batch.core.JobParametersBuilder;

import java.util.Date;

//Task qui sera utilisée pour replannifier les date du balayager
public class BalayageTask implements Runnable {


    public static BatchConfiguration batchConfiguration;

    public void run() {
        try {
            //TODO delete random values (1000 and * * 8 * *) added
            //Crée un nouveau reader avec la nouvelle configuration
            batchConfiguration.setClientReader(batchConfiguration.reader(1000));
            batchConfiguration.setClientProcessingWriter(batchConfiguration.writer(1000, 1000));
            //Programme le nouveau Job
            batchConfiguration.getJobLauncher().run(batchConfiguration.ScanJob(1000, 1000, 1000), new JobParametersBuilder()
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
