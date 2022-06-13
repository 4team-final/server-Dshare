package com.douzone.server.config.security;

import com.douzone.server.config.jwt.JwtTokenProvider;
import com.douzone.server.config.security.auth.PrincipalDetailService;
import com.douzone.server.config.security.filter.GlobalFilter;
import com.douzone.server.config.security.filter.JwtTokenAuthorizationFilter;
import com.douzone.server.config.security.filter.UserAuthenticationFilter;
import com.douzone.server.config.security.filter.UserAuthenticationManager;
import com.douzone.server.config.security.handler.UserLogoutHandler;
import com.douzone.server.config.security.handler.UserLogoutSuccessHandler;
import com.douzone.server.employee.domain.token.TokenRepository;
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
	@Value(value = "${jwt.header.access}")
	private String headerKeyAccess;
	@Value(value = "${jwt.header.refresh}")
	private String headerKeyRefresh;
	@Value(value = "${jwt.type.access}")
	private String typeAccess;
	@Value(value = "${jwt.type.refresh}")
	private String typeRefresh;

	private final GlobalFilter globalFilter;
	private final UserLogoutHandler userLogoutHandler;
	private final UserLogoutSuccessHandler logoutSuccessHandler;
	private final UserAuthenticationManager userAuthenticationManager;
	private final JwtTokenProvider jwtTokenProvider;
	private final PrincipalDetailService principalDetailService;
	private final TokenRepository tokenRepository;

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
				.addFilter(new UserAuthenticationFilter(userAuthenticationManager, jwtTokenProvider, tokenRepository, headerKeyAccess, headerKeyRefresh, typeAccess, typeRefresh))
				.addFilter(new JwtTokenAuthorizationFilter(userAuthenticationManager, jwtTokenProvider, principalDetailService))
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.authorizeRequests()
				.antMatchers(permitAll).permitAll();
		return http.build();
	}
}
