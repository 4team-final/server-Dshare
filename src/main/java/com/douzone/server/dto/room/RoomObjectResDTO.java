package com.douzone.server.dto.room;

import com.douzone.server.entity.RoomObject;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RoomObjectResDTO {
	private Long objId;
	//	private RoomResDTO room;
	private String name;

	@Builder
	public RoomObjectResDTO(Long objId, RoomResDTO room, String name) {
		this.objId = objId;
//		this.room = room;
		this.name = name;
	}

	public RoomObjectResDTO of(RoomObject object) {
		return RoomObjectResDTO.builder()
				.objId(object.getId())
//				.room(RoomResDTO.builder().build().of(object.getMeetingRoom()))
				.name(object.getName())
				.build();
	}
}
