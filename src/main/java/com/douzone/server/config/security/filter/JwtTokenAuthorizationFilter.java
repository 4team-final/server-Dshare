package com.douzone.server.config.security.filter;

import com.douzone.server.config.jwt.JwtTokenProvider;
import com.douzone.server.config.security.auth.PrincipalDetailService;
import com.douzone.server.config.security.handler.ResponseHandler;
import com.douzone.server.config.utils.Msg;
import com.douzone.server.dto.token.TokenResDTO;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 제외 지정한 URL 이 아닌 모든 URL 이 인가된 Token 을 보유하고 있는지 검증하는 클래스
 * 액세스 토큰 보유를 확인해서 올바른 토큰을 보유하고 있는 경우 doFilter 를 사용하여 다음 필터로 패스
 * 토큰이 올바르지 않을 경우 Fail Response 를 보냄
 * 리프레쉬 토큰을 보유한 경우 리프레쉬 토큰이 올바르면 액세스 토큰을 다시 리턴해 줌
 */

@Slf4j
@Setter
@Component
public class JwtTokenAuthorizationFilter extends BasicAuthenticationFilter {

	private static final String METHOD_NAME = JwtTokenAuthorizationFilter.class.getName();
	private final JwtTokenProvider jwtTokenProvider;
	private final PrincipalDetailService principalDetailService;
	private String headerKeyAccess;
	private String typeAccess;

	@Autowired
	public JwtTokenAuthorizationFilter(UserAuthenticationManager userAuthenticationManager, JwtTokenProvider jwtTokenProvider, PrincipalDetailService principalDetailService) {
		super(userAuthenticationManager);
		this.jwtTokenProvider = jwtTokenProvider;
		this.principalDetailService = principalDetailService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		log.info(METHOD_NAME + "- doFilterInternal() ...");
		try {
			TokenResDTO tokenResDTO = jwtTokenProvider.requestCheckToken(request);
			String token = tokenResDTO.getToken();
			switch (tokenResDTO.getCode()) {
				case 0:
					if (jwtTokenProvider.validateToken(token)) {
						log.info("Access Token Validation - Success");

						String userPk = jwtTokenProvider.getUserPk(token);

						UserDetails userDetails = principalDetailService.loadUserByUsername(userPk);
						UsernamePasswordAuthenticationToken authenticationToken =
								new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

						authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
						SecurityContextHolder.getContext().setAuthentication(authenticationToken);

						filterChain.doFilter(request, response);
					} else {
						log.info("Access Token Validation - Fail");

						response.setContentType("text/html; charset=UTF-8");
						response.getWriter().write(new ResponseHandler().convertResult(HttpStatus.BAD_REQUEST, Msg.FAIL_ACCESS + Msg.FAIL_TOKEN_VALIDATE));
					}
					return;
				case 1:
					if (jwtTokenProvider.validateRefreshToken(token)) {
						log.info("Refresh Token Validation - Success");
						String accessToken = jwtTokenProvider.generateAccessToken(jwtTokenProvider.getUserPk(token));

						response.addHeader(headerKeyAccess, typeAccess + accessToken);

						response.setContentType("text/html; charset=UTF-8");
						response.getWriter().write(new ResponseHandler().convertResult(HttpStatus.BAD_REQUEST, Msg.FAIL_ACCESS + Msg.FAIL_TOKEN_VALIDATE));
					}
					return;
				case 2:
				default:
					log.warn("Access/Refresh Token Validation - Fail");
			}
		} catch (NullPointerException ne) {
			log.error("토큰 값이 비어있습니다. " + METHOD_NAME);
		} catch (Exception e) {
			log.error("사용자 인증을 확인하지 못해 인가할 수 없습니다. " + METHOD_NAME, e);
		}
		response.setContentType("text/html; charset=UTF-8");
		response.getWriter().write(new ResponseHandler().convertResult(HttpStatus.INTERNAL_SERVER_ERROR, Msg.FAIL_ACCESS + Msg.FAIL_TOKEN_VALIDATE));
	}
}
