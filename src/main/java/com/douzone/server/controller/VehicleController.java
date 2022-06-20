package com.douzone.server.controller;

import com.douzone.server.config.security.auth.PrincipalDetails;
import com.douzone.server.config.utils.ResponseDTO;
import com.douzone.server.dto.vehicle.VehicleParseDTO;
import com.douzone.server.service.VehicleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * 차량 예약 등록, 조회, 수정, 삭제
 * Create:
 * registerByVehicleReservation - 차량 예약 등록 /creation/reservation - POST
 * registerByVehicleBookmark - 차량 즐겨찾기 등록 /creation/bookmark - POST
 * Read:
 * findByAllReservation - 차량 전체 예약 현황 조회 /list/reservation - GET
 * findByPaginationReservation - 차량 예약 페이징 처리 /list/reservation/paging - GET
 * findByAllNotReservation - 차량 전체 미예약 현황 조회 /list/stock - GET
 * findByModelReservation - 차량 종류별 예약 현황 조회 /list/type - GET
 * findByDateTimeReservation - 특정 일시 차량 예약 현황 조회 /list/time - GET
 * findByMyPastReservation - 내 예약 조회 - 과거 /list/own/past - GET
 * findByMyCurrentReservation - 내 예약 조회 - 미래 /list/own/current - GET
 * weekMostReservedVehicle - 7일 동안 가장 많이 예약된 차량 /best/vehicle - GET
 * weekMostReservedTime - 7일 동안 가장 많이 예약한 시간대 /best/time - GET
 * findByRecentReservedVehicle - 최근 예약된 차량 조회 /list/recent - GET
 * findByMyBookMarkVehicle - 내가 즐겨찾기한 차량 조회 /list/own/mark - GET
 * selectByBookMarkTop3Vehicle - 즐겨찾기가 많은 차량 Top 3 조회 /best/mark - GET
 * selectByVehicleReservation - 수정 전 차량 단일 조회 /reservation - GET
 * IngReservationMyTime - 내가 현재 진행중인 예약 종료 시간 조회 /own/reservation/ongoing - GET
 * soonReservationMyTime - 나의 다음 예약 시작 시간 조회 /own/reservation/next - GET
 * Update:
 * updateByMyReservation - 내 차량 예약 현황 수정 /modification - PATCH
 * Delete:
 * deleteByMyReservation - 내 차량 예약 삭제 /elimination
 * deleteByMyBookMark - 내 즐겨찾기 차량 삭제 /elimination/mark
 */

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/emp/vehicle")
public class VehicleController {
	private static final String METHOD_NAME = VehicleController.class.getName();
	private final VehicleService vehicleService;

	@PostMapping(path = "/creation/reservation")
	public ResponseEntity<ResponseDTO> registerByVehicleReservation(@RequestBody @Valid VehicleParseDTO vehicleParseDTO,
																	@AuthenticationPrincipal PrincipalDetails principalDetails) {
		log.info(METHOD_NAME + "- registerByVehicleReservation");
		Long empId = principalDetails.getEmployee().getId();

		return ResponseEntity.ok().body(vehicleService.registerByVehicleReservation(vehicleParseDTO, empId));
	}

	@PostMapping(path = "/creation/bookmark")
	public ResponseEntity<ResponseDTO> registerByVehicleBookmark(@RequestParam(value = "vId") Long vId,
																 @AuthenticationPrincipal PrincipalDetails principalDetails) {
		log.info(METHOD_NAME + "- registerByVehicleBookmark");
		Long empId = principalDetails.getEmployee().getId();

		return ResponseEntity.ok().body(vehicleService.registerByVehicleBookmark(empId, vId));
	}

	@GetMapping(path = "/list/reservation")
	public ResponseEntity<ResponseDTO> findByAllReservation() {
		log.info(METHOD_NAME + "- findByAllReservation");

		return ResponseEntity.ok().body(vehicleService.findByAllReservation());
	}

	@GetMapping(path = "/list/reservation/paging")
	public ResponseEntity<ResponseDTO> findByPaginationReservation(@RequestBody Map<String, Integer> pageNum) {
		log.info(METHOD_NAME + "- findByPaginationReservation");

		return ResponseEntity.ok().body(vehicleService.findByPaginationReservation(pageNum.get("page")));
	}

	@GetMapping(path = "/list/stock")
	public ResponseEntity<ResponseDTO> findByAllNotReservation() {
		log.info(METHOD_NAME + "- findByAllNotReservation");

		return ResponseEntity.ok().body(vehicleService.findByAllNotReservation());
	}

	@GetMapping(path = "/list/type")
	public ResponseEntity<ResponseDTO> findByModelReservation(@RequestBody Map<String, String> model) {
		log.info(METHOD_NAME + "- findByModelReservation");
		System.out.println(model);
		return ResponseEntity.ok().body(vehicleService.findByModelReservation(model.get("model")));
	}

