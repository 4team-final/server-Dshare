package com.douzone.server.config.security.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * csrf, cors 등 web security 에서 설정하는 전역 필터 설정 클래스
 *
 * corsFilter() : cross-origin resource sharing 허용 설정
 * */

@Configuration
public class GlobalFilter {
	@Value(value ="${user.cors.pattern}") private String corsPattern;
	@Value(value ="${user.cors.header}") private String corsHeader;
	@Value(value ="${user.cors.method}") private String corsMethod;
	@Value(value ="${user.cors.source.pattern}") private String corsSource;
	@Value(value ="${user.url.client}") private String clientURL;
	@Value(value ="${jwt.header.access}") private String headerAccess;
	@Value(value ="${jwt.header.refresh}") private String headerRefresh;

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
