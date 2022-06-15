package com.douzone.server.controller;

import com.douzone.server.config.utils.Msg;
import com.douzone.server.config.utils.ResponseDTO;
import com.douzone.server.dto.vehicle.VehicleResDTO;
import com.douzone.server.service.VehicleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * Read:
 * findAllReserved - 차량 전체 예약 현황 조회 /list_all
 * findAllUnreserved - 차량 전체 미예약 현황 조회 /list_un
 * findTypeReserved - 차량 종류별 예약 현황 조회 /list_type
 * findDateReserved - 특정 일시 차량 예약 현황 조회 /list_date
 * findEmpBefore - 내 예약 조회 - 과거 /list_pre
 * findEmpAfter - 내 예약 조회 - 미래 /list_post
 * findWeekVehicle - 7일 동안 가장 많이 예약된 차량 /best_vehicle
 * findWeekDate - 7일 동안 가장 많이 예약한 시간대 /best_time
 * findRecentVehicle - 최근 예약된 차량 조회 /recent
 * findMarkVehicle - 내가 즐겨찾기한 차량 조회 /mark
 * findMarkBest - 즐겨찾기가 많은 차량 Top 3 조회 /best_mark
 * Update:
 * updateReserved - 내 차량 예약 현황 수정 /update
 * delete:
 * deleteReserved - 내 차량 예약 삭제 /delete
 * deleteReserved - 내 즐겨찾기 차량 삭제 /delete_mark
 */

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/emp/vehicle")
public class VehicleController {
	private static final String METHOD_NAME = VehicleController.class.getName();
	private final VehicleService vehicleService;

