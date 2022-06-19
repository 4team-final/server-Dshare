package com.douzone.server.dto.room;


import com.douzone.server.entity.MeetingRoom;
import com.douzone.server.entity.RoomObject;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RoomObjectReqDTO {

	private long roomId;
	private String name;

	@Builder
	public RoomObjectReqDTO(long roomId, String name) {
		this.roomId = roomId;
		this.name = name;
	}

	public RoomObject of(long roomId, RoomObjectReqDTO roomObjectReqDTO) {
		return RoomObject.builder()
				.meetingRoom(MeetingRoom.builder().id(roomId).build())
				.name(roomObjectReqDTO.getName())
				.build();
	}
}
