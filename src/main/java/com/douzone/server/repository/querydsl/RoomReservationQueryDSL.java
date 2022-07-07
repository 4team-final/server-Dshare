package com.douzone.server.repository.querydsl;


import com.douzone.server.dto.reservation.QWeekCountHourResDTO;
import com.douzone.server.dto.reservation.WeekCountHourResDTO;
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
		return jpaQueryFactory.select(roomReservation)
				.from(roomReservation)
				.innerJoin(roomReservation.meetingRoom, meetingRoom).fetchJoin()
				.innerJoin(roomReservation.employee, employee).fetchJoin()
				.where(roomReservation.startedAt.gt(LocalDateTime.now())).orderBy(roomReservation.modifiedAt.desc()).limit(limit).fetch();
	}

	public LocalDateTime findBySoonStartTime(LocalDateTime now, long empId) {
		return jpaQueryFactory.select(roomReservation.startedAt)
				.from(roomReservation).innerJoin(roomReservation.employee, employee)
				.where(roomReservation.startedAt.gt(now).and(employee.id.eq(empId))).orderBy(roomReservation.startedAt.asc()).limit(1).fetchOne();
	}

	public LocalDateTime findByIngEndTime(LocalDateTime now, long empId) {
		return jpaQueryFactory.select(roomReservation.endedAt)
				.from(roomReservation).innerJoin(roomReservation.employee, employee)
				.where(roomReservation.startedAt.lt(now).and(roomReservation.endedAt.gt(now)).and(employee.id.eq(empId))).orderBy(roomReservation.startedAt.asc()).limit(1).fetchOne();
	}

	public List<RoomReservation> findByBeforeReservation(Long empId) {
		return jpaQueryFactory.select(roomReservation)
				.from(roomReservation).innerJoin(roomReservation.meetingRoom, meetingRoom).fetchJoin().innerJoin(roomReservation.employee, employee).fetchJoin()
				.where(roomReservation.startedAt.gt(LocalDateTime.now()).and(employee.id.eq(empId))).orderBy(roomReservation.modifiedAt.desc()).fetch();
	}

	public List<RoomReservation> findByAfterReservation(Long empId) {
		return jpaQueryFactory.select(roomReservation)
				.from(roomReservation).innerJoin(roomReservation.meetingRoom, meetingRoom).fetchJoin().innerJoin(roomReservation.employee, employee).fetchJoin()
				.where(roomReservation.startedAt.lt(LocalDateTime.now()).and(employee.id.eq(empId))).orderBy(roomReservation.modifiedAt.desc()).fetch();
	}

	public List<WeekCountHourResDTO> findByWeekAndMonthReservationCount(LocalDateTime now, LocalDateTime nowMinusWeek) {
		return jpaQueryFactory.select(new QWeekCountHourResDTO(
						meetingRoom.id.as("roomId"),
						meetingRoom.content,
						meetingRoom.categoryName,
						meetingRoom.roomNo,
						meetingRoom.capacity,
						meetingRoom.modifiedAt,
						meetingRoom.id.count().as("count")
				))
				.from(roomReservation).innerJoin(roomReservation.meetingRoom, meetingRoom)
				.where(roomReservation.modifiedAt.lt(now).and(roomReservation.modifiedAt.gt(nowMinusWeek)))
				.groupBy(meetingRoom.id)
				.orderBy(meetingRoom.id.count().desc()).fetch();
	}

	public List<RoomReservation> findByMeetingRoomIdAndWeek(Long roomId, LocalDateTime now, LocalDateTime nowMinusWeek) {
		return jpaQueryFactory.select(roomReservation).from(roomReservation).innerJoin(roomReservation.meetingRoom, meetingRoom).fetchJoin().innerJoin(roomReservation.employee, employee).fetchJoin()
				.where(roomReservation.meetingRoom.id.eq(roomId).and(roomReservation.modifiedAt.lt(now).and(roomReservation.modifiedAt.gt(nowMinusWeek))))
				.orderBy(roomReservation.modifiedAt.desc()).fetch();
	}

	public List<WeekCountHourResDTO> findByWeekAndMonthReservationCountHour(LocalDateTime now, LocalDateTime nowMinusWeek) {
		return jpaQueryFactory.select(
						new QWeekCountHourResDTO(
								meetingRoom.id.as("roomId"),
								meetingRoom.content,
								meetingRoom.categoryName,
								meetingRoom.roomNo,
								meetingRoom.capacity,
								meetingRoom.modifiedAt,
								roomReservation.modifiedAt.hour().count().as("count"),
								roomReservation.modifiedAt.hour().as("hour")
						))
				.from(roomReservation).innerJoin(roomReservation.meetingRoom, meetingRoom)
				.where(roomReservation.modifiedAt.lt(now).and(roomReservation.modifiedAt.gt(nowMinusWeek)))
				.groupBy(roomReservation.modifiedAt.hour(),meetingRoom.id)
				.orderBy(roomReservation.modifiedAt.hour().asc()).fetch();
	}

	public List<WeekCountHourResDTO> findByWeekAndMonthMeetingCountHour(LocalDateTime now, LocalDateTime nowMinusWeek) {
		return jpaQueryFactory.select(
						new QWeekCountHourResDTO(
								meetingRoom.id.as("roomId"),
								meetingRoom.content,
								meetingRoom.categoryName,
								meetingRoom.roomNo,
								meetingRoom.capacity,
								meetingRoom.modifiedAt,
								roomReservation.startedAt.hour().count().as("count"),
								roomReservation.startedAt.hour().as("hour")
						))
				.from(roomReservation).innerJoin(roomReservation.meetingRoom, meetingRoom)
				.where(roomReservation.startedAt.lt(now).and(roomReservation.startedAt.gt(nowMinusWeek)))
				.groupBy(roomReservation.startedAt.hour(),meetingRoom.id)
				.orderBy(roomReservation.startedAt.hour().asc()).fetch();
	}
}
