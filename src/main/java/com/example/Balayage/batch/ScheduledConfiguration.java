package com.example.Balayage.batch;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

import java.util.concurrent.ScheduledFuture;

/* Cette classe s'occupe de configurer la plannification des balayages.
*
* */
public class ScheduledConfiguration implements SchedulingConfigurer {
    ThreadPoolTaskScheduler taskScheduler;
    private ScheduledFuture<?> job;  //Pointeur sur le job de balayage actuellement programmé. A chaque fois que la plannification
                                    // des jobs est modifiée (nouvelle cronExpression), on reprogramme les jobs et on actualise
                                    //ce pointeur

    private ScheduledTaskRegistrar scheduledTaskRegistrar = new ScheduledTaskRegistrar();

    public ScheduledTaskRegistrar getScheduledTaskRegistrar() {
        return scheduledTaskRegistrar;
    }

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

        //TODO change this - schedule all jobs in DB
        //scheduleScanJob(threadPoolTaskScheduler, BatchConfiguration.getCronExpression());

        this.taskScheduler=threadPoolTaskScheduler;
        taskRegistrar.setTaskScheduler(threadPoolTaskScheduler);

    }

    {configureTasks(scheduledTaskRegistrar);} //configure le taskScheduler lors de l'initialisation du serveur


    /*Lorsqu'on modifie la cronExpression de la classe BatchConfiguration, on
    appele cette méthode pour replanifier l'execution des balayages suivants
    la nouvelle expression cron*/
    public void refreshCronSchedule(String cronExpression){

        if(job!=null){
            job.cancel(true);
        }
        scheduleScanJob(taskScheduler, cronExpression);
    }

    /*Demande au scheduler de reprogrammer les balayages suivant
    * la cronExpression passée en parametre.*/
    private void scheduleScanJob(ThreadPoolTaskScheduler scheduler, String cronExpression) {
        //TODO change this, use method in BatchConfiguration to create new scanJob (see ScanController (/start) )
        //job = scheduler.schedule(new BalayageTask(),
               // new CronTrigger(cronExpression)
        //);
    }

}
