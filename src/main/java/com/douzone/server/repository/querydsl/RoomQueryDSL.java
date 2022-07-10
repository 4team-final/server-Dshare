package com.douzone.server.repository.querydsl;

import com.douzone.server.dto.room.*;
import com.douzone.server.entity.QMeetingRoom;
import com.douzone.server.entity.RoomReservation;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.douzone.server.entity.QEmployee.employee;
import static com.douzone.server.entity.QMeetingRoom.meetingRoom;
import static com.douzone.server.entity.QRoomBookmark.roomBookmark;
import static com.douzone.server.entity.QRoomReservation.roomReservation;
import static com.douzone.server.entity.QVehicleReservation.vehicleReservation;

@Repository
@RequiredArgsConstructor
@Slf4j
public class RoomQueryDSL {
	private final JPAQueryFactory jpaQueryFactory;

	/**
	 * 전체 회의실 예약 조회 - 페이징
	 * 역순 정렬 후
	 * lastId(100) -> limit(10)개 조회
	 * lastId(90) -> limit(10)개 조회
	 * 마지막 조회 Id를 받아온다. lastId
	 */
	public List<RoomReservation> selectAllReservation() {
		return jpaQueryFactory
				.select(roomReservation)
				.from(roomReservation)
				.join(roomReservation.meetingRoom, meetingRoom).fetchJoin()
				.orderBy(roomReservation.modifiedAt.desc(), roomReservation.id.desc())
				.fetch();
	}

	public List<RoomReservation> selectAllReservationPage(long lastId, int limit) {
		List<RoomReservation> roomList = jpaQueryFactory
				.select(roomReservation)
				.from(roomReservation)
				.join(roomReservation.meetingRoom, meetingRoom).fetchJoin()
				.where(roomReservationIdLt(lastId))
				.orderBy(roomReservation.modifiedAt.desc(), roomReservation.id.desc())//플젝 시작하면 앞에 createdAt정렬을 먼저 해줘야함
				.limit(limit)
				.fetch();

		return roomList;
	}

	public List<RoomReservation> selectAllReservationPage2(long page, int limit) {
		List<RoomReservation> roomList = jpaQueryFactory
				.select(roomReservation)
				.from(roomReservation)
				.join(roomReservation.meetingRoom, meetingRoom).fetchJoin()
				.orderBy(roomReservation.modifiedAt.desc(), roomReservation.id.desc())//플젝 시작하면 앞에 createdAt정렬을 먼저 해줘야함
				.limit(limit)
				.offset((page - 1) * limit)
				.fetch();
		return roomList;
	}

	//내거 회의실 예약 조회
	public List<RoomReservation> selectAllReservationPage(long lastId, int limit, long Id) {
		List<RoomReservation> roomList = jpaQueryFactory
				.select(roomReservation)
				.from(roomReservation)
				.join(roomReservation.meetingRoom, meetingRoom).fetchJoin()
				.join(roomReservation.employee, employee).fetchJoin()
				.where(employee.id.eq(Id), roomReservationIdLt(lastId))
				.orderBy(roomReservation.modifiedAt.desc(), roomReservation.id.asc())
				.limit(limit)
				.fetch();
		return roomList;
	}

	//만약 아무것도 조회 안한 첫 시작이면 null처리돼서 마지막부터 limit개 보여주기
	private BooleanExpression roomReservationIdLt(long lastId) {
		return lastId != 0 ? roomReservation.id.lt(lastId) : null;
	}

	private BooleanExpression roomReservationStartedAtGt(String startedAt) {
		return startedAt != "" ? roomReservation.startedAt.gt(LocalDateTime.parse(startedAt)) : null;
	}

	private BooleanExpression roomReservationCreatedAtLt(String createdAt) {
		return createdAt != "" ? roomReservation.createdAt.lt(LocalDateTime.parse(createdAt)) : null;
	}

	public long countReservation() {
		return jpaQueryFactory.select(roomReservation).from(roomReservation).stream().count();
	}

	//내거 카운트 조회
	public long countReservation(long Id) {
		return jpaQueryFactory.select(roomReservation).from(roomReservation).where(roomReservation.employee.id.eq(Id)).stream().count();
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

	public List<RoomCountResDTO> selectDateTimeReservation2(String startTime, String endTime) {
		return jpaQueryFactory
				.select(new QRoomCountResDTO(roomReservation.id,
						roomReservation.meetingRoom.roomNo,
						roomReservation.startedAt.month().as("month"),
						roomReservation.startedAt.dayOfMonth().as("day"),
						roomReservation.meetingRoom.roomNo.count().as("count"),
						roomReservation.reason,
						roomReservation.title,
						roomReservation.startedAt,
						roomReservation.endedAt
				))
				.from(roomReservation)
				.join(roomReservation.meetingRoom, meetingRoom)
				.where(roomReservation.startedAt.goe(LocalDateTime.parse(startTime))
						.and(roomReservation.endedAt.loe(LocalDateTime.parse(endTime))))
				.groupBy(meetingRoom.roomNo, roomReservation.startedAt.dayOfMonth())
				.orderBy(roomReservation.startedAt.asc())
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

	private BooleanExpression empNameContain(String empName) {
		return empName != null ? roomReservation
				.employee.name.contains(empName) : null;
	}

	//시간 검색 추가
	private BooleanExpression startAtEq(String startedAt) {
		if (startedAt != null) {
			LocalDateTime test = LocalDateTime.parse(startedAt, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			return roomReservation.startedAt.after(test);
		} else return null;
	}

	private BooleanExpression endedAtEq(String endedAt) {
		if (endedAt != null) {
			return roomReservation.endedAt.before(LocalDateTime.parse(endedAt, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		} else return null;
	}


	public List<RoomReservation> selectByVariousColumns(RoomReservationSearchDTO search, long page, int limit) {
		Long deptId = search.getDeptId();
		return jpaQueryFactory
				.select(roomReservation)
				.from(roomReservation)
				.join(roomReservation.employee, employee).fetchJoin()
				.join(roomReservation.meetingRoom, meetingRoom).fetchJoin()
				.where(
						positionIdEq(search.getPositionId()),
						deptIdEq(search.getDeptId()),
						teamIdEq(search.getTeamId()),
						empNoEq(search.getEmpNo()),
						empNameContain(search.getEmpName()),
						startAtEq(search.getStartedAt()),
						endedAtEq(search.getEndedAt())
				)
				.orderBy(roomReservation.modifiedAt.desc(), roomReservation.id.desc())
				.limit(limit)
				.offset((page-1)*limit)
				.fetch();
	}
//	이것의 카운트
	public long countReservation(RoomReservationSearchDTO search) {
		return jpaQueryFactory.select(roomReservation).from(roomReservation)
				.where(
					positionIdEq(search.getPositionId()),
					deptIdEq(search.getDeptId()),
					teamIdEq(search.getTeamId()),
					empNoEq(search.getEmpNo()),
						empNameContain(search.getEmpName()),
						startAtEq(search.getStartedAt()),
						endedAtEq(search.getEndedAt())
				)
				.stream().count();
	}

//	public List<RoomReservation> selectAllReservationPage2(long page, int limit){
//		List<RoomReservation> roomList = jpaQueryFactory
//				.select(roomReservation)
//				.from(roomReservation)
//				.join(roomReservation.meetingRoom, meetingRoom).fetchJoin()
//				.orderBy(roomReservation.modifiedAt.desc(),roomReservation.id.desc())//플젝 시작하면 앞에 createdAt정렬을 먼저 해줘야함
//				.limit(limit)
//				.offset((page-1)*limit)
//				.fetch();
//		return roomList;
//	}
}
