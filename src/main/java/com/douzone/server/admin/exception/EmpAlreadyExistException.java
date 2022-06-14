package com.douzone.server.admin.exception;

public class EmpAlreadyExistException extends DshareServerException {
    public EmpAlreadyExistException(ErrorCode errorCode) {
        super(errorCode.EMP_ALREADY_EXIST);
    }
}
