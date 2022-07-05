package com.douzone.server.dto.room;

import com.douzone.server.entity.MeetingRoom;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomListResDTO {
	private Long roomId;
	private String content;
	private String categoryName;
	private Integer roomNo;
	private Integer capacity;
	private LocalDateTime modifiedAt;
	private List<String> imgList;

	public RoomListResDTO of(MeetingRoom room, List<String> imgList) {
		return RoomListResDTO.builder()
				.roomId(room.getId())
				.content(room.getContent())
				.categoryName(room.getCategoryName())
				.roomNo(room.getRoomNo())
				.capacity(room.getCapacity())
				.modifiedAt(room.getModifiedAt())
				.imgList(imgList)
				.build();
	}
}
