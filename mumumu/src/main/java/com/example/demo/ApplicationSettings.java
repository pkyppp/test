package com.example.demo;

import java.util.Map;

import org.omg.CORBA.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Setter;

@Component
@ConfigurationProperties(prefix="appsettings")
public class ApplicationSettings {
	private static ApplicationSettings myself;
	
	@Setter
	private Map<String, String> properties;
	
	@Autowired(required=false)
	private void setApplicationSettings(ApplicationSettings applicationSettings) {
		myself = applicationSettings;
	}
	
	public static String getTenantId() {
		if (myself == null) {
			return null;
		}
		String tenantid = myself.properties.get("tenantid");
		if(tenantid == null) {
			throw new SystemException("fdjkldf");
		}
		return tenantid;
	}
	
	
}
