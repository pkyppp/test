package com.example.demo;

import java.util.List;

import org.assertj.core.internal.Iterables;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import lombok.Setter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled=true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Component
	@Setter
	@ConfigurationProperties("security")
	public static class ServerSecurityConfig {
		
		private String permitAllAntPattern;
		private String ignoreSpringSecurityAntPattern;
		
		public List<String> getPermitAllAntPattern() {
			if(permitAllAntPattern == null) {
				return Lists.newArrayList();
			}
			return Lists.newArrayList(permitAllAntPattern.split(","));
		}
		public List<String> getIgnoreSpringSecurityAntPattern() {
			if(ignoreSpringSecurityAntPattern == null) {
				return Lists.newArrayList();
			}
			return Lists.newArrayList(ignoreSpringSecurityAntPattern.split(","));
		}		
	}
	
	@Autowired
	private SessionRegistry sessionRegistry;
	@Autowired
	private SeverSecurityConfig severSecurityConfig;
	@Autowired
	private SystemManager systemManager;
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers(Iterables.toArray(severSecurityConfig.getIgnoreSecurityAntPattern(), String.class));
	}
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	http.addFilterAt()authenticationFilter(), UsernamepasswordauthenticationFilter.class)
		.authorizeRequests()
		.antMatchers(Iterables.toArray(severSecurityConfig.getPermitAllAntPattern(), String.class)).permitAll()
		.anyRequest().authenticated()
		.and()
	.formLogin()
		.loginPage(Wb010Controller.WB010C_PATH)
		.permitAll()
		.and()
	.logout()
		.logoutRequestMatcher(new AntPathrequestMatcher("/logout"))
		.permitAll().deleteCookies("SESSION")
		.and()
	.exceptionHandling()
		.accessDeniedpage("/404")
		.and()
	.sessionManagement()
		.invalidSessionStrategy(new CustomRedirectInvalidSessionStrategy(systemManager))
		.maximumSession(-1)
		.sessionRegistry(sessionRegistry);
	}
	
	private UsernamePasswordAuthenticationFilter authenticationFilter( throws Exception) {
		UsernamePasswordAuthenticationFilter filter = new UsernamePasswordAuthenticationFilter();
		filter.setRequiresAuthenticationRequestMatcher(
				new AntPathRequestMatcher(Wb010cController.WB010C_PATH, "POST"));
		filter.setUsernameParameter("smypagid");
		filter.setPasswordParameter("password");
		filter.setAuthenticationManager(authenticationManagerBean());
		filter.setAuthenticationFailureHandler(new AuthenticationFailureHandler());
		SimpleUrlAuthenticationSuccessHandler authenticationSuccessHandler =
				new AuthenticationSuccessHandler();
		authSuccessFilterHandler.setDefaultTargetUrl(Wb015cControler.WB015C_PATH);
		authSuccessFilterHandler.setAlwaysUseDefaultTargetUrl(true);
		filter.setAuthenticationSuccessHandler(authSuccessFilterHandler);
		filter.setSessionAuthenticationStrategy(new ChangeSessionIdAuthenticationStrategy());
		return filter;				
	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth, UserService userService) {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(new LoginUserDetailService(userService));
		daoAuthenticationProvider.setHideUserNotFoundException(false);
		daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
		auth.authenticationProvider(daoAuthenticationProvider);
	}
	
}
