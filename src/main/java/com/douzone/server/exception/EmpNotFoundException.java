package com.douzone.server.exception;

public class EmpNotFoundException extends DshareServerException {

    public EmpNotFoundException(ErrorCode errorCode) {
        super(errorCode.EMP_NOT_FOUND);
    }
}
