package com.douzone.server.config.security.filter;

import com.douzone.server.config.jwt.JwtTokenProvider;
import com.douzone.server.config.security.handler.ResponseHandler;
import com.douzone.server.config.security.handler.UserLoginFailureHandler;
import com.douzone.server.dto.token.CommonTokenDTO;
import com.douzone.server.entity.Employee;
import com.douzone.server.entity.Token;
import com.douzone.server.repository.TokenRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.douzone.server.config.utils.Msg.FAIL_SIGN_IN;
import static com.douzone.server.config.utils.Msg.SUCCESS_SIGN_IN;

/**
 * URL 이 /login 으로 넘어올 경우 spring security 에서 자동으로 attemptAuthentication() 으로 보내줌
 * attemptAuthentication() 에서 유저 정보를 확인 후 성공하면 successfulAuthentication()
 * 실패할 경우 unsuccessfulAuthentication() 으로 자동 이동 시켜줌
 */

@Slf4j
@Setter
@Component
public class UserAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private static final String METHOD_NAME = UserAuthenticationFilter.class.getName();
	private UserAuthenticationManager userAuthenticationManager;
	private JwtTokenProvider jwtTokenProvider;
	private TokenRepository tokenRepository;
	private String headerKeyAccess;
	private String typeAccess;

	@Autowired
	public UserAuthenticationFilter(UserAuthenticationManager userAuthenticationManager, JwtTokenProvider jwtTokenProvider, TokenRepository tokenRepository) {
		super(userAuthenticationManager);
		this.userAuthenticationManager = userAuthenticationManager;
		this.jwtTokenProvider = jwtTokenProvider;
		this.tokenRepository = tokenRepository;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		log.info(METHOD_NAME + "- attemptAuthentication() ...");
		try {
			Employee employee = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(request.getInputStream(), Employee.class);

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
				log.info("Token Set Existed - Token issuance");
				CommonTokenDTO commonTokenDTO = jwtTokenProvider.generateToken(principal);
				if (!jwtTokenProvider.updateRefresh(commonTokenDTO.getReIssuanceTokenDTO()))
					log.warn("Token Set Update to Token Repository - Fail");
				response.addHeader(headerKeyAccess, typeAccess + commonTokenDTO.getAccessToken());
				response.addCookie(jwtTokenProvider.generateCookie(commonTokenDTO.getReIssuanceTokenDTO().getRefreshToken()));
			} else {
				log.info("First Login User - Token issuance");
				CommonTokenDTO commonTokenDTO = jwtTokenProvider.generateToken(principal);
				if (!jwtTokenProvider.saveRefresh(commonTokenDTO.getReIssuanceTokenDTO()))
					log.warn("Token Set Save to Token Repository - Fail");
				response.addHeader(headerKeyAccess, typeAccess + commonTokenDTO.getAccessToken());
				response.addCookie(jwtTokenProvider.generateCookie(commonTokenDTO.getReIssuanceTokenDTO().getRefreshToken()));
			}
			response.setContentType("text/html; charset=UTF-8");
			response.getWriter().write(new ResponseHandler().convertResult(HttpStatus.OK, SUCCESS_SIGN_IN));
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
			response.getWriter().write(new ResponseHandler().convertResult(HttpStatus.BAD_REQUEST, FAIL_SIGN_IN + message));
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
			response.getWriter().write(new ResponseHandler().convertResult(HttpStatus.BAD_REQUEST, FAIL_SIGN_IN + message));
		} catch (IOException ie) {
			log.error("전달받은 정보를 읽지 못했습니다. " + METHOD_NAME, ie);
		} catch (Exception e) {
			log.error("SERVER ERROR " + METHOD_NAME, e);
		}
	}
}