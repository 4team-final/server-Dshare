


package com.douzone.server.repository.querydsl;

import com.douzone.server.dto.room.QRoomBookmarkDTO;
import com.douzone.server.dto.room.RoomBookmarkDTO;
import com.douzone.server.entity.Employee;
import com.douzone.server.entity.RoomReservation;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.List;

import static com.douzone.server.entity.QEmployee.employee;
import static com.douzone.server.entity.QMeetingRoom.meetingRoom;
import static com.douzone.server.entity.QPosition.position;
import static com.douzone.server.entity.QRoomBookmark.roomBookmark;
import static com.douzone.server.entity.QRoomReservation.roomReservation;
import static com.douzone.server.entity.QTeam.team;


@Repository
@RequiredArgsConstructor
public class EmployeeQueryDSL {
	private final JPAQueryFactory jpaQueryFactory;

	public List<Employee> findEmployeeList(long positionId) {
		return jpaQueryFactory.select(employee)
				.from(employee).leftJoin(employee.position).fetchJoin()
				.where(employee.position.id.eq(positionId)).fetch();
	}

	public List<Employee> findMyProfile(long id) {
		return jpaQueryFactory
				.select(employee).from(employee)
				.join(employee.position, position).fetchJoin()
				.join(employee.team, team).fetchJoin()
				.where(employee.id.eq(id))
				.fetch();
	}
	public List<RoomBookmarkDTO> selectByMyBookmark(int empNo) {
		return jpaQueryFactory
				.select(new QRoomBookmarkDTO(
						roomBookmark.id,
						roomBookmark.employee.as("employee"),
						roomBookmark.meetingRoom.as("meetingRoom"),
						roomBookmark.createdAt,
						roomBookmark.modifiedAt
				))
				.from(roomBookmark)
				.where(roomBookmark.employee.empNo.eq(String.valueOf(empNo)))
				.fetch();
	}




}