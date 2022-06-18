package com.douzone.server.dto.room;

import com.douzone.server.entity.Employee;
import com.douzone.server.entity.MeetingRoom;
import com.douzone.server.entity.RoomBookmark;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import javafx.scene.NodeBuilder;
import lombok.*;
import org.w3c.dom.stylesheets.LinkStyle;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter

@NoArgsConstructor
public class RoomBookmarkDTO {

	private long id;
	@JsonInclude(JsonInclude.Include.NON_DEFAULT)
	private long empId;
	@JsonInclude(JsonInclude.Include.NON_DEFAULT)
	private long roomId;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Employee employee;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private MeetingRoom meetingRoom;
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
	@Builder
	@QueryProjection
	public RoomBookmarkDTO(long id, Employee employee, MeetingRoom meetingRoom, LocalDateTime createdAt, LocalDateTime modifiedAt) {
		this.id=id;
		this.employee = employee;
		this.meetingRoom = meetingRoom;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
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
