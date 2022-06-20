package com.douzone.server.dto.room;

import com.douzone.server.entity.Employee;
import com.douzone.server.entity.MeetingRoom;
import com.douzone.server.entity.RoomBookmark;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter

@NoArgsConstructor
public class RoomBookmarkResDTO {

	private long id;
	@JsonInclude(JsonInclude.Include.NON_DEFAULT)
	private long empId;
	@JsonInclude(JsonInclude.Include.NON_DEFAULT)
	private long roomId;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;

	private long count = 0;

	@Builder
	@QueryProjection
	public RoomBookmarkResDTO(long id, long empId, long roomId, LocalDateTime createdAt, LocalDateTime modifiedAt, long count) {
		this.id = id;
		this.empId = empId;
		this.roomId = roomId;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
		this.count = count;
	}

	@Builder
	@QueryProjection
	public RoomBookmarkResDTO(long id, long empId, long roomId, LocalDateTime createdAt, LocalDateTime modifiedAt) {
		this.id = id;
		this.empId = empId;
		this.roomId = roomId;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
	}




	public RoomBookmarkResDTO of(RoomBookmark roomBookmark) {
		return RoomBookmarkResDTO.builder()
				.id(roomBookmark.getId())
				.empId(roomBookmark.getEmployee().getId())
				.roomId(roomBookmark.getMeetingRoom().getId())
				.createdAt(roomBookmark.getCreatedAt())
				.modifiedAt(roomBookmark.getModifiedAt())
				.build();
	}


}
