package com.example.demo;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.assertj.core.util.DateUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.example.demo.LocalDateTimeUtil.LocalDateTimeStrategy;

import lombok.extern.slf4j.Slf4j;

//@SpringBootApplication(scanBasePackages= {"aaa.bbb.common.config", "aaa.bbb.common", "aaa.bbb.web", "aaa.bbb.domain" })
//@MapperScan("aaa.bbb.domain.mapper")
@SpringBootApplication
@Slf4j
public class MumumuApplication {

	public static void main(String[] args) {
		SpringApplication.run(MumumuApplication.class, args);
	}

//	@Bean
//	public Mapper dozerBean() {
//		List<String> mappingFiles = Arrays.asList("dozer/dozer-global-configuration.xml");
//		DozerBeanMapper dozerBean = new DozerBeanMapper();
//		dozerBean.setMappingFiles(mappingFiles);
//		return dozerBean;
//	}

	@Bean
	public LocalValidatorFactoryBean validator(MessageSource messageSource) {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.setValidationMessageSource(messageSource);
		return localValidatorFactoryBean;
	}

	@Bean
	public LocalDateTimeStrategy localDateTimeStrategy() {
		return new LocalDateTimeStrategy() {
			@Override
			public LocalDateTime getCurrentDateTime() {
				return LocalDateTime.now();	
			}
			@Override
			public LocalDate getCurrentDate() {
				return LocalDate.now();		
			}
		};
	}
//	@Bean
//	public EncryptStrategy encryptStrategy() {
//		return new EncryptStrategy() {
//			@Override
//			public String passwordEncode(String pass) {
//				BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//				return encoder.encoder(pass);	
//			}
//			@Override
//			public boolean matchesPassword(String pawPass, String encodedPass) {
//				BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//				return encoder.matches(pawPass, encodedPass)
//			}
//		};
//	}
}
