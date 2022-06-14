package com.douzone.server.config.security.interceptor;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Dispatcher Servlet 을 통과 후 Controller 에 도달하기 전에 지정된 URL 을 통과할 경우 작동
 * 여러 Interceptor 를 설정하여 작업 처리 가능
 * order() 메소드를 통해 Interceptor 의 처리 순서 결정
 */

@Configuration
@RequiredArgsConstructor
public class InterceptorConfig implements WebMvcConfigurer {
	private final RoleInterceptor roleInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(roleInterceptor)
				.order(1)
				.addPathPatterns("/**")
				.excludePathPatterns("/sign");
	}
}
