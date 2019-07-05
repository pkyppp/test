package com.example.demo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

@Configuration
public class UploadConfig {
		
	@Component
	@ConfigurationProperties("api.upload")
	private static class UploadSettingConfig {
		private String url;
	}
	
	@Bean
	Public UploadStrategy uploadStrategy(UploadSettingConfig config) {
		return data -> {
			if (config.url == null) {
				log.warn("xx");
				return new SuccessResponse(new UploadResultData("-999"));
			}
			String url = config.url;
			ApiResponse response = ApiRequestUtil.request(url. data);
			log.debug("##");
			
			ObjectMapper mapper = new ObjectMapper();
			Response responseData;
			try {
				JsonNode root = mapper.readTree(response.getBody());
				String code = root.get("code").asText();
				switch (code) {
				case 0:
					String id = root.get("resultData").get("id").asText();
					responseData = new SuccessResponse(new UploadResultData(id));
					break;
				case 999:
					responseData = new ErrorResponse("system error");
					break;
				default:
					responseData = new ErrorResponse("virus error", "E102");
					break;
				}	
				
			}catch (Exception e) {
				responseData = new ErrorResponse("system error");
			}
			return responseData;
		};
	}
}
