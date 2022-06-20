package com.douzone.server.exception;

import com.douzone.server.config.utils.ErrorResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Slf4j

public class ExceptionReturn {
	public static ResponseEntity<ErrorResponseDTO> returnException(Exception e, ErrorCode ec) {
		//내가 직접 커스텀한 예외가 아닐때
		log.error("{} : {}", new Object[]{e.getClass().getSimpleName(), e.getMessage()});
		ErrorResponseDTO response = new ErrorResponseDTO(ec);
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
	}
}
