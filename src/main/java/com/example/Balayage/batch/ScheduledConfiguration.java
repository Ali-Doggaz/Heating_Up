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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

/* Cette classe s'occupe de configurer la plannification des balayages.
*
* */
@Configuration
public class ScheduledConfiguration implements SchedulingConfigurer {

    @Autowired
    @Qualifier("asyncJobLauncher")
    JobLauncher jobLauncher;

    @Autowired
    ThreadPoolTaskScheduler taskScheduler;

    //@Autowired
    //private ScheduledTaskRegistrar scheduledTaskRegistrar;

    private List<ScheduledFuture<?>> scheduledJobs = new ArrayList<>();
    private List<String> scheduledJobsNames = new ArrayList<>();


    /*@Bean
    public ScheduledTaskRegistrar ScheduledTaskRegistrar() {
        ScheduledTaskRegistrar scheduledTaskRegistrar = new ScheduledTaskRegistrar();
        return scheduledTaskRegistrar;
    }*/

    @Bean
    public ThreadPoolTaskScheduler taskScheduler(){
        ThreadPoolTaskScheduler threadPoolTaskScheduler =new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setThreadNamePrefix("scheduler-thread");
        threadPoolTaskScheduler.initialize();
        return threadPoolTaskScheduler;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        //TODO check that autowired works and delete this
/*        ThreadPoolTaskScheduler threadPoolTaskScheduler =new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setThreadNamePrefix("scheduler-thread");
        threadPoolTaskScheduler.initialize();
        this.taskScheduler=threadPoolTaskScheduler;*/

        //taskRegistrar.setTaskScheduler(taskScheduler);

        //TODO change this - schedule all jobs in DB
        //scheduleScanJob(threadPoolTaskScheduler, BatchConfiguration.getCronExpression());

    }

    //{configureTasks(scheduledTaskRegistrar);} //configure le taskScheduler lors de l'initialisation du serveur


    /*Demande au scheduler de reprogrammer les balayages suivant
    * la cronExpression passée en parametre.*/
    public void scheduleScanJob(Job scanJob, String cronExpression) {
        scheduledJobs.add(taskScheduler.schedule(
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
               new CronTrigger(cronExpression)
            )
        );
        scheduledJobsNames.add(scanJob.getName());
    }

    //TODO delete this
    /*Lorsqu'on modifie la cronExpression de la classe BatchConfiguration, on
    appele cette méthode pour replanifier l'execution des balayages suivants
    la nouvelle expression cron*/
    public void refreshCronSchedule(String cronExpression){

        /*if(job!=null){
            job.cancel(true);
        }*/
    }

}
