package com.douzone.server.exception;

public class WebsocketIOException extends DshareServerException {
	public WebsocketIOException(ErrorCode errorCode) {
		super(errorCode.SOCKET_NOT_CLOSE_ERROR);
	}
}
