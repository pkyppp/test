package com.example.demo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import lombok.Setter;

@Configuration
public class MailConfig {
	@Component
	@ConfigurationProperties("api.mail")
	@Setter
	private static class MailSettionConfig {
		private String url;
	}
	
	@Bean
	public MailSendStrategy mailSendStrategy(Environment env, MailSettingConfig config) {
		return data -> {
			if (config.url == null) {
				log.warn("syorimusi");
				return null;
			}
			ApiResponce response = ApiRequestUtil.request(config.url, data);
			log.debug("ssdsdsfxx request="+response.getRequestToStr());
			log.debug("ssdsdsfxx response="+response.getResponseToStr());
			response.checkResponse();
			checkResponse(response);
			return response.getResponse().getBody();
		};
	}
	
	protected static void checkResponse(ApiResponse response) {
		if (response.getStatusCode() != HttpStatus.OK) {
			throw new BusinessLoginException("xxx response="+response.getResponseToStr());
		}
		ResultResponse resultResponse = response.getApiData(ResultResponse.class);
		if (!"resultSuccessCode 0 ".equals(resultResponse.getResuldCode())) {
			throw new BusinessLoginException("xxx response="+response.getResponseToStr());
		}
	}
}
