package com.douzone.server.exception;

public class EmpAlreadyExistException extends DshareServerException {
    public EmpAlreadyExistException(ErrorCode errorCode) {
        super(errorCode.EMP_ALREADY_EXIST);
    }
}
