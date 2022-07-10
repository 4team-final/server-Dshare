package com.douzone.server.dto.room;

import com.douzone.server.entity.MeetingRoom;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<RoomObjectResDTO> roomObjectResDTOList = new ArrayList<>();

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<RoomImgResDTO> roomImgResDTOList = new ArrayList<>();

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private long count = 0;

	@Builder
	@QueryProjection
	public RoomResDTO(Long roomId, String content, String categoryName, Integer roomNo, Integer capacity, LocalDateTime modifiedAt, List<RoomObjectResDTO> roomObjectResDTOList, List<RoomImgResDTO> roomImgResDTOList, long count) {
		this.roomId = roomId;
		this.content = content;
		this.categoryName = categoryName;
		this.roomNo = roomNo;
		this.capacity = capacity;
		this.modifiedAt = modifiedAt;
		this.roomObjectResDTOList = roomObjectResDTOList;
		this.roomImgResDTOList = roomImgResDTOList;
		this.count = count;
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

	public RoomResDTO of(MeetingRoom room, List<RoomObjectResDTO> roomObjectResDTOList, List<RoomImgResDTO> roomImgResDTOList) {
		return RoomResDTO.builder()
				.roomId(room.getId())
				.content(room.getContent())
				.categoryName(room.getCategoryName())
				.roomNo(room.getRoomNo())
				.capacity(room.getCapacity())
				.modifiedAt(room.getModifiedAt())
				.roomObjectResDTOList(roomObjectResDTOList)
				.roomImgResDTOList(roomImgResDTOList)
				.build();
	}

	public RoomResDTO of(MeetingRoom room, List<RoomObjectResDTO> roomObjectResDTOList, List<RoomImgResDTO> roomImgResDTOList,long count) {
		return RoomResDTO.builder()
				.roomId(room.getId())
				.content(room.getContent())
				.categoryName(room.getCategoryName())
				.roomNo(room.getRoomNo())
				.capacity(room.getCapacity())
				.modifiedAt(room.getModifiedAt())
				.roomObjectResDTOList(roomObjectResDTOList)
				.roomImgResDTOList(roomImgResDTOList)
				.count(count)
				.build();
	}

}
