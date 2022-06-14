package com.douzone.server.exception;

import lombok.Getter;

@Getter
public class DshareServerException extends RuntimeException {
    private ErrorCode errorCode;

    public DshareServerException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
