package com.douzone.server.repository.querydsl;

import com.douzone.server.dto.room.QRoomBookmarkResDTO;
import com.douzone.server.dto.room.RoomBookmarkResDTO;
import com.douzone.server.dto.room.RoomReservationSearchDTO;
import com.douzone.server.entity.RoomReservation;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.douzone.server.entity.QMeetingRoom.meetingRoom;
import static com.douzone.server.entity.QRoomBookmark.roomBookmark;
import static com.douzone.server.entity.QRoomReservation.roomReservation;

@Repository
@RequiredArgsConstructor
public class RoomQueryDSL {
	private final JPAQueryFactory jpaQueryFactory;

	/**
	 * 전체 회의실 예약 조회
	 *
	 * @return
	 */
	public List<RoomReservation> selectAllReservation() {
		return jpaQueryFactory
				.select(roomReservation)
				.from(roomReservation)
				.join(roomReservation.meetingRoom, meetingRoom).fetchJoin()
				.orderBy(roomReservation.modifiedAt.desc())
				.fetch();
	}

	private BooleanExpression roomNoEq(Integer roomNo) {
		return roomNo != null ? roomReservation.meetingRoom.roomNo.eq(roomNo) : null;
	}

	private BooleanExpression capacityEq(Integer capacity) {
		return capacity != null ? roomReservation.meetingRoom.capacity.eq(capacity) : null;
	}

	private BooleanExpression startedAt_endedAt(String startedAt, String endedAt) {
		if (startedAt.equals("")) {
			startedAt = null;
		}
		if (endedAt.equals("")) {
			endedAt = null;
		}

		return (startedAt != null && endedAt != null) ?
				roomReservation.startedAt.goe(LocalDateTime.parse(startedAt)).and(roomReservation.endedAt.loe(LocalDateTime.parse(endedAt))) : null;
	}

	public List<RoomReservation> selectByRoomNoElseCapacityElseReservation(RoomReservationSearchDTO search) {

		return jpaQueryFactory
				.select(roomReservation)
				.from(roomReservation)
				.join(roomReservation.meetingRoom, meetingRoom).fetchJoin()
				.where(
						roomNoEq(search.getRoomNo()),
						capacityEq(search.getCapacity()),
						startedAt_endedAt(search.getStartedAt(), search.getEndedAt())
				)
				.orderBy(roomReservation.modifiedAt.desc())
				.fetch();
	}

	public List<RoomReservation> selectDateTimeReservation(String startTime, String endTime) {
		return jpaQueryFactory
				.select(roomReservation)
				.from(roomReservation)
				.join(roomReservation.meetingRoom, meetingRoom).fetchJoin()
				.where(roomReservation.startedAt.goe(LocalDateTime.parse(startTime))
						.and(roomReservation.endedAt.loe(LocalDateTime.parse(endTime))))
				.orderBy(roomReservation.modifiedAt.desc())
				.fetch();
	}

	public List<RoomBookmarkResDTO> selectTop3BookmarkMeetingRoom(long limit) {
		return jpaQueryFactory
				.select(new QRoomBookmarkResDTO(
						roomBookmark.id,
						roomBookmark.employee.id.as("empId"),
						roomBookmark.meetingRoom.id.as("roomId"),
						roomBookmark.createdAt,
						roomBookmark.modifiedAt,
						roomBookmark.meetingRoom.id.count().as("count")
				))
				.from(roomBookmark)
				.join(roomBookmark.meetingRoom, meetingRoom)
				.groupBy(meetingRoom.id)
				.orderBy(roomBookmark.meetingRoom.count().desc())
				.limit(limit)
				.fetch();
	}


}
