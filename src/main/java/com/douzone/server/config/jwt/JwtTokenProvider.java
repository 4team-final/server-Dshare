package com.douzone.server.config.jwt;

import com.douzone.server.employee.domain.token.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class JwtTokenProvider {
	@Value("${jwt.secret-key}") private String secretValue;
	@Value("${jwt.header.access}") private String headerKeyAccess;
	@Value("${jwt.type.access}") private String typeKeyAccess;
	@Value("${jwt.type.refresh}") private String typeKeyRefresh;
	@Value("${jwt.time.access}") private long accessValidTime;
	@Value("${jwt.time.refresh}") private long refreshValidTime;
	private final TokenRepository tokenRepository;
	private final String secretKey;

	@Autowired
	public JwtTokenProvider(TokenRepository tokenRepository) {
		this.tokenRepository = tokenRepository;
		this.secretKey = Base64.getEncoder().encodeToString(secretValue.getBytes());
	}
}
