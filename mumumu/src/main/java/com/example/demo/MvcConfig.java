package com.example.demo;

import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class MvcConfig implements WebMvcConfigure{
	
	private final XxxService xxxService;
	private final UserService userService;
	private final ServerSecurityConfig serverSecurityConfig;
	private final SystemManager systemManager;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new CheckMaintenanceInterceptor()).addPathPattern("/**")
			.excludePathPatterns(serverSecurityConfig.getIgnoreSpringSecurityAntPattern())
			.excludePathPatterns(new ErrorProperties().getPath());
		//....
		registry.addInterceptor(new CheckInvalidSessionInterceptor(systemManager))
			.addPathPatterns("/wa**");
	}
	WebMvcConfigurer.super.addInterceptors(registry);	
}
