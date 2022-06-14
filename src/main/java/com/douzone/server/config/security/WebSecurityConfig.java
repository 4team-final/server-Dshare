package com.douzone.server.config.security;

import com.douzone.server.config.security.filter.GlobalFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
	private final GlobalFilter globalFilter;

	@Bean
	public SecurityFilterChain securityConfig(HttpSecurity http) throws Exception {
		http.httpBasic().disable().csrf().disable().formLogin().disable()
				.logout()
				.logoutUrl(globalFilter.getLogoutURL())
				.deleteCookies(globalFilter.getSessionId())
				.addLogoutHandler(globalFilter.logoutHandler())
				.logoutSuccessHandler(globalFilter.logoutSuccessHandler())
				.and()
				.addFilter(globalFilter.corsFilter())
				.addFilter(globalFilter.authenticationFilter())
				.addFilter(globalFilter.authorizationFilter())
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.authorizeRequests()
				.antMatchers(globalFilter.getPermitAll()).permitAll();
		return http.build();
	}
}