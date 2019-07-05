package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

@Configuration
public class DataAccessConfig {

	@Bean
	AuditorAware auditorAware(SystemManager sm) {
		AuditorAware auditorAware = new AuditorAware() {
			@Override
			public String getCurrentTnntid() {
				return ApplicationSettings.getTenantId();
			}
			@Override
			public String getCurrentTenno() {
				return SpringSecurityHelper.getLoginUser() != null ? SpringSecurityHelper.getLoginUser().getTenno() : sm.getSystemInf().getSystusrTenno();
			}
		};
		return auditAware;
	}

}
