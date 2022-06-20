package com.douzone.server.dto.room;

import com.douzone.server.entity.MeetingRoom;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class RoomResDTO {
	private Long roomId;
	private String content;
	private String categoryName;
	private Integer roomNo;
	private Integer capacity;
	private LocalDateTime modifiedAt;

	@Builder
	public RoomResDTO(Long roomId, String content, String categoryName, Integer roomNo, Integer capacity, LocalDateTime modifiedAt) {
		this.roomId = roomId;
		this.content = content;
		this.categoryName = categoryName;
		this.roomNo = roomNo;
		this.capacity = capacity;
		this.modifiedAt = modifiedAt;
	}

	public RoomResDTO of(MeetingRoom room) {
		return RoomResDTO.builder()
				.roomId(room.getId())
				.content(room.getContent())
				.categoryName(room.getCategoryName())
				.roomNo(room.getRoomNo())
				.capacity(room.getCapacity())
				.modifiedAt(room.getModifiedAt())
				.build();
	}
}
