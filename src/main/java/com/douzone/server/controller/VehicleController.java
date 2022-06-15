package com.douzone.server.controller;

import com.douzone.server.config.security.auth.PrincipalDetails;
import com.douzone.server.config.utils.ResponseDTO;
import com.douzone.server.dto.vehicle.VehicleReqDTO;
import com.douzone.server.dto.vehicle.VehicleReservationDTO;
import com.douzone.server.service.VehicleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * Create:
 * createReservation - 차량 예약 등록 /create_reservation
 * createBookmark - 차량 즐겨찾기 등록 /create_bookmark
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
 * Delete:
 * deleteReserved - 내 차량 예약 삭제 /delete
 * deleteMark - 내 즐겨찾기 차량 삭제 /delete_mark
 */

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/emp/vehicle")
public class VehicleController {
	private static final String METHOD_NAME = VehicleController.class.getName();
	private final VehicleService vehicleService;

	@PostMapping(path = "/create_reservation")
	public ResponseDTO createReservation(@RequestBody VehicleReservationDTO vehicleReservationDTO) {
		log.info(METHOD_NAME + "- createReservation");
		Integer result = vehicleService.createReservation(vehicleReservationDTO);

		if (result == 0) return new ResponseDTO().fail(HttpStatus.BAD_REQUEST, "");

		return new ResponseDTO().of(HttpStatus.OK, "");
	}

	@PostMapping(path = "/create_bookmark")
	public ResponseDTO createBookmark() {
		log.info(METHOD_NAME + "- createBookmark");
		Integer result = vehicleService.createBookmark();

		if (result == 0) return new ResponseDTO().fail(HttpStatus.BAD_REQUEST, "");

		return new ResponseDTO().of(HttpStatus.OK, "");
	}

	@GetMapping(path = "/list_all")
	public ResponseDTO findAllReserved() {
		log.info(METHOD_NAME + "- findAllReserved");

		return vehicleService.findAllReserved();
	}

	@GetMapping(path = "/list_un")
	public ResponseDTO findAllUnreserved() {
		log.info(METHOD_NAME + "- findAllUnreserved");

		return vehicleService.findAllUnreserved();
	}

	@GetMapping(path = "/list_type")
	public ResponseDTO findTypeReserved(@RequestBody String model) {
		log.info(METHOD_NAME + "- findTypeReserved");

		return vehicleService.findTypeReserved(model);
	}

	@GetMapping(path = "/list_date")
	public ResponseDTO findDateReserved(@RequestBody Date date) {
		log.info(METHOD_NAME + "- findDateReserved");

		return vehicleService.findDateReserved(date);
	}

	@GetMapping(path = "/list_pre")
	public ResponseDTO findEmpBefore(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		log.info(METHOD_NAME + "- findEmpBefore");
		Long id = principalDetails.getEmployee().getId();

		return vehicleService.findEmpBefore(id, new Date());
	}

	@GetMapping(path = "/list_post")
	public ResponseDTO findEmpAfter(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		log.info(METHOD_NAME + "- findEmpAfter");
		Long id = principalDetails.getEmployee().getId();

		return vehicleService.findEmpAfter(id, new Date());
	}

	@GetMapping(path = "/best_vehicle")
	public ResponseDTO findWeekVehicle() {
		log.info(METHOD_NAME + "- findWeekVehicle");

		return vehicleService.findWeekVehicle();
	}

	@GetMapping(path = "/best_time")
	public ResponseDTO findWeekDate() {
		log.info(METHOD_NAME + "- findWeekDate");

		return vehicleService.findWeekDate();
	}

	@GetMapping(path = "/recent")
	public ResponseDTO findRecentVehicle() {
		log.info(METHOD_NAME + "- findRecentVehicle");

		return vehicleService.findRecentVehicle();
	}

	@GetMapping(path = "/mark")
	public ResponseDTO findMarkVehicle(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		log.info(METHOD_NAME + "- findMarkVehicle");
		String empNo = principalDetails.getEmployee().getEmpNo();

		return vehicleService.findMarkVehicle(empNo);
	}

	@GetMapping(path = "/best_mark")
	public ResponseDTO findMarkBest() {
		log.info(METHOD_NAME + "- findMarkBest");

		return vehicleService.findMarkBest();
	}

	@PostMapping(path = "/update")
	public ResponseDTO updateReserved(@RequestBody VehicleReqDTO vehicleReqDTO) {
		log.info(METHOD_NAME + "- updateReserved");

		return vehicleService.updateReserved(vehicleReqDTO);
	}

	@PostMapping(path = "/delete")
	public ResponseDTO deleteReserved(@RequestBody Long id) {
		log.info(METHOD_NAME + "- deleteReserved");

		return vehicleService.deleteReserved(id);
	}

	@PostMapping(path = "/delete_mark")
	public ResponseDTO deleteMark(@RequestBody Long id) {
		log.info(METHOD_NAME + "- deleteMark");

		return vehicleService.deleteMark(id);
	}
}
