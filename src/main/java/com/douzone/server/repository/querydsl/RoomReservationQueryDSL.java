package com.douzone.server.repository.querydsl;

import com.douzone.server.entity.RoomReservation;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.douzone.server.entity.QEmployee.employee;
import static com.douzone.server.entity.QRoomReservation.roomReservation;
import static com.douzone.server.entity.QTeam.team;

@Repository
@RequiredArgsConstructor
public class RoomReservationQueryDSL {
	private final JPAQueryFactory jpaQueryFactory;

	public List<RoomReservation> findRecentReservation(int limit) {
		return jpaQueryFactory.select(roomReservation)
				.from(roomReservation)
				.innerJoin(roomReservation.meetingRoom).fetchJoin()
				.innerJoin(roomReservation.employee).fetchJoin()
				.innerJoin(employee.position).fetchJoin()
				.innerJoin(employee.team).fetchJoin()
				.innerJoin(team.dept).fetchJoin()
				.where(roomReservation.startedAt.gt(LocalDateTime.now()))
				.orderBy(roomReservation.modifiedAt.desc())
				.limit(limit)
				.fetch();
	}
}
