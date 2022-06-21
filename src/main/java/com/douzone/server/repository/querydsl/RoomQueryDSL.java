package com.douzone.server.repository.querydsl;

import com.douzone.server.dto.room.QRoomBookmarkResDTO;
import com.douzone.server.dto.room.RoomBookmarkResDTO;
import com.douzone.server.dto.room.RoomReservationSearchDTO;
import com.douzone.server.entity.RoomReservation;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.douzone.server.entity.QEmployee.employee;
import static com.douzone.server.entity.QMeetingRoom.meetingRoom;
import static com.douzone.server.entity.QRoomBookmark.roomBookmark;
import static com.douzone.server.entity.QRoomReservation.roomReservation;

@Repository
@RequiredArgsConstructor
@Slf4j
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

	//select empId, roomId, createdAt, modifiedAt, count(roomId)
//				from room_bookmark group by roomId order by count(roomId) desc
//				limit 3;
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
				.groupBy(meetingRoom.id)
				.orderBy(roomBookmark.meetingRoom.id.count().desc())
				.limit(limit)
				.fetch();
	}

	/**
	 * 팀별/부서별/사원번호별/사원이름별 유저의 회의실 예약 조회 - 관리자
	 */
	private BooleanExpression teamIdEq(Long teamId) {
		return teamId != null ? roomReservation.employee.team.id.eq(teamId) : null;
	}

	private BooleanExpression deptIdEq(Long deptId) {
		return deptId != null ? employee.team.department.id.eq(deptId) : null;
	}

	private BooleanExpression positionIdEq(Long positionId) {
		return positionId != null ? employee.position.id.eq(positionId) : null;
	}

	private BooleanExpression empNoEq(String empNo) {
		return empNo != null ? roomReservation.employee.empNo.eq(empNo) : null;
	}

	private BooleanExpression empNameEq(String empName) {
		return empName != null ? roomReservation.employee.name.eq(empName) : null;
	}


	//	select *
//				from room_reservation rr
//				join employee e on rr.empId =e.id
//				join team t on e.teamId = t.id
//				join department d on t.deptId = d.id
//				where(
//						deptId = 1
//				);
	public List<RoomReservation> selectByVariousColumns(RoomReservationSearchDTO search) {

		return jpaQueryFactory
				.select(roomReservation)
				.from(roomReservation)
				.join(roomReservation.employee, employee).fetchJoin()
				.where(
						positionIdEq(search.getPositionId()),
						deptIdEq(search.getDeptId()),
						teamIdEq(search.getTeamId()),
						empNoEq(search.getEmpNo()),
						empNameEq(search.getEmpName())
				)
				.orderBy(roomReservation.modifiedAt.desc())
				.fetch();
	}


}
