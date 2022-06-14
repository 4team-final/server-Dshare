package com.douzone.server.service;

import com.douzone.server.entity.Vehicle;
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
	private static final String METHOD_NAME = "VehicleService";
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
}
