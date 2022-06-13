package com.douzone.server.config.jwt;

import com.douzone.server.employee.domain.token.CommonTokenSet;
import com.douzone.server.employee.domain.token.ReIssuanceTokenSet;
import com.douzone.server.employee.domain.token.Token;
import com.douzone.server.employee.domain.token.TokenRepository;
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
 * */

@Slf4j
@Component
public class JwtTokenProvider {
	private static final String METHOD_NAME = "JwtTokenProvider";
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

	public CommonTokenSet generateToken(String userPk) {
		log.info(METHOD_NAME + "- generateToken() ...");
		Date now = new Date();

		String accessToken = generateAccessToken(userPk);
		String refreshToken = Jwts.builder()
									.setSubject(userPk)
									.setExpiration(new Date(now.getTime() + refreshValidTime))
									.signWith(SignatureAlgorithm.HS512, secretKey)
									.compact();

		return CommonTokenSet.builder().accessToken(accessToken)
				.reIssuanceTokenSet(ReIssuanceTokenSet.builder()
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
					.setExpiration(new Date(now.getTime()+ accessValidTime))
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

			if(token.startsWith(typeKeyAccess)) {
				return TokenResDTO.builder()
									.code(0)
									.token(token.replace(typeKeyAccess, ""))
									.build();
			}
			if(token.startsWith(typeKeyRefresh)){
				return TokenResDTO.builder()
									.code(1)
									.token(token.replace(typeKeyRefresh, "")).build();
			}
		} catch (NullPointerException ne) {
			log.error("요청 값이 비어 있습니다. " + METHOD_NAME);
		} catch (Exception e) {
			log.error("SERVER ERROR " + METHOD_NAME, e);
		}
		return TokenResDTO.builder().code(2).token("").build();
	}

	public boolean saveRefresh(ReIssuanceTokenSet reIssuanceTokenSet) {
		log.info(METHOD_NAME + "- saveRefresh() ...");
		try {
			Token tokenEntity = tokenRepository.save(Token.builder()
															.empNo(reIssuanceTokenSet.getEmpNo())
															.refreshToken(reIssuanceTokenSet.getRefreshToken())
															.build());
			if(tokenEntity.getEmpNo() != null) return true;
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
			if(this.validateToken(token)) {
				String userPk = this.getUserPk(token);
				String existingToken = tokenRepository.findByEmpNo(userPk).getRefreshToken();
				if(existingToken.equals(token)) return true;
			}
		} catch (Exception e) {
			log.error("토큰 저장소 비교 검증 에러 " + METHOD_NAME, e);
		}
		return false;
	}

	public boolean updateRefresh(ReIssuanceTokenSet reIssuanceTokenSet) {
		log.info(METHOD_NAME + "- updateRefresh() ...");
		try {
			Integer result = tokenRepository.updateToken(reIssuanceTokenSet.getRefreshToken(), reIssuanceTokenSet.getEmpNo());
			if(result > 0) return true;
		} catch (NullPointerException ne) {
			log.error("토큰 저장소가 비어있습니다. " + METHOD_NAME, ne);
		} catch (Exception e) {
			log.error("SERVER ERROR " + METHOD_NAME, e);
		}
		return false;
	}
}
