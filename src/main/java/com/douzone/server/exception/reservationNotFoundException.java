package com.douzone.server.exception;

public class reservationNotFoundException extends DshareServerException {
    public reservationNotFoundException(ErrorCode errorCode) {
        super(errorCode.RES_NOT_FOUND);
    }
}
