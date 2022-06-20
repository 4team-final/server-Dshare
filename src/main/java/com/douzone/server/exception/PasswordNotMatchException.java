package com.douzone.server.exception;

public class PasswordNotMatchException extends DshareServerException{
    public PasswordNotMatchException(ErrorCode errorCode) {
        super(errorCode.PW_NOT_MATCH);
    }
}
