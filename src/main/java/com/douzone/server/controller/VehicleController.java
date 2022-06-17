package com.douzone.server.controller;

import com.douzone.server.config.security.auth.PrincipalDetails;
import com.douzone.server.config.utils.ResponseDTO;
import com.douzone.server.dto.vehicle.VehicleReqDTO;
import com.douzone.server.dto.vehicle.VehicleReservationDTO;
import com.douzone.server.service.VehicleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * Create:
 * createReservation - 차량 예약 등록 /creation/reservation
 * createBookmark - 차량 즐겨찾기 등록 /creation/bookmark
 * Read:
 * findAllReserved - 차량 전체 예약 현황 조회 /list/reservation
 * findAllUnreserved - 차량 전체 미예약 현황 조회 /list/stock
 * findAllReservedPaging - 차량 예약 페이징 처리 /list/reservation/paging
 * findTypeReserved - 차량 종류별 예약 현황 조회 /list/type
 * findDateReserved - 특정 일시 차량 예약 현황 조회 /list/time
 * findEmpBefore - 내 예약 조회 - 과거 /list/own/past
 * findEmpAfter - 내 예약 조회 - 미래 /list/own/current
 * findWeekVehicle - 7일 동안 가장 많이 예약된 차량 /best/vehicle
 * findWeekDate - 7일 동안 가장 많이 예약한 시간대 /best/time
 * findRecentVehicle - 최근 예약된 차량 조회 /list/recent
 * findMarkVehicle - 내가 즐겨찾기한 차량 조회 /list/own/mark
 * findMarkBest - 즐겨찾기가 많은 차량 Top 3 조회 /best/mark
 * findVehicleReserved - 수정 전 차량 단일 조회 /reservation
 * soonAndIngReservationMyTime - 동적 타이머 /own/reservation
 * Update:
 * updateReserved - 내 차량 예약 현황 수정 /modification
 * Delete:
 * deleteReserved - 내 차량 예약 삭제 /elimination
 * deleteMark - 내 즐겨찾기 차량 삭제 /elimination/mark
 */

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/emp/vehicle")
public class VehicleController {
	private static final String METHOD_NAME = VehicleController.class.getName();
	private final VehicleService vehicleService;

	@PostMapping(path = "/creation/reservation")
	public ResponseDTO createReservation(@RequestBody @Valid VehicleReservationDTO vehicleReservationDTO,
										 @AuthenticationPrincipal PrincipalDetails principalDetails) {
		log.info(METHOD_NAME + "- createReservation");
		Long empId = principalDetails.getEmployee().getId();

		return vehicleService.createReservation(vehicleReservationDTO, empId);
	}

	@PostMapping(path = "/creation/bookmark")
	public ResponseDTO createBookmark(@RequestParam(value = "vId") Long vId,
									  @AuthenticationPrincipal PrincipalDetails principalDetails) {
		log.info(METHOD_NAME + "- createBookmark");
		Long empId = principalDetails.getEmployee().getId();

		return vehicleService.createBookmark(empId, vId);
	}

	@GetMapping(path = "/list/reservation")
	public ResponseDTO findAllReserved() {
		log.info(METHOD_NAME + "- findAllReserved");

		return vehicleService.findAllReserved();
	}

	@GetMapping(path = "/list/reservation/paging")
	public ResponseDTO findAllReservedPaging(@RequestBody Map<String, Integer> pageNum) {
		log.info(METHOD_NAME + "- findAllReservedPaging");

		return vehicleService.findAllReservedPaging(pageNum.get("page"));
	}

	@GetMapping(path = "/list/stock")
	public ResponseDTO findAllUnreserved() {
		log.info(METHOD_NAME + "- findAllUnreserved");

		return vehicleService.findAllUnreserved();
	}

	@GetMapping(path = "/list/type")
	public ResponseDTO findTypeReserved(@RequestBody Map<String, String> model) {
		log.info(METHOD_NAME + "- findTypeReserved");
		System.out.println(model);
		return vehicleService.findTypeReserved(model.get("model"));
	}

	@GetMapping(path = "/list/time")
	public ResponseDTO findDateReserved(@RequestBody Map<String, String> model) {
		log.info(METHOD_NAME + "- findDateReserved");

		return vehicleService.findDateReserved(model.get("start"), model.get("end"));
	}

	@GetMapping(path = "/list/own/past")
	public ResponseDTO findEmpBefore(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		log.info(METHOD_NAME + "- findEmpBefore");
		Long id = principalDetails.getEmployee().getId();

		return vehicleService.findEmpBefore(id);
	}

	@GetMapping(path = "/list/own/current")
	public ResponseDTO findEmpAfter(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		log.info(METHOD_NAME + "- findEmpAfter");
		Long id = principalDetails.getEmployee().getId();

		return vehicleService.findEmpAfter(id);
	}

	@GetMapping(path = "/best/vehicle")
	public ResponseDTO findWeekVehicle() {
		log.info(METHOD_NAME + "- findWeekVehicle");

		return vehicleService.findWeekVehicle();
	}

	@GetMapping(path = "/best/time")
	public ResponseDTO findWeekDate() {
		log.info(METHOD_NAME + "- findWeekDate");

		return vehicleService.findWeekDate();
	}

	@GetMapping(path = "/list/recent")
	public ResponseDTO findRecentVehicle() {
		log.info(METHOD_NAME + "- findRecentVehicle");

		return vehicleService.findRecentVehicle();
	}

	@GetMapping(path = "/list/own/mark")
	public ResponseDTO findMarkVehicle(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		log.info(METHOD_NAME + "- findMarkVehicle");
		String empNo = principalDetails.getEmployee().getEmpNo();

		return vehicleService.findMarkVehicle(empNo);
	}

	@GetMapping(path = "/best/mark")
	public ResponseDTO findMarkBest() {
		log.info(METHOD_NAME + "- findMarkBest");

		return vehicleService.findMarkBest();
	}

	@GetMapping(path = "/reservation")
	public ResponseDTO findVehicleReserved(@RequestParam(value = "vrId") Long vrId,
										   @AuthenticationPrincipal PrincipalDetails principalDetails) {
		log.info(METHOD_NAME + "- findVehicleReserved");

		return vehicleService.findVehicleReserved(vrId);
	}


	@PostMapping(path = "/modification")
	public ResponseDTO updateReserved(@RequestBody @Valid VehicleReqDTO vehicleReqDTO,
									  @AuthenticationPrincipal PrincipalDetails principalDetails) {
		log.info(METHOD_NAME + "- updateReserved");

		Long id = principalDetails.getEmployee().getId();

		return vehicleService.updateReserved(vehicleReqDTO, id);
	}

	@PostMapping(path = "/elimination")
	public ResponseDTO deleteReserved(@RequestParam("id") Long id, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		log.info(METHOD_NAME + "- deleteReserved");
		Long empId = principalDetails.getEmployee().getId();
		return vehicleService.deleteReserved(id, empId);
	}

	@PostMapping(path = "/elimination/mark")
	public ResponseDTO deleteMark(@RequestParam("id") Long id) {
		log.info(METHOD_NAME + "- deleteMark");

		return vehicleService.deleteMark(id);
	}

	@GetMapping(path = "/reservation")
	public ResponseDTO findVehicleReserved(@RequestParam("id") Long id) {
		log.info(METHOD_NAME + "- findVehicleReserved");

		return vehicleService.findVehicleReserved(id);
	}
}
