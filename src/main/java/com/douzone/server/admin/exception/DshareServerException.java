package com.douzone.server.admin.exception;

import lombok.Getter;

@Getter
public class DshareServerException extends RuntimeException {
    private ErrorCode errorCode;

    public DshareServerException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
