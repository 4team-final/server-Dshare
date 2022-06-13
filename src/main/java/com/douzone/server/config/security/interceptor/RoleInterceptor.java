package com.douzone.server.config.security.interceptor;

import com.douzone.server.config.jwt.JwtTokenProvider;
import com.douzone.server.config.security.handler.DecodeEncodeHandler;
import com.douzone.server.config.security.handler.ResponseHandler;
import com.douzone.server.config.utils.Payload;
import com.douzone.server.employee.dto.token.TokenResDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class RoleInterceptor implements HandlerInterceptor {
	private static final String METHOD_NAME = "RoleInterceptor";
	private final DecodeEncodeHandler decodeEncodeHandler;
	private final JwtTokenProvider jwtTokenProvider;
	private final String adminRole;
	private final String employeeRole;
	private final String adminURL;
	private final String employeeURL;

	@Autowired
	public RoleInterceptor(DecodeEncodeHandler decodeEncodeHandler, JwtTokenProvider jwtTokenProvider,
						   @Value(value = "${user.role.admin}") String adminRole,
						   @Value(value = "${user.role.employee}") String employeeRole,
						   @Value(value = "${user.url.admin}") String adminURL,
						   @Value(value = "${user.url.employee}") String employeeURL) {
		this.decodeEncodeHandler = decodeEncodeHandler;
		this.jwtTokenProvider = jwtTokenProvider;
		this.adminRole = adminRole;
		this.employeeRole = employeeRole;
		this.adminURL = adminURL;
		this.employeeURL = employeeURL;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		log.info(METHOD_NAME + "- preHandle() ...");
		boolean result = false;
		try {
			TokenResDTO tokenResDTO = jwtTokenProvider.requestCheckToken(request);
			String token = tokenResDTO.getToken();
			Outer:
			{
				if (jwtTokenProvider.validateToken(token)) {
					log.info("Token validate - success");
					String email = jwtTokenProvider.getUserPk(token);

					if (decodeEncodeHandler.empNoValid(email)) {
						log.info("User validate - Success");
						String role = decodeEncodeHandler.roleValid(email);
						if (request.getRequestURI().startsWith(adminURL)) {
							log.info("ADMIN role validate ...");
							if (role != null && role.equals(adminRole)) {
								log.info("ADMIN role validate - Success");
								result = true;
							} else {
								log.warn("ADMIN role validate - Fail");
								response.setContentType("text/html; charset=UTF-8");
								response.getWriter().write(new ResponseHandler().convertResult(HttpStatus.BAD_REQUEST, Payload.USER_ROLE_CHECK_FAIL));
							}
							break Outer;
						}
						if (request.getRequestURI().startsWith(employeeURL)) {
							log.info("USER role validate ...");
							if (role != null && role.equals(employeeRole)) {
								log.info("USER role validate - Success");
								result = true;
							} else {
								log.warn("USER role validate - Fail");
								response.setContentType("text/html; charset=UTF-8");
								response.getWriter().write(new ResponseHandler().convertResult(HttpStatus.BAD_REQUEST, Payload.USER_ROLE_CHECK_FAIL));
							}
							break Outer;
						}
						log.warn("Unverified role ACCESS ... ");

					} else {
						log.warn("Request User is not exist " + METHOD_NAME);
						response.setContentType("text/html; charset=UTF-8");
						response.getWriter().write(new ResponseHandler().convertResult(HttpStatus.BAD_REQUEST, Payload.USER_ROLE_CHECK_FAIL));
					}
				} else {
					log.warn("Token validate - Fail");
					response.setContentType("text/html; charset=UTF-8");
					response.getWriter().write(new ResponseHandler().convertResult(HttpStatus.BAD_REQUEST, Payload.TOKEN_FAIL));
				}
			}
			return result;
		} catch (IOException ie) {
			log.error("역할이 입력되지 않았습니다. " + METHOD_NAME, ie);
		} catch (NullPointerException ne) {
			log.error("역할이 존재하지 않습니다. " + METHOD_NAME, ne);
		} catch (Exception e) {
			log.error("SERVER ERROR " + METHOD_NAME, e);
		}
		return false;
	}
}
