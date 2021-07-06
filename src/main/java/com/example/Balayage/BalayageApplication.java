package com.example.Balayage;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;


@SpringBootApplication
@EnableScheduling
public class BalayageApplication {
	@Autowired
	JobLauncher jobLauncher;

	@Autowired
	Job scanJob;

	public static void main(String[] args) {
		SpringApplication.run(BalayageApplication.class, args);
	}

	//TODO modifier ca (la ca s'active toutes les 1 minutes)
	//Genere un rapport tous les jours, à 8heures

	@Scheduled(cron = "0 * 8 * * *")
	public void schedule() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
		{
			jobLauncher.run(scanJob, new JobParametersBuilder()
					.addDate("date", new Date())
					.toJobParameters());
		}
	}

}
