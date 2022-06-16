package com.douzone.server.config.utils;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO {
    private HttpStatus status;
    private String message;
    private Object value;

    public ResponseDTO(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public static ResponseDTO of(HttpStatus status, String message, Object value) {
        return new ResponseDTO(status, message, value);
    }

    public static ResponseDTO of(HttpStatus status, String message) {
        return new ResponseDTO(status, message);
    }



    public static ResponseDTO fail(HttpStatus status, String message) {
        return new ResponseDTO(status, message, null);
    }
}

