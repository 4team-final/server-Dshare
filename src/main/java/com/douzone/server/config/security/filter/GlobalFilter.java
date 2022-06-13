package com.douzone.server.config.security.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class GlobalFilter {
	@Value("${user.cors.pattern}") private String corsPattern;
	@Value("${user.cors.header}") private String corsHeader;
	@Value("${user.cors.method}") private String corsMethod;
	@Value("${user.cors.source-pattern}") private String corsSource;
	@Value("${user.url.client}") private String clientURL;
	@Value("${jwt.header.access}") private String headerAccess;
	@Value("${jwt.header.refresh}") private String headerRefresh;

	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOriginPattern(corsPattern);
//		config.addAllowedOrigin(clientURL);
		config.addAllowedHeader(corsHeader);
		config.addAllowedMethod(corsMethod);
		config.addExposedHeader(headerAccess);
		config.addExposedHeader(headerRefresh);
		source.registerCorsConfiguration(corsSource, config);
		return new CorsFilter(source);
	}
}
