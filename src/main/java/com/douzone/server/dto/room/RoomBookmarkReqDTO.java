package com.douzone.server.dto.room;


import com.douzone.server.entity.Employee;
import com.douzone.server.entity.MeetingRoom;
import com.douzone.server.entity.RoomBookmark;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RoomBookmarkReqDTO {

	private long empId;
	private long roomId;

	public RoomBookmark of() {
		return RoomBookmark.builder()
				.employee(Employee.builder().id(empId).build())
				.meetingRoom(MeetingRoom.builder().id(roomId).build())
				.build();
	}
}
