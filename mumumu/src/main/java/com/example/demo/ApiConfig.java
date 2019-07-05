package com.example.demo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApiConfig {
	
	@Component
	@ConfigurationProperties("api.connection")
	private static class ApiSettingConfig {
		private int maxSize;
		private int timeoutMills;
	}
	
	@Bean
	public RestTemplate restTemplate(ApiSettingConfig config) {
		ClientHttpRequestFactory requestFactory = ApiRequestUtil.createpoolingHttpRequestFactory(
				config.maxSize, config.timeoutMills);
		return new RestTemplateBuilder().requestFactory(() -> requestFactory).errorHandler(responseErrorHandler()).build();
	}
	
	@Bean
	public ResponseErrorHandler responseErrorHandler() {
		return new ResponseErrorHandler() {
			
			@Override
			public boolean hasError(ClientHttpResponse response) {
				return false;
			}
			@Override
			public void handleError(ClientHttpResponse response) {
				
			}
			
		};
	}
}
