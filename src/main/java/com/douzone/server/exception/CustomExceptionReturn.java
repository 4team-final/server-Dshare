package com.douzone.server.exception;

import com.douzone.server.config.utils.ErrorResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Slf4j
public class CustomExceptionReturn{
	public static ResponseEntity<ErrorResponseDTO> returnException(DshareServerException e) {
		log.error("{} : {}",  new Object[]{e.getErrorCode(),  e.getClass().getSimpleName()});
		ErrorCode errorCode = e.getErrorCode();
		return new ResponseEntity<>(new ErrorResponseDTO(errorCode.getStatus(), errorCode.getMessage()), HttpStatus.valueOf(errorCode.getStatus()));
	}
}
