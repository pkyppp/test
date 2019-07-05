package com.example.demo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisOperations;

import lombok.RequiredArgsConstructor;

@EnableSpringHttpSession
@Configuration
@RequiredArgsConstructor
@ConfigurationProperties("session")
public class SessionConfig {
	private final Environment env;
	private final RedisOperations<Object, Object> sessionRedisOperations;
	private Boolean useHttps = Boolean.FALSE;

	public void setUseHttps(Boolean useHttps) {
		this.useHttps = useHttps;
	}
	@Bean
	public SessionRegistry sessionRegistry() {
		if (sessionRepository() instanceof FindByIndexNameSessionRepository) {
			return new SpringSessionBackedSessionRegistry((FindByIndexNameSessionRepository) sessionRepository())
		}
		return new SessionRegistryImpl();
	}
	@Bean
	public <S extends Session> SessionRepository<?> SessionRepository() {
		if ("redis".equals(env.getProperty("spring.session.store-type"))) {
			return new RedisOperationSessionRepository(sessionRedisOperations);
		}
		return new MapSessionRepository(new ConcurrenthashMap<>());
	}
	@Bean
	public CookieSerializer cookieSerializer() {
		DefaultCookieSerializer serializer = new DefaultCookieSerializer();
		if (useHttps) {
			serializer.setUseSecureCookie(true);
		}
		return serializer;
	}
	
}
