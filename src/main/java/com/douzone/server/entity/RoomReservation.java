package com.douzone.server.entity;

import com.douzone.server.config.utils.BaseAtTime;
import com.douzone.server.dto.reservation.RegistReservationReqDto;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "room_reservation")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RoomReservation extends BaseAtTime {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "roomId")
	private MeetingRoom meetingRoom;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "empId")
	private Employee employee;

	private String reason;
	private String title;
	private LocalDateTime startedAt;
	private LocalDateTime endedAt;

	public void updateReservation(long roomId, String reason, String title, LocalDateTime startedAt, LocalDateTime endedAt) {
		this.meetingRoom.builder().id(roomId).build();
		this.reason = reason;
		this.title = title;
		this.startedAt =startedAt;
		this.endedAt = endedAt;
	}

	public RoomReservation of(RegistReservationReqDto registReservationReqDto) {
		return RoomReservation.builder()
				.meetingRoom(MeetingRoom.builder().id(registReservationReqDto.getRoomId()).build())
				.employee(Employee.builder().id(registReservationReqDto.getEmpId()).build())
				.reason(registReservationReqDto.getReason())
				.title(registReservationReqDto.getTitle())
				.startedAt(registReservationReqDto.getStartedAt())
				.endedAt(registReservationReqDto.getEndedAt())
				.build();
	}
	
}
