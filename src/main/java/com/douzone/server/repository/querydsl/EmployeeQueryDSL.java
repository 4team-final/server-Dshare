


package com.douzone.server.repository.querydsl;

import com.douzone.server.entity.Employee;
import com.douzone.server.entity.MeetingRoom;
import com.douzone.server.entity.QRoomReservation;
import com.douzone.server.entity.RoomReservation;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.douzone.server.entity.QEmployee.employee;
import static com.douzone.server.entity.QMeetingRoom.meetingRoom;
import static com.douzone.server.entity.QRoomReservation.roomReservation;
import static com.douzone.server.entity.QPosition.position;
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




}