	@GetMapping(path = "/list/time")
	public ResponseEntity<ResponseDTO> findByDateTimeReservation(@RequestBody Map<String, String> model) {
		log.info(METHOD_NAME + "- findByDateTimeReservation");

		return ResponseEntity.ok().body(vehicleService.findByDateTimeReservation(model.get("start"), model.get("end")));
	}

	@GetMapping(path = "/list/own/past")
	public ResponseEntity<ResponseDTO> findByMyPastReservation(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		log.info(METHOD_NAME + "- findByMyPastReservation");
		Long id = principalDetails.getEmployee().getId();

		return ResponseEntity.ok().body(vehicleService.findByMyPastReservation(id));
	}

	@GetMapping(path = "/list/own/current")
	public ResponseEntity<ResponseDTO> findByMyCurrentReservation(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		log.info(METHOD_NAME + "- findByMyCurrentReservation");
		Long id = principalDetails.getEmployee().getId();

		return ResponseEntity.ok().body(vehicleService.findByMyCurrentReservation(id));
	}

	@GetMapping(path = "/best/vehicle")
	public ResponseEntity<ResponseDTO> weekMostReservedVehicle() {
		log.info(METHOD_NAME + "- weekMostReservedVehicle");

		return ResponseEntity.ok().body(vehicleService.weekMostReservedVehicle());
	}

	@GetMapping(path = "/best/time")
	public ResponseEntity<ResponseDTO> weekMostReservedTime() {
		log.info(METHOD_NAME + "- weekMostReservedTime");

		return ResponseEntity.ok().body(vehicleService.weekMostReservedTime());
	}

	@GetMapping(path = "/list/recent")
	public ResponseEntity<ResponseDTO> findByRecentReservedVehicle() {
		log.info(METHOD_NAME + "- findByRecentReservedVehicle");

		return ResponseEntity.ok().body(vehicleService.findByRecentReservedVehicle());
	}

	@GetMapping(path = "/list/own/mark")
	public ResponseEntity<ResponseDTO> findByMyBookMarkVehicle(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		log.info(METHOD_NAME + "- findByMyBookMarkVehicle");
		String empNo = principalDetails.getEmployee().getEmpNo();

		return ResponseEntity.ok().body(vehicleService.findByMyBookMarkVehicle(empNo));
	}

	@GetMapping(path = "/best/mark")
	public ResponseEntity<ResponseDTO> selectByBookMarkTop3Vehicle() {
		log.info(METHOD_NAME + "- selectByBookMarkTop3Vehicle");

		return ResponseEntity.ok().body(vehicleService.selectByBookMarkTop3Vehicle());
	}


	@PatchMapping(path = "/modification")
	public ResponseEntity<ResponseDTO> updateByMyReservation(@RequestBody @Valid VehicleParseDTO vehicleParseDTO,
															 @AuthenticationPrincipal PrincipalDetails principalDetails) {

		log.info(METHOD_NAME + "- updateByMyReservation");

		Long id = principalDetails.getEmployee().getId();

		return ResponseEntity.ok().body(vehicleService.updateByMyReservation(vehicleParseDTO, id));
	}

	@DeleteMapping(path = "/elimination")
	public ResponseEntity<ResponseDTO> deleteByMyReservation(@RequestParam("id") Long id, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		log.info(METHOD_NAME + "- deleteByMyReservation");
		Long empId = principalDetails.getEmployee().getId();
		return ResponseEntity.ok().body(vehicleService.deleteByMyReservation(id, empId));
	}

	@DeleteMapping(path = "/elimination/mark")
	public ResponseEntity<ResponseDTO> deleteByMyBookMark(@RequestParam("id") Long id) {
		log.info(METHOD_NAME + "- deleteByMyBookMark");

		return ResponseEntity.ok().body(vehicleService.deleteByMyBookMark(id));
	}

	@GetMapping(path = "/reservation")
	public ResponseEntity<ResponseDTO> selectByVehicleReservation(@RequestParam("id") Long id) {
		log.info(METHOD_NAME + "- selectByVehicleReservation");

		return ResponseEntity.ok().body(vehicleService.selectByVehicleReservation(id));
	}

	@GetMapping(path = "/own/reservation/next")

	public ResponseEntity<ResponseDTO> soonReservationMyTime(@AuthenticationPrincipal PrincipalDetails principalDetails) {

		log.info(METHOD_NAME + "- soonReservationMyTime");

		Long empId = principalDetails.getEmployee().getId();


		return ResponseEntity.ok().body(vehicleService.soonAndIngReservationMyTime(empId, 1));
	}

	@GetMapping(path = "/own/reservation/ongoing")
	public ResponseEntity<ResponseDTO> ingReservationMyTime(@AuthenticationPrincipal PrincipalDetails principalDetails) {

		log.info(METHOD_NAME + "- ingReservationMyTime");

		Long empId = principalDetails.getEmployee().getId();


		return ResponseEntity.ok().body(vehicleService.soonAndIngReservationMyTime(empId, 0));
	}

}
