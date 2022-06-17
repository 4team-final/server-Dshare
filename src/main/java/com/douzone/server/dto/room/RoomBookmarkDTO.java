package com.douzone.server.dto.room;

import com.douzone.server.entity.Employee;
import com.douzone.server.entity.MeetingRoom;
import com.douzone.server.entity.RoomBookmark;
import com.querydsl.core.annotations.QueryProjection;
import javafx.scene.NodeBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.w3c.dom.stylesheets.LinkStyle;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter

@NoArgsConstructor
public class RoomBookmarkDTO {

	private long id;
	private long empId;
	private long roomId;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;

	private long count =0;

	@Builder
	@QueryProjection
	public RoomBookmarkDTO(long id, long empId, long roomId, LocalDateTime createdAt, LocalDateTime modifiedAt, long count) {
		this.id = id;
		this.empId = empId;
		this.roomId = roomId;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
		this.count = count;
	}

	public RoomBookmarkDTO of(RoomBookmark roomBookmark) {
		return RoomBookmarkDTO.builder()
				.id(roomBookmark.getId())
				.empId(roomBookmark.getEmployee().getId())
				.roomId(roomBookmark.getMeetingRoom().getId())
				.createdAt(roomBookmark.getCreatedAt())
				.modifiedAt(roomBookmark.getModifiedAt())
				.build();
	}



}
