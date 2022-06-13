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
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
	@Value("${user.url.logout}") private String logoutURL;
	@Value("${user.permit-all}") private String permitAll;
	@Value("${user.session-id}") private String sessionId;
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
					.addFilter(new UserAuthenticationFilter(userAuthenticationManager, jwtTokenProvider, tokenRepository))
					.addFilter(new JwtTokenAuthorizationFilter(userAuthenticationManager, jwtTokenProvider, principalDetailService))
					.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
					.authorizeRequests()
					.antMatchers(permitAll).permitAll();
		return http.build();
	}
}
