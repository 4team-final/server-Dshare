package com.douzone.server.repository.querydsl;

import com.douzone.server.entity.RoomReservation;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.douzone.server.entity.QEmployee.employee;
import static com.douzone.server.entity.QMeetingRoom.meetingRoom;
import static com.douzone.server.entity.QRoomReservation.roomReservation;

@Repository
@RequiredArgsConstructor
public class RoomReservationQueryDSL {
	private final JPAQueryFactory jpaQueryFactory;

	public List<RoomReservation> findRecentReservation(int limit) {
		return jpaQueryFactory.select(roomReservation).from(roomReservation).innerJoin(roomReservation.meetingRoom, meetingRoom).fetchJoin().innerJoin(roomReservation.employee, employee).fetchJoin().where(roomReservation.startedAt.gt(LocalDateTime.now())).orderBy(roomReservation.modifiedAt.desc()).limit(limit).fetch();
	}

	public LocalDateTime findByUserSoonReservationTime(LocalDateTime now, long empId) {
		return jpaQueryFactory.select(roomReservation.startedAt)
				.from(roomReservation).innerJoin(roomReservation.employee, employee)
				.where(roomReservation.startedAt.gt(now).and(employee.id.eq(empId))).orderBy(roomReservation.startedAt.asc()).limit(1).fetchOne();
	}

	public LocalDateTime findByUserEndReservationTime(LocalDateTime now, long empId) {
		return jpaQueryFactory.select(roomReservation.endedAt)
				.from(roomReservation).innerJoin(roomReservation.employee, employee)
				.on(employee.id.eq(empId))
				.where(roomReservation.startedAt.lt(now).and(roomReservation.endedAt.gt(now)).and(employee.id.eq(empId))).orderBy(roomReservation.startedAt.asc()).limit(1).fetchOne();
	}

}
