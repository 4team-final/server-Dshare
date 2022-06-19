package com.douzone.server.dto.room;

import com.douzone.server.entity.MeetingRoom;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RoomReqDTO {

	@NotBlank(message = "내용이 없습니다.")
	private String content;

	@NotBlank(message = "카테고리 내용이 없습니다.")
	private String categoryName;

	@NotNull(message = "호실이 없습니다.")
	private int roomNo;

	@NotNull(message = "인원수가 없습니다.")
	private int capacity;

	private List<RoomObjectReqDTO> roomObjects;

	@Builder
	public RoomReqDTO(String content, String categoryName, int roomNo, int capacity) {
		this.content = content;
		this.categoryName = categoryName;
		this.roomNo = roomNo;
		this.capacity = capacity;
	}

	public MeetingRoom of() {
		return MeetingRoom.builder()
				.content(content)
				.categoryName(categoryName)
				.roomNo(roomNo)
				.capacity(capacity)
				.build();
	}
}
