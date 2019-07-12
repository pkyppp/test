package com.example.batch.config;



import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.PeriodicTrigger;

import lombok.extern.slf4j.Slf4j;

@ConditionalOnProperty(value="batch.scheduled.enable", havingValue="true", matchIfMissing=true)
@Configuration
@EnableScheduling
@Slf4j
public class ScheduleConfig implements SchedulingConfigurer{

	private static final Long DEFAULT_PERIOD = 10L * 1000L;
	@Autowired
	private Environment env;
	@Autowired
	private BatchConfigurer batchConfigurer;
	@Autowired
	private Job job;
	@Autowired
	private MailProperties mailProperties;
	
	@Bean(destroyMethod = "shutdown")
	public ThreadPoolTaskScheduler taskScheduler() {
		ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
		threadPoolTaskScheduler.setPoolSize(1);
		threadPoolTaskScheduler.setThreadNamePrefix("SendMailScheduler-");
		threadPoolTaskScheduler.initialize();
		return threadPoolTaskScheduler;
	}
	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		Long period = env.getProperty("batch.schduled.period", Long.class, DEFAULT_PERIOD);
		
		ThreadPoolTaskScheduler scheduler = taskScheduler();
		taskRegistrar.setScheduler(scheduler);
		taskRegistrar.addTriggerTask(new Runnable() {
			@Override
			public void run() {
				try {
					//log.debug("host:{}, port:{},", mailProperties.getProperty("host"), 
					//		mailProperties.getProperty("port"));
					JobExecution jobExecution = batchConfigurer.getJobLauncher()
							.run(job, new JobParametersBuilder()
									.addLong("time", System.nanoTime()).toJobParameters());
					if (BatchStatus.COMPLETED != jobExecution.getStatus()) {
						scheduler.shutdown();
					}
				} catch (Exception e) {
					//log.error("error :", e);
					scheduler.shutdown();
				}
			}
		}, new PeriodicTrigger(period) );
						
	}
	
}