	@GetMapping(path = "/list_all")
	public ResponseDTO findAllReserved() {
		log.info(METHOD_NAME + "- findAllReserved");
		VehicleResDTO result = vehicleService.findAllReserved();

		switch (result.getCode()) {
			case 0:
				return ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_VEHICLE_FIND_ALL, result);
			case 1:
				return ResponseDTO.fail(HttpStatus.BAD_REQUEST, Msg.FAIL_VEHICLE_FIND_ALL);
			case 2:
			default:
				return ResponseDTO.fail(HttpStatus.INTERNAL_SERVER_ERROR, Msg.FAIL_VEHICLE_FIND_ALL);
		}
	}

	@GetMapping(path = "/list_un")
	public ResponseDTO findAllUnreserved() {
		log.info(METHOD_NAME + "- findAllUnreserved");
		VehicleResDTO result = vehicleService.findAllUnreserved();

		switch (result.getCode()) {
			case 0:
				return ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_VEHICLE_FIND_NONE, result);
			case 1:
				return ResponseDTO.fail(HttpStatus.BAD_REQUEST, Msg.FAIL_VEHICLE_FIND_NONE);
			case 2:
			default:
				return ResponseDTO.fail(HttpStatus.INTERNAL_SERVER_ERROR, Msg.FAIL_VEHICLE_FIND_NONE);
		}
	}

	@PostMapping(path = "/list_type")
	public ResponseDTO findTypeReserved(@RequestBody String model) {
		log.info(METHOD_NAME + "- findTypeReserved");
		VehicleResDTO result = vehicleService.findTypeReserved(model);

		switch (result.getCode()) {
			case 0:
				return ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_VEHICLE_FIND_TYPE, result);
			case 1:
				return ResponseDTO.fail(HttpStatus.BAD_REQUEST, Msg.FAIL_VEHICLE_FIND_TYPE);
			case 2:
			default:
				return ResponseDTO.fail(HttpStatus.INTERNAL_SERVER_ERROR, Msg.FAIL_VEHICLE_FIND_TYPE);
		}
	}

	@PostMapping(path = "/list_date")
	public ResponseDTO findDateReserved(@RequestBody Date date) {
		log.info(METHOD_NAME + "- findDateReserved");
		VehicleResDTO result = vehicleService.findDateReserved(date);

		switch (result.getCode()) {
			case 0:
				return ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_VEHICLE_FIND_DATE, result);
			case 1:
				return ResponseDTO.fail(HttpStatus.BAD_REQUEST, Msg.FAIL_VEHICLE_FIND_DATE);
			case 2:
			default:
				return ResponseDTO.fail(HttpStatus.INTERNAL_SERVER_ERROR, Msg.FAIL_VEHICLE_FIND_DATE);
		}
	}

	@PostMapping(path = "/list_pre")
	public ResponseDTO findEmpBefore(@RequestBody Long id) {
		log.info(METHOD_NAME + "- findEmpBefore");
		VehicleResDTO result = vehicleService.findEmpBefore(id, new Date());

		switch (result.getCode()) {
			case 0:
				return ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_VEHICLE_BEFORE, result);
			case 1:
				return ResponseDTO.fail(HttpStatus.BAD_REQUEST, Msg.FAIL_VEHICLE_BEFORE);
			case 2:
			default:
				return ResponseDTO.fail(HttpStatus.INTERNAL_SERVER_ERROR, Msg.FAIL_VEHICLE_BEFORE);
		}
	}

	@PostMapping(path = "/list_post")
	public ResponseDTO findEmpAfter(@RequestBody Long id) {
		log.info(METHOD_NAME + "- findEmpAfter");
		VehicleResDTO result = vehicleService.findEmpAfter(id, new Date());

		switch (result.getCode()) {
			case 0:
				return ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_VEHICLE_AFTER, result);
			case 1:
				return ResponseDTO.fail(HttpStatus.BAD_REQUEST, Msg.FAIL_VEHICLE_AFTER);
			case 2:
			default:
				return ResponseDTO.fail(HttpStatus.INTERNAL_SERVER_ERROR, Msg.FAIL_VEHICLE_AFTER);
		}
	}

	@GetMapping(path = "/best_vehicle")
	public ResponseDTO findWeekVehicle() {
		log.info(METHOD_NAME + "- findWeekVehicle");
		VehicleResDTO result = vehicleService.findWeekVehicle();

		switch (result.getCode()) {
			case 0:
				return ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_VEHICLE_BEST_WEEK, result);
			case 1:
				return ResponseDTO.fail(HttpStatus.BAD_REQUEST, Msg.FAIL_VEHICLE_BEST_WEEK);
			case 2:
			default:
				return ResponseDTO.fail(HttpStatus.INTERNAL_SERVER_ERROR, Msg.FAIL_VEHICLE_BEST_WEEK);
		}
	}

	@GetMapping(path = "/best_time")
	public ResponseDTO findWeekDate() {
		log.info(METHOD_NAME + "- findWeekDate");
		VehicleResDTO result = vehicleService.findWeekDate();

		switch (result.getCode()) {
			case 0:
				return ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_VEHICLE_BEST_DATE, result);
			case 1:
				return ResponseDTO.fail(HttpStatus.BAD_REQUEST, Msg.FAIL_VEHICLE_BEST_DATE);
			case 2:
			default:
				return ResponseDTO.fail(HttpStatus.INTERNAL_SERVER_ERROR, Msg.FAIL_VEHICLE_BEST_DATE);
		}
	}

	@GetMapping(path = "/recent")
	public ResponseDTO findRecentVehicle() {
		log.info(METHOD_NAME + "- findRecentVehicle");
		VehicleResDTO result = vehicleService.findRecentVehicle();

		switch (result.getCode()) {
			case 0:
				return ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_VEHICLE_RECENT, result);
			case 1:
				return ResponseDTO.fail(HttpStatus.BAD_REQUEST, Msg.FAIL_VEHICLE_RECENT);
			case 2:
			default:
				return ResponseDTO.fail(HttpStatus.INTERNAL_SERVER_ERROR, Msg.FAIL_VEHICLE_RECENT);
		}
	}

	@GetMapping(path = "/mark")
	public ResponseDTO findMarkVehicle(@RequestBody String empNo) {
		log.info(METHOD_NAME + "- findMarkVehicle");
		VehicleResDTO result = vehicleService.findMarkVehicle(empNo);

		switch (result.getCode()) {
			case 0:
				return ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_VEHICLE_FIND_MARK, result);
			case 1:
				return ResponseDTO.fail(HttpStatus.BAD_REQUEST, Msg.FAIL_VEHICLE_FIND_MARK);
			case 2:
			default:
				return ResponseDTO.fail(HttpStatus.INTERNAL_SERVER_ERROR, Msg.FAIL_VEHICLE_FIND_MARK);
		}
	}

	@GetMapping(path = "/best_mark")
	public ResponseDTO findMarkBest() {
		log.info(METHOD_NAME + "- findMarkBest");
		VehicleResDTO result = vehicleService.findMarkBest();

		switch (result.getCode()) {
			case 0:
				return ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_VEHICLE_BEST_MARK, result);
			case 1:
				return ResponseDTO.fail(HttpStatus.BAD_REQUEST, Msg.FAIL_VEHICLE_BEST_MARK);
			case 2:
			default:
				return ResponseDTO.fail(HttpStatus.INTERNAL_SERVER_ERROR, Msg.FAIL_VEHICLE_BEST_MARK);
		}
	}
}
