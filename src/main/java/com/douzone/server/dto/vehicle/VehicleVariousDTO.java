package com.douzone.server.dto.vehicle;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class VehicleVariousDTO {
	private Long id, vId;
	private String reason, title, vName, vNumber, model, color, empNo, eName, email, tel, profileImg, team, position;
	private LocalDateTime startedAt, endedAt, createdAt, modifiedAt, birthday;
	private Integer capacity;
	private List<String> imgList;

	@Builder
	@QueryProjection
	public VehicleVariousDTO(Long id, Long vId, String reason, String title, String vName, String vNumber, String model, String color,
							 String empNo, String eName, String email, String tel, String profileImg, String team, String position,
							 LocalDateTime startedAt, LocalDateTime endedAt, LocalDateTime createdAt, LocalDateTime modifiedAt, LocalDateTime birthday, Integer capacity) {
		this.id = id;
		this.reason = reason;
		this.title = title;
		this.vId = vId;
		this.vName = vName;
		this.vNumber = vNumber;
		this.model = model;
		this.color = color;
		this.empNo = empNo;
		this.eName = eName;
		this.email = email;
		this.tel = tel;
		this.profileImg = profileImg;
		this.team = team;
		this.position = position;
		this.startedAt = startedAt;
		this.endedAt = endedAt;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
		this.birthday = birthday;
		this.capacity = capacity;
	}
}
