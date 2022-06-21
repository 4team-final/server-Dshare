package com.douzone.server.repository.querydsl;

import com.douzone.server.dto.room.RoomReservationSearchDTO;
import com.douzone.server.entity.Employee;
import com.douzone.server.entity.MeetingRoom;
import com.douzone.server.entity.RoomReservation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class RoomQueryDSLTest {
	@Autowired
	RoomQueryDSL roomQueryDSL;

	@Test
	public void 전체회의실조회() {

		//given
		//더미데이터 활용, 임의로 예약 객체를 불러왔다고 가정
		//가장 첫번쨰는 6번 예약(더미데이터기준)
		RoomReservation roomReservation = RoomReservation.builder()
				.id(6L)
				.meetingRoom(MeetingRoom.builder().id(3L).build())
				.employee(Employee.builder().id(19L).build())
				.reason("마케팅부 정기회의")
				.title("마케팅부 정기회의")
				.build();
		//when
		List<RoomReservation> roomReservations = roomQueryDSL.selectAllReservation();

		//then
		assertThat(roomReservations.get(0).getId()).isEqualTo(roomReservation.getId());//6
		assertThat(roomReservations.get(0).getReason()).isEqualTo(roomReservation.getReason());//마케팅부 정기회의
		assertThat(roomReservations.get(0).getMeetingRoom().getId()).isEqualTo(roomReservation.getMeetingRoom().getId());//3
		assertThat(roomReservations.get(0).getEmployee().getId()).isEqualTo(roomReservation.getEmployee().getId());//19
	}

	@Test
	public void 회의실호수에따른예약조회() {
		//given
		RoomReservation roomReservation = RoomReservation.builder()
				.id(6L)
				.meetingRoom(MeetingRoom.builder().id(3L).build())
				.employee(Employee.builder().id(19L).build())
				.reason("마케팅부 정기회의")
				.title("마케팅부 정기회의")
				.build();
		//when
//		List<RoomReservation> roomReservations = roomQueryDSL.selectRoomNoElseCapacityReservation(RoomReservationSearchDTO.builder()
//				.roomNo(103).build());

		//then
//		assertThat(roomReservations.get(0).getId()).isEqualTo(roomReservation.getId());//6
//		assertThat(roomReservations.get(0).getReason()).isEqualTo(roomReservation.getReason());//마케팅부 정기회의
//		assertThat(roomReservations.get(0).getMeetingRoom().getId()).isEqualTo(roomReservation.getMeetingRoom().getId());//3
//		assertThat(roomReservations.get(0).getEmployee().getId()).isEqualTo(roomReservation.getEmployee().getId());//19

	}
	@Test
	public void 동적쿼리() {
		RoomReservationSearchDTO search = RoomReservationSearchDTO.builder().deptId(1L).build();
		roomQueryDSL.selectByVariousColumns(search);
	}

}