package com.example.Balayage.batch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

import java.util.concurrent.ScheduledFuture;

public class ScheduledConfiguration implements SchedulingConfigurer {
    ThreadPoolTaskScheduler taskScheduler;
    private ScheduledFuture<?> job1;

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
        scheduleScanJob(threadPoolTaskScheduler, BatchConfiguration.getCronExpression());// Assign the job1 to the scheduler
        this.taskScheduler=threadPoolTaskScheduler;// this will be used when refreshing the cron expression dynamically
        taskRegistrar.setTaskScheduler(threadPoolTaskScheduler);

    }

    {configureTasks(scheduledTaskRegistrar);} //configure le taskScheduler lors de l'initialisation du serveur

    private void scheduleScanJob(ThreadPoolTaskScheduler scheduler, String cronExpression) {
        job1 = scheduler.schedule(new BalayageTask(),
                new CronTrigger(cronExpression)
                );
    }

    public void refreshCronSchedule(){

        if(job1!=null){
            job1.cancel(true);
        }
        scheduleScanJob(taskScheduler, BatchConfiguration.getCronExpression());
    }

}
