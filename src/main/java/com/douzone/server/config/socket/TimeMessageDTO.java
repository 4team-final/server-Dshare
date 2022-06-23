package com.douzone.server.config.socket;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TimeMessageDTO {
	public enum MessageType {
		ENTER, TALK, QUIT
	}

	private MessageType type;
	private String uid;
	private String time;
	private Integer isSeat;
	private String empNo;
	private String message;
}