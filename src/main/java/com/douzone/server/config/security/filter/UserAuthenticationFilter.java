package com.douzone.server.config.security.filter;

import com.douzone.server.config.jwt.JwtTokenProvider;
import com.douzone.server.config.security.handler.ResponseHandler;
import com.douzone.server.config.security.handler.UserLoginFailureHandler;
import com.douzone.server.config.utils.Payload;
import com.douzone.server.employee.domain.employee.Employee;
import com.douzone.server.employee.domain.token.CommonTokenSet;
import com.douzone.server.employee.domain.token.Token;
import com.douzone.server.employee.domain.token.TokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * URL 이 /login 으로 넘어올 경우 spring security 에서 자동으로 attemptAuthentication() 으로 보내줌
 * attemptAuthentication() 에서 유저 정보를 확인 후 성공하면 successfulAuthentication()
 * 실패할 경우 unsuccessfulAuthentication() 으로 자동 이동 시켜줌
 * */

@Slf4j
@RequiredArgsConstructor
public class UserAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private static final String METHOD_NAME = "UserAuthenticationFilter";
	private final UserAuthenticationManager userAuthenticationManager;
	private final JwtTokenProvider jwtTokenProvider;
	private final TokenRepository tokenRepository;
	@Value("${jwt.header.access}") private String headerKeyAccess;
	@Value("${jwt.header.refresh}") private String headerKeyRefresh;
	@Value("${jwt.type.access}") private String typeKeyAccess;
	@Value("${jwt.type.refresh}") private String typeKeyRefresh;

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		log.info(METHOD_NAME + "- attemptAuthentication() ...");
		try {
			Employee employee = new ObjectMapper().readValue(request.getInputStream(), Employee.class);

			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(employee.getEmpNo(), employee.getPassword());

			return userAuthenticationManager.authenticate(authenticationToken);
		} catch (IOException ie) {
			log.error("유저 정보를 읽지 못했습니다. " + METHOD_NAME, ie);
			unsuccessfulAuthentication(request, response, ie);
		} catch (NullPointerException ne) {
			log.error("받은 유저 정보가 비어 있습니다. " + METHOD_NAME, ne);
			unsuccessfulAuthentication(request, response, ne);
		} catch (Exception e) {
			log.error("SERVER ERROR " + METHOD_NAME, e);
			unsuccessfulAuthentication(request, response, e);
		}
		log.error("자격 증명에 실패하였습니다. " + METHOD_NAME);
		return null;
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request,
											HttpServletResponse response,
											FilterChain chain, Authentication authResult) throws ServletException {
		log.info(METHOD_NAME + "- successfulAuthentication() ...");

		try {
			String principal = String.valueOf(authResult.getPrincipal());

			Token token = tokenRepository.findByEmpNo(principal);
			if (token != null) {
				if (jwtTokenProvider.validateToken(token.getRefreshToken())) {
					log.info("RefreshToken validate success - AccessToken issuance");
					String accessToken = jwtTokenProvider.generateAccessToken(principal);

					response.addHeader(headerKeyAccess, typeKeyAccess + accessToken);
				}
				else {
					log.info("RefreshToken Expired - All Token issuance");
					CommonTokenSet commonTokenSet = jwtTokenProvider.generateToken(principal);
					if(!jwtTokenProvider.updateRefresh(commonTokenSet.getReIssuanceTokenSet())) log.warn("Token Set Update to Token Repository - Fail");

					response.addHeader(headerKeyAccess, typeKeyAccess + commonTokenSet.getAccessToken());
					response.addHeader(headerKeyRefresh, typeKeyRefresh + commonTokenSet.getReIssuanceTokenSet().getRefreshToken());
				}
			}
			else {
				log.info("First Login User - All Token issuance");
				CommonTokenSet commonTokenSet = jwtTokenProvider.generateToken(principal);
				if(!jwtTokenProvider.saveRefresh(commonTokenSet.getReIssuanceTokenSet())) log.warn("Token Set Save to Token Repository - Fail");

				response.addHeader(headerKeyAccess, typeKeyAccess + commonTokenSet.getAccessToken());
				response.addHeader(headerKeyRefresh,typeKeyRefresh + commonTokenSet.getReIssuanceTokenSet().getRefreshToken());
			}
			response.setContentType("text/html; charset=UTF-8");
			response.getWriter().write(new ResponseHandler().convertResult(HttpStatus.OK, Payload.SIGN_IN_OK));
		} catch (IOException ie) {
			log.error("유저 정보를 읽지 못했습니다. " + METHOD_NAME, ie);
		} catch (NullPointerException ne) {
			log.error("받은 유저 정보가 비어 있습니다. " + METHOD_NAME, ne);
		} catch (Exception e) {
			log.error("SERVER ERROR " + METHOD_NAME, e);
		}
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request,
											  HttpServletResponse response,
											  AuthenticationException failed) throws ServletException {
		log.info(METHOD_NAME + "- unsuccessfulAuthentication() ...");

		try {
			String message = new UserLoginFailureHandler().onAuthenticationFailure(failed);

			response.setContentType("text/html; charset=UTF-8");
			response.getWriter().write(new ResponseHandler().convertResult(HttpStatus.BAD_REQUEST, Payload.SIGN_IN_FAIL + message));
		} catch (IOException ie) {
			log.error("전달받은 정보를 읽지 못했습니다. " + METHOD_NAME, ie);
		} catch (Exception e) {
			log.error("SERVER ERROR " + METHOD_NAME, e);
		}
	}

	public void unsuccessfulAuthentication(HttpServletRequest request,
										   HttpServletResponse response,
										   Exception exception) {
		log.info(METHOD_NAME + "- unsuccessfulAuthentication() ...");

		try {
			SecurityContextHolder.clearContext();
			String message = new UserLoginFailureHandler().onAuthenticationFailure(exception);

			response.setContentType("text/html; charset=UTF-8");
			response.getWriter().write(new ResponseHandler().convertResult(HttpStatus.BAD_REQUEST, Payload.SIGN_IN_FAIL + message));
		} catch (IOException ie) {
			log.error("전달받은 정보를 읽지 못했습니다. " + METHOD_NAME, ie);
		} catch (Exception e) {
			log.error("SERVER ERROR " + METHOD_NAME, e);
		}
	}
}
