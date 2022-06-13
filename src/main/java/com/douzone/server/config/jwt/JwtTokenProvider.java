package com.douzone.server.config.jwt;

import com.douzone.server.employee.domain.token.Token;
import com.douzone.server.employee.domain.token.TokenRepository;
import com.douzone.server.employee.dto.token.CommonTokenDTO;
import com.douzone.server.employee.dto.token.ReIssuanceTokenDTO;
import com.douzone.server.employee.dto.token.TokenResDTO;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

/**
 * generateToken() : 사번 값을 입력하여 accessToken, refreshToken 을 CommonTokenSet 으로 리턴
 * generateAccessToken() : 사번 값을 입력하여 accessToken 을 String 으로 리턴
 * getUserPk() : accessToken 값을 입력하여 Token 안에 있는 유저의 사번을 String 으로 리턴
 * validateToken() : accessToken 검증 함수
 * requestCheckToken() : Token 이 accessToken 인지 refreshToken 인지 확인 후 TokenReqDTO 로 리턴 ... 0 : accessToken / 1 : refreshToken / 2 : 에러
 * saveRefresh() :
 * validateExistingToken() : refreshToken 검증 함수
 * updateRefresh() : refreshToken 을 새로 받아 DB에 업데이트
 */

@Slf4j
@Component
public class JwtTokenProvider {
	private static final String METHOD_NAME = "JwtTokenProvider";
	private final TokenRepository tokenRepository;
	private final String headerKeyAccess;
	private final String typeAccess;
	private final String typeRefresh;
	private final String secretKey;
	private final long accessValidTime;
	private final long refreshValidTime;

	@Autowired
	public JwtTokenProvider(TokenRepository tokenRepository,
							@Value(value = "${jwt.header.access}") String headerKeyAccess,
							@Value(value = "${jwt.type.access}") String typeAccess,
							@Value(value = "${jwt.type.refresh}") String typeRefresh,
							@Value(value = "${jwt.secret.key}") String secretValue,
							@Value(value = "${jwt.time.access}") String accessValidString,
							@Value(value = "${jwt.time.refresh}") String refreshValidString) {
		this.tokenRepository = tokenRepository;
		this.headerKeyAccess = headerKeyAccess;
		this.typeAccess = typeAccess;
		this.typeRefresh = typeRefresh;
		this.secretKey = Base64.getEncoder().encodeToString(secretValue.getBytes());
		this.accessValidTime = Long.parseLong(accessValidString) * 1000;
		this.refreshValidTime = Long.parseLong(refreshValidString) * 1000;
	}

	public CommonTokenDTO generateToken(String userPk) {
		log.info(METHOD_NAME + "- generateToken() ...");
		Date now = new Date();

		String accessToken = generateAccessToken(userPk);
		String refreshToken = Jwts.builder()
				.setSubject(userPk)
				.setExpiration(new Date(now.getTime() + refreshValidTime))
				.signWith(SignatureAlgorithm.HS512, secretKey)
				.compact();

		return CommonTokenDTO.builder().accessToken(accessToken)
				.reIssuanceTokenDTO(ReIssuanceTokenDTO.builder()
						.empNo(userPk)
						.refreshToken(refreshToken)
						.build()).build();
	}

	public String generateAccessToken(String userPk) {
		log.info(METHOD_NAME + "- generateAccessToken() ...");
		Date now = new Date();

		return Jwts.builder()
				.setSubject(userPk)
				.setIssuedAt(now)
				.setExpiration(new Date(now.getTime() + accessValidTime))
				.signWith(SignatureAlgorithm.HS512, secretKey)
				.compact();
	}

	public String getUserPk(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
	}

	public boolean validateToken(String token) {
		log.info(METHOD_NAME + "- validateToken() ...");
		try {
			Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
			return true;
		} catch (SignatureException se) {
			log.error("잘못된 서명 " + METHOD_NAME, se);
		} catch (MalformedJwtException me) {
			log.error("잘못된 토큰 " + METHOD_NAME, me);
		} catch (ExpiredJwtException ee) {
			log.error("만료된 토큰 " + METHOD_NAME, ee);
		} catch (UnsupportedJwtException ue) {
			log.error("지원되지 않는 토큰 " + METHOD_NAME, ue);
		} catch (IllegalArgumentException ie) {
			log.error("비어있는 토큰 " + METHOD_NAME, ie);
		} catch (NullPointerException ne) {
			log.error("존재하지 않는 토큰 " + METHOD_NAME, ne);
		}
		return false;
	}

	public TokenResDTO requestCheckToken(HttpServletRequest request) {
		log.info(METHOD_NAME + "- requestCheckToken() ...");

		try {
			String token = request.getHeader(headerKeyAccess);

			if (token.startsWith(typeAccess)) {
				return TokenResDTO.builder()
						.code(0)
						.token(token.replace(typeAccess, ""))
						.build();
			}
			if (token.startsWith(typeRefresh)) {
				return TokenResDTO.builder()
						.code(1)
						.token(token.replace(typeRefresh, "")).build();
			}
		} catch (NullPointerException ne) {
			log.error("요청 값이 비어 있습니다. " + METHOD_NAME);
		} catch (Exception e) {
			log.error("SERVER ERROR " + METHOD_NAME, e);
		}
		return TokenResDTO.builder().code(2).token("").build();
	}

	public boolean saveRefresh(ReIssuanceTokenDTO reIssuanceTokenDTO) {
		log.info(METHOD_NAME + "- saveRefresh() ...");
		try {
			Token tokenEntity = tokenRepository.save(Token.builder()
					.empNo(reIssuanceTokenDTO.getEmpNo())
					.refreshToken(reIssuanceTokenDTO.getRefreshToken())
					.build());
			if (tokenEntity.getEmpNo() != null) return true;
		} catch (NullPointerException ne) {
			log.error("토큰 셋이 비어있습니다. " + METHOD_NAME, ne);
		} catch (Exception e) {
			log.error("토큰 셋 저장 실패 " + METHOD_NAME, e);
		}
		return false;
	}

	public boolean validateRefreshToken(String token) {
		log.info(METHOD_NAME + "- validateExistingToken() ...");
		try {
			if (this.validateToken(token)) {
				String userPk = this.getUserPk(token);
				String existingToken = tokenRepository.findByEmpNo(userPk).getRefreshToken();
				if (existingToken.equals(token)) return true;
			}
		} catch (Exception e) {
			log.error("토큰 저장소 비교 검증 에러 " + METHOD_NAME, e);
		}
		return false;
	}

	public boolean updateRefresh(ReIssuanceTokenDTO reIssuanceTokenDTO) {
		log.info(METHOD_NAME + "- updateRefresh() ...");
		try {
			Integer result = tokenRepository.updateToken(reIssuanceTokenDTO.getRefreshToken(), reIssuanceTokenDTO.getEmpNo());
			if (result > 0) return true;
		} catch (NullPointerException ne) {
			log.error("토큰 저장소가 비어있습니다. " + METHOD_NAME, ne);
		} catch (Exception e) {
			log.error("SERVER ERROR " + METHOD_NAME, e);
		}
		return false;
	}
}
