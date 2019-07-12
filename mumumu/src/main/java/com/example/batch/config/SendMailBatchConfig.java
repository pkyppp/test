package com.example.batch.config;

import java.time.Clock;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableBatchProcessing
@Slf4j
public class SendMailBatchConfig {
	private static final int DEFAULT_LIMIT = 100;
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	@Autowired
	private DataSource dataSource;
	@Autowired
	private Environment env;
	
	@Bean 
	public JdbcCursorItemReader<SendMailTargetData> jdbcCursorItemReader() {
		Integer limit = env.getProperty("batch.send.limei", Integer.class, DEFAULT_LIMIT);
		log.debug("total cnt : {}", limit);
		
		return new JdbcCursorItemReader<SendMailTargetData>().setDataSource(dataSource)
				.name("jdbcCursorItemReader")
				.fetchSize(limit)
				.sql("Select * FROM xxx where sss ORDER BY sss.aaa limit " + limit + ";")
				.rowMapper(new SendMailDataRowMapper()).build();
	}
	
	@Bean
	public Clock clock() {
		return Clock.systemDefaultZone();
	}
	
	@Bean
	public Step sendMailStep(final JdbcCursoritemReader<SendMailTargetData> reader,
			final SendMailItemProcessor processor, final SendMailItemWriter writer) throw Exception {
		return stepBuilderFactory.get("sendMailStep").<SendMailTargetData, SendMailData>chunk(1)
				.reader(reader)
				.processor(processor)
				.writer(writer)
				.build();
	}
	
	@Bean
	public Job sendMailJob(Step sendMailStep) {
		return jobBuilderFactory.get("sendMailJob").incrementer(new RunIdIncrementer())
				.flow(sendMailStep).end().build();
	}
}
