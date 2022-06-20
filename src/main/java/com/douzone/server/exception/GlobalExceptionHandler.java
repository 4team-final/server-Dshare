package com.douzone.server.exception;

import com.douzone.server.config.utils.ErrorResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**유저가 이미 존재할때
     */
    @ExceptionHandler({EmpAlreadyExistException.class})
    public ResponseEntity<ErrorResponseDTO> EmpAlreadyExistException(EmpAlreadyExistException e) {
        return CustomExceptionReturn.returnException(e);
    }
    /**유저가 없을 때
     */
    @ExceptionHandler({EmpNotFoundException.class})
    public ResponseEntity<ErrorResponseDTO> EmpNotExistException(EmpAlreadyExistException e) {
        return CustomExceptionReturn.returnException(e);
    }
    /**예약이 없을 때
     */
    @ExceptionHandler({reservationNotFoundException.class})
    public ResponseEntity<ErrorResponseDTO> ResNotExistException(reservationNotFoundException e) {
        return CustomExceptionReturn.returnException(e);
    }

    /**비밀번호가 일치하지 않을 때
     */
    @ExceptionHandler({PasswordNotMatchException.class})
    public ResponseEntity<ErrorResponseDTO> PasswordNotMatchException(PasswordNotMatchException e) {
        return CustomExceptionReturn.returnException(e);
    }
    /**기본키에 없는 외래키를 입력했을 때
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDTO> PasswordNotMatchException(DataIntegrityViolationException e, ErrorCode ec) {
        return ExceptionReturn.returnException(e, ec);
    }

    /**함수에 인자가 안들어갔을 때 -> validation시 필드에 널값들어가면 발생
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public ResponseEntity<ErrorResponseDTO> handleMethodArgumentNotValidException(BindException e) {
        log.error("MethodArgumentNotValidException : ", e.getMessage());
        ErrorResponseDTO response = new ErrorResponseDTO(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    /** 함수에 인자가 들어갈때 타입이 안맞으면 발생 -> validation 수행 전에 실시
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ErrorResponseDTO> MethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("MethodArgumentTypeMismatchException : ", e.getMessage());
        ErrorResponseDTO response = new ErrorResponseDTO(ErrorCode.TYPE_MISMATCH);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /** 런타임 중에 익셉션 발생
     */
    @ExceptionHandler(DshareServerException.class)
    public ResponseEntity<ErrorResponseDTO> serverExceptionHandler(DshareServerException e) {
        return new CustomExceptionReturn().returnException(e);
    }

    /**
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    protected ResponseEntity<ErrorResponseDTO> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("HttpRequestMethodNotSupportedException : ", e.getMessage());
        ErrorResponseDTO response = new ErrorResponseDTO(ErrorCode.METHOD_NOT_ALLOWED);
        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
    }
    /**
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<ErrorResponseDTO> MissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.error("MissingServletRequestParameterException : ", e.getMessage());
        ErrorResponseDTO response = new ErrorResponseDTO(ErrorCode.MISSING_REQUEST_PARAMETER);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    /**
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponseDTO> handleMaxUploadSizeExceedException(MaxUploadSizeExceededException e) {
        log.error(String.format("MaxUploadSizeExceededException %s", e.getMessage()), e);
        ErrorResponseDTO response = new ErrorResponseDTO(ErrorCode.MAX_UPLOAD_SIZE_EXCEEDED);
        return new ResponseEntity<>(response, HttpStatus.PAYLOAD_TOO_LARGE);
    }
    /**
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponseDTO> handleException(final Exception e) {
        log.error("Exception : {}. message : {}",e.getClass(), e.getMessage());
        ErrorResponseDTO response = new ErrorResponseDTO(ErrorCode.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
