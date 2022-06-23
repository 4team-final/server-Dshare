package com.douzone.server.config.socket;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TestMessage {

	public enum MessageType {
		ENTER, TALK
	}

	private MessageType type;

	private String message;


}
