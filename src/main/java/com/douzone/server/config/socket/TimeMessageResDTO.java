package com.douzone.server.config.socket;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TimeMessageResDTO {
	public enum MessageType {
		ENTER, TALK, QUIT
	}

	private MessageType type;
	private String uid;
	private String time;
	private Integer isSeat;
	private String empNo;
	private Integer roomId;

	@Builder
	public TimeMessageResDTO(MessageType type, String uid, String time, Integer isSeat, String empNo, Integer roomId) {
		this.type = type;
		this.uid = uid;
		this.time = time;
		this.isSeat = isSeat;
		this.empNo = empNo;
		this.roomId = roomId;
	}

	public TimeMessageResDTO of(Time time) {
		return TimeMessageResDTO.builder()
				.type(MessageType.ENTER)
				.uid(time.getCalendar().getUid())
				.time(time.getTime())
				.isSeat(time.getIsSeat())
				.empNo(time.getEmpNo())
				.roomId(time.getRoomId())
				.build();
	}

}