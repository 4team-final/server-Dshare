package com.douzone.server.config.utils;


import com.douzone.server.entity.MeetingRoom;
import com.douzone.server.entity.RoomImg;
import com.douzone.server.entity.Vehicle;
import com.douzone.server.entity.VehicleImg;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UploadDTO {

	private String path;
	private String type;
	private String imgSize;

	@Builder
	public UploadDTO(String path, String type, String imgSize) {
		this.path = path;
		this.type = type;
		this.imgSize = imgSize;

	}

	public RoomImg room_of(long roomId, UploadDTO uploadDTO) {
		return RoomImg.builder()
				.meetingRoom(MeetingRoom.builder().id(roomId).build())
				.path(uploadDTO.path)
				.type(uploadDTO.type)
				.imgSize(uploadDTO.imgSize)
				.build();
	}

	public VehicleImg vehicle_of(long vehicleId, UploadDTO uploadDTO) {
		return VehicleImg.builder()
				.vehicle(Vehicle.builder().id(vehicleId).build())
				.path(uploadDTO.path)
				.type(uploadDTO.type)
				.imgSize(uploadDTO.imgSize)
				.build();
	}

}
