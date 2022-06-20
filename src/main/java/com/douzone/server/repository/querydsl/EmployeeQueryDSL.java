package com.douzone.server.repository.querydsl;

import com.douzone.server.dto.room.QRoomBookmarkResDTO;
import com.douzone.server.dto.room.RoomBookmarkResDTO;
import com.douzone.server.entity.Employee;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.douzone.server.entity.QEmployee.employee;
import static com.douzone.server.entity.QPosition.position;
import static com.douzone.server.entity.QRoomBookmark.roomBookmark;
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

	public List<RoomBookmarkResDTO> selectByMyBookmark(int empNo) {
		return jpaQueryFactory
				.select(new QRoomBookmarkResDTO(
						roomBookmark.id,
						roomBookmark.employee.id.as("empId"),
						roomBookmark.meetingRoom.id.as("roomId"),
						roomBookmark.createdAt,
						roomBookmark.modifiedAt
				))
				.from(roomBookmark)
				.where(roomBookmark.employee.empNo.eq(String.valueOf(empNo)))
				.fetch();
	}
}