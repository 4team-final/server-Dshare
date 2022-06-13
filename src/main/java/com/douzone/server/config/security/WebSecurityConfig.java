package com.douzone.server.config.security;

import com.douzone.server.config.security.filter.GlobalFilter;
import com.douzone.server.config.security.handler.UserLogoutHandler;
import com.douzone.server.config.security.handler.UserLogoutSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig {
	@Value(value = "${user.url.logout}")
	private String logoutURL;
	@Value(value = "${user.permit.all}")
	private String permitAll;
	@Value(value = "${user.session.id}")
	private String sessionId;
	private final GlobalFilter globalFilter;
	private final UserLogoutHandler userLogoutHandler;
	private final UserLogoutSuccessHandler logoutSuccessHandler;

	@Bean
	public SecurityFilterChain securityConfig(HttpSecurity http) throws Exception {
		http.httpBasic().disable().csrf().disable().formLogin().disable()
				.logout()
				.logoutUrl(logoutURL)
				.deleteCookies(sessionId)
				.addLogoutHandler(userLogoutHandler)
				.logoutSuccessHandler(logoutSuccessHandler)
				.and()
				.addFilter(globalFilter.corsFilter())
				.addFilter(globalFilter.authenticationFilter())
				.addFilter(globalFilter.authorizationFilter())
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.authorizeRequests()
				.antMatchers(permitAll).permitAll();
		return http.build();
	}
}