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

import java.util.Date;
import java.util.concurrent.ScheduledFuture;

/* Cette classe s'occupe de configurer la plannification des balayages.
*
* */
@Configuration
public class ScheduledConfiguration implements SchedulingConfigurer {

    @Autowired
    @Qualifier("asyncJobLauncher")
    JobLauncher jobLauncher;

    ThreadPoolTaskScheduler taskScheduler;
    private ScheduledFuture<?> job;  //Pointeur sur le job de balayage actuellement programmé. A chaque fois que la plannification
                                    // des jobs est modifiée (nouvelle cronExpression), on reprogramme les jobs et on actualise
                                    //ce pointeur

    private ScheduledTaskRegistrar scheduledTaskRegistrar = new ScheduledTaskRegistrar();

    @Bean
    public ScheduledTaskRegistrar ScheduledTaskRegistrar() {
        ScheduledTaskRegistrar scheduledTaskRegistrar = new ScheduledTaskRegistrar();
        return scheduledTaskRegistrar;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {

        ThreadPoolTaskScheduler threadPoolTaskScheduler =new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setThreadNamePrefix("scheduler-thread");
        threadPoolTaskScheduler.initialize();
        this.taskScheduler=threadPoolTaskScheduler;
        taskRegistrar.setTaskScheduler(threadPoolTaskScheduler);

        //TODO change this - schedule all jobs in DB
        //scheduleScanJob(threadPoolTaskScheduler, BatchConfiguration.getCronExpression());

    }

    {configureTasks(scheduledTaskRegistrar);} //configure le taskScheduler lors de l'initialisation du serveur


    /*Demande au scheduler de reprogrammer les balayages suivant
    * la cronExpression passée en parametre.*/
    public void scheduleScanJob(Job scanJob, String cronExpression) {
        job = taskScheduler.schedule(new Runnable(){

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
        );
    }

    //TODO delete this
    /*Lorsqu'on modifie la cronExpression de la classe BatchConfiguration, on
    appele cette méthode pour replanifier l'execution des balayages suivants
    la nouvelle expression cron*/
    public void refreshCronSchedule(String cronExpression){

        if(job!=null){
            job.cancel(true);
        }
    }

}
