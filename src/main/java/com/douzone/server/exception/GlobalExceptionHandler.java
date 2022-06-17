package com.douzone.server.exception;

import com.douzone.server.config.utils.ErrorResponseDTO;
import com.douzone.server.config.utils.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.time.DateTimeException;
import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler({EmpAlreadyExistException.class})
	public ResponseEntity<ErrorResponseDTO> EmpAlreadyExistException(EmpAlreadyExistException e) {
		log.error("EmpAlreadyExistException : " + e.getMessage());
		ErrorCode errorCode = e.getErrorCode();
		return new ResponseEntity<>(new ErrorResponseDTO(errorCode.getStatus(), errorCode.getMessage()), HttpStatus.valueOf(errorCode.getStatus()));
	}

	@ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
	public ResponseEntity<ErrorResponseDTO> handleMethodArgumentNotValidException(BindException e) {
		log.error("MethodArgumentNotValidException : " + e.getMessage());
		ErrorResponseDTO response = new ErrorResponseDTO(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(DshareServerException.class)
	public ResponseEntity<ErrorResponseDTO> serverExceptionHandler(DshareServerException e) {
		log.error("DshareServerException : " + e.getMessage());
		ErrorCode errorCode = e.getErrorCode();
		return new ResponseEntity<>(new ErrorResponseDTO(errorCode.getStatus(), errorCode.getMessage()), HttpStatus.valueOf(errorCode.getStatus()));
	}

	@ExceptionHandler({HttpRequestMethodNotSupportedException.class})
	protected ResponseEntity<ErrorResponseDTO> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
		log.error("HttpRequestMethodNotSupportedException : " + e.getMessage());
		ErrorResponseDTO response = new ErrorResponseDTO(ErrorCode.METHOD_NOT_ALLOWED);
		return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	protected ResponseEntity<ErrorResponseDTO> MissingServletRequestParameterException(MissingServletRequestParameterException e) {
		log.error("MissingServletRequestParameterException : " + e.getMessage());
		ErrorResponseDTO response = new ErrorResponseDTO(ErrorCode.MISSING_REQUEST_PARAMETER);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	protected ResponseEntity<ErrorResponseDTO> MethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
		log.error("MethodArgumentTypeMismatchException : " + e.getMessage());
		ErrorResponseDTO response = new ErrorResponseDTO(ErrorCode.TYPE_MISMATCH);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<ErrorResponseDTO> handleMaxUploadSizeExceedException(MaxUploadSizeExceededException e) {
		log.error(String.format("MaxUploadSizeExceededException %s", e.getMessage()), e);
		ErrorResponseDTO response = new ErrorResponseDTO(ErrorCode.MAX_UPLOAD_SIZE_EXCEEDED);
		return new ResponseEntity<>(response, HttpStatus.PAYLOAD_TOO_LARGE);
	}

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<ErrorResponseDTO> handleException(final Exception e) {
		log.error("Exception : " + e.getMessage());
		ErrorResponseDTO response = new ErrorResponseDTO(ErrorCode.INTERNAL_SERVER_ERROR);
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(DataAccessException.class)
	protected ResponseDTO handleDataAccessException(DataAccessException e) {
		log.error("SERVER ERROR " + e.getMessage());
		return ResponseDTO.fail(HttpStatus.INTERNAL_SERVER_ERROR, "SQL 문법, 제약 조건 위배 혹은 DB 서버와의 연결을 실패하였습니다.");
	}

	@ExceptionHandler(DateTimeException.class)
	protected ResponseDTO handleDateTimeException(DateTimeException e) {
		log.error("SERVER ERROR " + e.getMessage());
		return ResponseDTO.fail(HttpStatus.INTERNAL_SERVER_ERROR, "시간 변환에 실패하였습니다.");
	}

	@ExceptionHandler(TransactionSystemException.class)
	protected ResponseDTO handleTransactionSystemException(TransactionSystemException e) {
		log.error("SERVER ERROR " + e.getMessage());
		return ResponseDTO.fail(HttpStatus.INTERNAL_SERVER_ERROR, "트랜잭션 커밋을 실패하였습니다.");
	}

	@ExceptionHandler(ConversionFailedException.class)
	protected ResponseDTO handleConversionFailedException(ConversionFailedException e) {
		log.error("SERVER ERROR " + e.getMessage());
		return ResponseDTO.fail(HttpStatus.INTERNAL_SERVER_ERROR, "서비스로의 리턴 형식이 잘못되었습니다.");
	}

	@ExceptionHandler(NoSuchElementException.class)
	protected ResponseDTO handleNoSuchElementException(NoSuchElementException e) {
		log.error("SERVER ERROR " + e.getMessage());
		return ResponseDTO.fail(HttpStatus.INTERNAL_SERVER_ERROR, "값이 들어갈 공간이 없습니다.");
	}

	@ExceptionHandler(NullPointerException.class)
	protected ResponseDTO handleNullPointerException(NullPointerException e) {
		log.error("SERVER ERROR - Vehicle Service" + e.getMessage());
		return ResponseDTO.fail(HttpStatus.INTERNAL_SERVER_ERROR, "받은 정보가 비어있습니다.");
	}
}
