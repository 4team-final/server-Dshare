package com.douzone.server.entity;

import com.douzone.server.config.utils.BaseAtTime;
import com.douzone.server.dto.vehicle.VehicleImgDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "vehicle_img")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleImg extends BaseAtTime {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vehicleId")
	private Vehicle vehicle;

	private String path;
	private String type;
	private String imgSize;

	public void updateVehicleImg(VehicleImgDTO vehicleImgDTO) {
		this.vehicle = Vehicle.builder().id(vehicleImgDTO.getVehicleId()).build();
		this.path = vehicleImgDTO.getPath();
		this.type = vehicleImgDTO.getType();
		this.imgSize = vehicleImgDTO.getImgSize();
	}
}
