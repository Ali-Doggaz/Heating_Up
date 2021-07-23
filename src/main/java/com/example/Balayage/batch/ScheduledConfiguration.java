package com.example.Balayage.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

import java.util.*;
import java.util.concurrent.ScheduledFuture;

/* Cette classe s'occupe de configurer la plannification des balayages.
*
* */
@Configuration
public class ScheduledConfiguration{

    @Autowired
    BatchConfigParamsService batchConfigParamsService;

    @Autowired
    @Qualifier("asyncJobLauncher")
    JobLauncher jobLauncher;

    @Autowired
    ThreadPoolTaskScheduler taskScheduler;

    private List<String> scheduledJobsNames = new ArrayList<>();
    private Map<BatchConfigParams, ScheduledFuture<?>> schedulesJobs = new HashMap<>();

    @Bean
    public ThreadPoolTaskScheduler taskScheduler(){
        ThreadPoolTaskScheduler threadPoolTaskScheduler =new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setThreadNamePrefix("scheduler-thread");
        threadPoolTaskScheduler.setPoolSize(1);
        threadPoolTaskScheduler.initialize();
        return threadPoolTaskScheduler;
    }

    /*Demande au scheduler de reprogrammer les balayages suivant
    * la cronExpression passée en parametre.*/
    public void scheduleScanJob(Job scanJob, BatchConfigParams batchConfigParams) {
        ScheduledFuture<?> futureJob = taskScheduler.schedule(
                new Runnable(){

                    public void run(){
                        try {
                            jobLauncher.run(scanJob, new JobParametersBuilder()
                                    .addDate("date", new Date())
                                    .toJobParameters());
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                },
               new CronTrigger(batchConfigParams.getCronExpression())
            );
        schedulesJobs.put(batchConfigParams, futureJob);
        scheduledJobsNames.add(scanJob.getName());
    }

    public void deleteScheduledJob(Long id){
        schedulesJobs.forEach((params, job) -> {
            if (params.getId() == id){
                if(job!=null){
                    job.cancel(true);
                }
                batchConfigParamsService.deleteConfigById(id);
                return;
            }
        });
    }

    public List<String> getScheduledJobsNames() {
        return scheduledJobsNames;
    }

    public void addJobName(String jobName){
        scheduledJobsNames.add(jobName);
    }
}
