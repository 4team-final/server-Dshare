package com.douzone.server.config.security.handler;

import com.douzone.server.config.utils.Msg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
@Slf4j
public class UserLogoutSuccessHandler implements LogoutSuccessHandler {
	private static final String METHOD_NAME = UserLogoutSuccessHandler.class.getName();

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		log.info(METHOD_NAME + "- onLogoutSuccess() ...");
		try {
			response.setContentType("text/html; charset=UTF-8");
			response.getWriter().write(new ResponseHandler().convertResult(HttpStatus.OK, Msg.SUCCESS_SIGN_OUT));
			return;
		} catch (IOException ie) {
			log.error("전달받은 정보를 읽지 못했습니다. " + METHOD_NAME, ie);
		} catch (Exception e) {
			log.error("SERVER ERROR " + METHOD_NAME, e);
		}

		response.setContentType("text/html; charset=UTF-8");
		response.getWriter().write(new ResponseHandler().convertResult(HttpStatus.INTERNAL_SERVER_ERROR, Msg.FAIL_SIGN_OUT));
	}
}
