package com.douzone.server.service;

//import com.douzone.server.dto.vehicle.VehicleRegisterDTO;
import com.douzone.server.dto.vehicle.VehicleReservationDTO;
import com.douzone.server.entity.Employee;
import com.douzone.server.entity.Vehicle;
import com.douzone.server.entity.VehicleBookmark;
import com.douzone.server.entity.VehicleReservation;
import com.douzone.server.repository.VehicleBookmarkRepository;
import com.douzone.server.repository.VehicleRepository;
import com.douzone.server.repository.VehicleReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class VehicleService {
	private static final String METHOD_NAME = "VehicleService";
	private final VehicleRepository vehicleRepository;
	private final VehicleReservationRepository vehicleReservationRepository;
	private final VehicleBookmarkRepository vehicleBookmarkRepository;

	public List<Vehicle> findAllReserved() {
		log.info(METHOD_NAME + "-findAllReserved");
		try {
			return vehicleRepository.findAllReserved();
		} catch (Exception e) {
			log.error("SERVER ERROR", e);
		}
		return null;
	}

	public List<Vehicle> findAllUnreserved() {
		log.info(METHOD_NAME + "-findAllReserved");
		try {
			return vehicleRepository.findAllUnreserved();
		} catch (Exception e) {
			log.error("SERVER ERROR", e);
		}
		return null;
	}

	public List<Vehicle> findTypeReserved(String model) {
		log.info(METHOD_NAME + "-findAllReserved");
		try {
			return vehicleRepository.findTypeReserved(model);
		} catch (Exception e) {
			log.error("SERVER ERROR", e);
		}
		return null;
	}

	public List<Vehicle> findDateReserved(Date date) {
		log.info(METHOD_NAME + "-findAllReserved");
		try {
			return vehicleRepository.findDateReserved(date);
		} catch (Exception e) {
			log.error("SERVER ERROR", e);
		}
		return null;
	}

	public Integer createReservation(VehicleReservationDTO vehicleReservationDTO) {
		log.info(METHOD_NAME + "-createReservation");
		try {
//			VehicleReservation vehicleReservation = VehicleReservation.builder()
//					.vehicle(Vehicle.builder().id(1L).build())
//					.employee(Employee.builder().id(1L).build())
//					.reason("사유")
//					.title("제목")
//					.build();
			VehicleReservation result = vehicleReservationRepository.save(vehicleReservationDTO);
			if(result.getId() == null) return 0;
			return 1;
		} catch (Exception e) {
			log.error("SERVER ERROR", e);
		}

		return null;
	}

	public Integer createBookmark( ) {
		log.info(METHOD_NAME + "-createBookmark");

		try {
			VehicleBookmark vehicleBookmark = VehicleBookmark.builder()
					.employee(Employee.builder().id(1L).build())
					.vehicle(Vehicle.builder().id(1L).build())
					.build();
			VehicleBookmark result = vehicleBookmarkRepository.save(vehicleBookmark);
			if(result.getId() == null) return 0;
			return 1;
		} catch (Exception e) {
			log.error("SERVER ERROR", e);
		}
		return null;
	}
}
