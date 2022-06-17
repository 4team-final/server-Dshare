package com.douzone.server.exception;

public class ImgFileNotFoundException extends DshareServerException {
    public ImgFileNotFoundException(ErrorCode errorCode) {
        super(ErrorCode.IMG_NOT_FOUND);
    }
}
