package com.douzone.server.config.socket;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TimeMessage {

	public enum MessageType {
		ENTER, TALK
	}

	private MessageType type;
	private String uId;
	private String time;
	private int isSeat;
	private String empName;
	private String message;


}
