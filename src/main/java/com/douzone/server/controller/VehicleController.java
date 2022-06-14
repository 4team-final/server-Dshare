package com.douzone.server.controller;

import com.douzone.server.config.utils.ResponseDTO;
import com.douzone.server.entity.Vehicle;
import com.douzone.server.service.VehicleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * findAllReserved - 차량 전체 예약 현황 조회 /list_all
 * findAllUnreserved - 차량 전체 미예약 현황 조회 /list_un
 * findTypeReserved - 차량 종류별 예약 현황 조회 /list_type
 * findDateReserved - 특정 일시 차량 예약 현황 조회 /list_date
 */

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/emp/vehicle")
public class VehicleController {
	private static final String METHOD_NAME = "VehicleController";
	private final VehicleService vehicleService;

	@GetMapping(path = "/list_all")
	public ResponseDTO findAllReserved() {
		log.info(METHOD_NAME + "- findAllReserved");
		List<Vehicle> result = vehicleService.findAllReserved();

		if (result == null) return new ResponseDTO().fail(HttpStatus.BAD_REQUEST, "");

		return new ResponseDTO().of(HttpStatus.OK, "", result);
	}

	@GetMapping(path = "/list_un")
	public ResponseDTO findAllUnreserved() {
		log.info(METHOD_NAME + "- findAllUnreserved");
		List<Vehicle> result = vehicleService.findAllUnreserved();

		if (result == null) return new ResponseDTO().fail(HttpStatus.BAD_REQUEST, "");

		return new ResponseDTO().of(HttpStatus.OK, "", result);
	}

	@GetMapping(path = "/list_type")
	public ResponseDTO findTypeReserved(@RequestBody String model) {
		log.info(METHOD_NAME + "- findTypeReserved");
		List<Vehicle> result = vehicleService.findTypeReserved(model);

		if (result == null) return new ResponseDTO().fail(HttpStatus.BAD_REQUEST, "");

		return new ResponseDTO().of(HttpStatus.OK, "", result);
	}

	@GetMapping(path = "/list_date")
	public ResponseDTO findDateReserved(@RequestBody Date date) {
		log.info(METHOD_NAME + "- findDateReserved");
		List<Vehicle> result = vehicleService.findDateReserved(date);

		if (result == null) return new ResponseDTO().fail(HttpStatus.BAD_REQUEST, "");

		return new ResponseDTO().of(HttpStatus.OK, "", result);
	}
}
