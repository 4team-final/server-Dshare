package com.douzone.server.config.socket.vehicle;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude
@ToString
public class VehicleSocketResDTO {
	public enum MessageType {
		ENTER, TALK, DUAL, QUIT
	}

	private VehicleSocketDTO.MessageType type;
	private Long vehicleId;
	private String uid;
	private String time;
	private Integer isSeat;
	private String empNo;

	@Builder
	public VehicleSocketResDTO(VehicleSocketDTO.MessageType type, Long vehicleId, String uid, String time, Integer isSeat, String empNo) {
		this.type = type;
		this.vehicleId = vehicleId;
		this.uid = uid;
		this.time = time;
		this.isSeat = isSeat;
		this.empNo = empNo;
	}

	public VehicleSocketResDTO of(TimeVehicle timeVehicle) {
		return VehicleSocketResDTO.builder()
				.type(VehicleSocketDTO.MessageType.ENTER)
				.vehicleId(timeVehicle.getVehicleId())
				.uid(timeVehicle.getCalendar().getUid())
				.time(timeVehicle.getTime())
				.isSeat(timeVehicle.getIsSeat())
				.empNo(timeVehicle.getEmpNo())
				.build();
	}
}
