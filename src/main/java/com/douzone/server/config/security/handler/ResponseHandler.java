package com.douzone.server.config.security.handler;

import com.douzone.server.config.utils.ResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.io.IOException;

@Slf4j
public class ResponseHandler {
	private static final String METHOD_NAME = "ResponseHandler";
	private final ObjectMapper objectMapper = new ObjectMapper();

	public String convertResult(HttpStatus httpStatus, String message) {
		log.info(METHOD_NAME + "- convertResult() ...");
		String result = "메시지 변환 에러";
		try {
			result = objectMapper.writeValueAsString(ResponseDTO.builder()
																.status(httpStatus)
																.message(message)
																.build());
		} catch (IOException ie) {
			log.error("입력 값을 읽어오지 못했습니다. " + METHOD_NAME, ie);
		} catch (Exception e) {
			log.error("SERVER ERROR " + METHOD_NAME, e);
		}
		return result;
	}
}
