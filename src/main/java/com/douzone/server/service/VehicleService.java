package com.douzone.server.service;

import com.douzone.server.dto.vehicle.VehicleResDTO;
import com.douzone.server.entity.Vehicle;
import com.douzone.server.entity.VehicleReservation;
import com.douzone.server.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class VehicleService {
	private static final String METHOD_NAME = VehicleService.class.getName();
	private final VehicleRepository vehicleRepository;

	public VehicleResDTO findAllReserved() {
		log.info(METHOD_NAME + "-findAllReserved");
		try {
			List<Vehicle> list = vehicleRepository.findAllReserved();

			if (list == null) return VehicleResDTO.builder().code(1).build();

			return VehicleResDTO.builder().code(0).data(list).build();
		} catch (Exception e) {
			log.error("SERVER ERROR", e);
		}
		return VehicleResDTO.builder().code(2).build();
	}

	public VehicleResDTO findAllUnreserved() {
		log.info(METHOD_NAME + "-findAllUnreserved");
		try {
			List<Vehicle> list = vehicleRepository.findAllUnreserved();

			if (list == null) return VehicleResDTO.builder().code(1).build();

			return VehicleResDTO.builder().code(0).data(list).build();
		} catch (Exception e) {
			log.error("SERVER ERROR", e);
		}
		return VehicleResDTO.builder().code(2).build();
	}

	public VehicleResDTO findTypeReserved(String model) {
		log.info(METHOD_NAME + "-findTypeReserved");
		try {
			List<Vehicle> list = vehicleRepository.findTypeReserved(model);

			if (list == null) return VehicleResDTO.builder().code(1).build();

			return VehicleResDTO.builder().code(0).data(list).build();
		} catch (Exception e) {
			log.error("SERVER ERROR", e);
		}
		return VehicleResDTO.builder().code(2).build();
	}

	public VehicleResDTO findDateReserved(Date date) {
		log.info(METHOD_NAME + "-findTypeReserved");
		try {
			List<Vehicle> list = vehicleRepository.findDateReserved(date);

			if (list == null) return VehicleResDTO.builder().code(1).build();

			return VehicleResDTO.builder().code(0).data(list).build();
		} catch (Exception e) {
			log.error("SERVER ERROR", e);
		}
		return VehicleResDTO.builder().code(2).build();
	}

	public VehicleResDTO findEmpBefore(Long id, Date date) {
		log.info(METHOD_NAME + "-findEmpBefore");
		try {
			List<VehicleReservation> list = vehicleRepository.findEmpBefore(id, date);

			if (list == null) return VehicleResDTO.builder().code(1).build();

			return VehicleResDTO.builder().code(0).data(list).build();
		} catch (Exception e) {
			log.error("SERVER ERROR", e);
		}
		return VehicleResDTO.builder().code(2).build();
	}

	public VehicleResDTO findEmpAfter(Long id, Date date) {
		log.info(METHOD_NAME + "-findEmpAfter");
		try {
			List<VehicleReservation> list = vehicleRepository.findEmpAfter(id, date);

			if (list == null) return VehicleResDTO.builder().code(1).build();

			return VehicleResDTO.builder().code(0).data(list).build();
		} catch (Exception e) {
			log.error("SERVER ERROR", e);
		}
		return VehicleResDTO.builder().code(2).build();
	}

	public VehicleResDTO findWeekVehicle() {
		log.info(METHOD_NAME + "-findWeekVehicle");
		try {
			Vehicle vehicle = vehicleRepository.findWeekVehicle();

			if (vehicle == null) return VehicleResDTO.builder().code(1).build();

			return VehicleResDTO.builder().code(0).data(vehicle).build();
		} catch (Exception e) {
			log.error("SERVER ERROR", e);
		}
		return VehicleResDTO.builder().code(2).build();
	}

	public VehicleResDTO findWeekDate() {
		log.info(METHOD_NAME + "-findWeekDate");
		try {
			Integer result = vehicleRepository.findWeekDate();

			if (result == null) return VehicleResDTO.builder().code(1).build();

			return VehicleResDTO.builder().code(0).data(result).build();
		} catch (Exception e) {
			log.error("SERVER ERROR", e);
		}
		return VehicleResDTO.builder().code(2).build();
	}

	public VehicleResDTO findRecentVehicle() {
		log.info(METHOD_NAME + "-findRecentVehicle");
		try {
			Vehicle vehicle = vehicleRepository.findRecentVehicle();

			if (vehicle == null) return VehicleResDTO.builder().code(1).build();

			return VehicleResDTO.builder().code(0).data(vehicle).build();
		} catch (Exception e) {
			log.error("SERVER ERROR", e);
		}
		return VehicleResDTO.builder().code(2).build();
	}

	public VehicleResDTO findMarkVehicle(String empNo) {
		log.info(METHOD_NAME + "-findMarkVehicle");
		try {
			List<Vehicle> list = vehicleRepository.findMarkVehicle(empNo);

			if (list == null) return VehicleResDTO.builder().code(1).build();

			return VehicleResDTO.builder().code(0).data(list).build();
		} catch (Exception e) {
			log.error("SERVER ERROR", e);
		}
		return VehicleResDTO.builder().code(2).build();
	}

	public VehicleResDTO findMarkBest() {
		log.info(METHOD_NAME + "-findMarkBest");
		try {
			List<Vehicle> list = vehicleRepository.findMarkBest();

			if (list == null) return VehicleResDTO.builder().code(1).build();

			return VehicleResDTO.builder().code(0).data(list).build();
		} catch (Exception e) {
			log.error("SERVER ERROR", e);
		}
		return VehicleResDTO.builder().code(2).build();
	}
}
