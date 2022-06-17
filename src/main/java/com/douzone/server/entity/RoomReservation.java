package com.douzone.server.entity;

import com.douzone.server.config.utils.BaseAtTime;
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
}
