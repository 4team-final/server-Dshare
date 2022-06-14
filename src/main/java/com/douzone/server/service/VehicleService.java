package com.douzone.server.service;

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
		log.info(METHOD_NAME + "-findAllUnreserved");
		try {
			return vehicleRepository.findAllUnreserved();
		} catch (Exception e) {
			log.error("SERVER ERROR", e);
		}
		return null;
	}

	public List<Vehicle> findTypeReserved(String model) {
		log.info(METHOD_NAME + "-findTypeReserved");
		try {
			return vehicleRepository.findTypeReserved(model);
		} catch (Exception e) {
			log.error("SERVER ERROR", e);
		}
		return null;
	}

	public List<Vehicle> findDateReserved(Date date) {
		log.info(METHOD_NAME + "-findTypeReserved");
		try {
			return vehicleRepository.findDateReserved(date);
		} catch (Exception e) {
			log.error("SERVER ERROR", e);
		}
		return null;
	}

	public List<VehicleReservation> findEmpBefore(Long id, Date date) {
		log.info(METHOD_NAME + "-findEmpBefore");
		try {
			return vehicleRepository.findEmpBefore(id, date);
		} catch (Exception e) {
			log.error("SERVER ERROR", e);
		}
		return null;
	}

	public List<VehicleReservation> findEmpAfter(Long id,
												 Date date) {
		log.info(METHOD_NAME + "-findEmpAfter");
		try {
			return vehicleRepository.findEmpAfter(id, date);
		} catch (Exception e) {
			log.error("SERVER ERROR", e);
		}
		return null;
	}

	public Vehicle findWeekVehicle() {
		log.info(METHOD_NAME + "-findWeekVehicle");
		try {
			return vehicleRepository.findWeekVehicle();
		} catch (Exception e) {
			log.error("SERVER ERROR", e);
		}
		return null;
	}

	public Integer findWeekDate() {
		log.info(METHOD_NAME + "-findWeekDate");
		try {
			return vehicleRepository.findWeekDate();
		} catch (Exception e) {
			log.error("SERVER ERROR", e);
		}
		return null;
	}

	public Vehicle findRecentVehicle() {
		log.info(METHOD_NAME + "-findRecentVehicle");
		try {
			return vehicleRepository.findRecentVehicle();
		} catch (Exception e) {
			log.error("SERVER ERROR", e);
		}
		return null;
	}

	public List<Vehicle> findMarkVehicle(String empNo) {
		log.info(METHOD_NAME + "-findMarkVehicle");
		try {
			return vehicleRepository.findMarkVehicle(empNo);
		} catch (Exception e) {
			log.error("SERVER ERROR", e);
		}
		return null;
	}

	public List<Vehicle> findMarkBest() {
		log.info(METHOD_NAME + "-findMarkBest");
		try {
			return vehicleRepository.findMarkBest();
		} catch (Exception e) {
			log.error("SERVER ERROR", e);
		}
		return null;
	}
}
