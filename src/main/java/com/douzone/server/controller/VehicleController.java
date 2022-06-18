package com.douzone.server.controller;

import com.douzone.server.config.security.auth.PrincipalDetails;
import com.douzone.server.config.utils.ResponseDTO;
import com.douzone.server.dto.vehicle.VehicleReqDTO;
import com.douzone.server.dto.vehicle.VehicleReservationDTO;
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
 * createReservation - 차량 예약 등록 /creation/reservation - POST
 * createBookmark - 차량 즐겨찾기 등록 /creation/bookmark - POST
 * Read:
 * findAllReserved - 차량 전체 예약 현황 조회 /list/reservation - GET
 * findAllUnreserved - 차량 전체 미예약 현황 조회 /list/stock - GET
 * findAllReservedPaging - 차량 예약 페이징 처리 /list/reservation/paging - GET
 * findTypeReserved - 차량 종류별 예약 현황 조회 /list/type - GET
 * findDateReserved - 특정 일시 차량 예약 현황 조회 /list/time - GET
 * findEmpBefore - 내 예약 조회 - 과거 /list/own/past - GET
 * findEmpAfter - 내 예약 조회 - 미래 /list/own/current - GET
 * findWeekVehicle - 7일 동안 가장 많이 예약된 차량 /best/vehicle - GET
 * findWeekDate - 7일 동안 가장 많이 예약한 시간대 /best/time - GET
 * findRecentVehicle - 최근 예약된 차량 조회 /list/recent - GET
 * findMarkVehicle - 내가 즐겨찾기한 차량 조회 /list/own/mark - GET
 * findMarkBest - 즐겨찾기가 많은 차량 Top 3 조회 /best/mark - GET
 * findVehicleReserved - 수정 전 차량 단일 조회 /reservation - GET
 * IngReservationMyTime - 내가 현재 진행중인 예약 종료 시간 조회 /own/reservation/ongoing - GET
 * soonReservationMyTime - 나의 다음 예약 시작 시간 조회 /own/reservation/next - GET
 * Update:
 * updateReserved - 내 차량 예약 현황 수정 /modification - PATCH
 * Delete:
 * deleteReserved - 내 차량 예약 삭제 /elimination
 * deleteMark - 내 즐겨찾기 차량 삭제 /elimination/mark
 * 차량 등록, 수정, 삭제 (AdminController)
 * createVehicle - 차량 등록 /creation/vehicle
 * updateVehicle - 차량 수정 /modification/vehicle
 * deleteVehicle - 차량 삭제 /elimination/vehicle
 */

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/emp/vehicle")
public class VehicleController {
	private static final String METHOD_NAME = VehicleController.class.getName();
	private final VehicleService vehicleService;

	@PostMapping(path = "/creation/reservation")
	public ResponseEntity<ResponseDTO> createReservation(@RequestBody @Valid VehicleReservationDTO vehicleReservationDTO,
														 @AuthenticationPrincipal PrincipalDetails principalDetails) {
		log.info(METHOD_NAME + "- createReservation");
		Long empId = principalDetails.getEmployee().getId();

		return ResponseEntity.ok().body(vehicleService.createReservation(vehicleReservationDTO, empId));
	}

	@PostMapping(path = "/creation/bookmark")
	public ResponseEntity<ResponseDTO> createBookmark(@RequestParam(value = "vId") Long vId,
													  @AuthenticationPrincipal PrincipalDetails principalDetails) {
		log.info(METHOD_NAME + "- createBookmark");
		Long empId = principalDetails.getEmployee().getId();

		return ResponseEntity.ok().body(vehicleService.createBookmark(empId, vId));
	}

	@GetMapping(path = "/list/reservation")
	public ResponseEntity<ResponseDTO> findAllReserved() {
		log.info(METHOD_NAME + "- findAllReserved");

		return ResponseEntity.ok().body(vehicleService.findAllReserved());
	}

	@GetMapping(path = "/list/reservation/paging")
	public ResponseEntity<ResponseDTO> findAllReservedPaging(@RequestBody Map<String, Integer> pageNum) {
		log.info(METHOD_NAME + "- findAllReservedPaging");

		return ResponseEntity.ok().body(vehicleService.findAllReservedPaging(pageNum.get("page")));
	}

	@GetMapping(path = "/list/stock")
	public ResponseEntity<ResponseDTO> findAllUnreserved() {
		log.info(METHOD_NAME + "- findAllUnreserved");

		return ResponseEntity.ok().body(vehicleService.findAllUnreserved());
	}

	@GetMapping(path = "/list/type")
	public ResponseEntity<ResponseDTO> findTypeReserved(@RequestBody Map<String, String> model) {
		log.info(METHOD_NAME + "- findTypeReserved");
		System.out.println(model);
		return ResponseEntity.ok().body(vehicleService.findTypeReserved(model.get("model")));
	}

	@GetMapping(path = "/list/time")
	public ResponseEntity<ResponseDTO> findDateReserved(@RequestBody Map<String, String> model) {
		log.info(METHOD_NAME + "- findDateReserved");

		return ResponseEntity.ok().body(vehicleService.findDateReserved(model.get("start"), model.get("end")));
	}

	@GetMapping(path = "/list/own/past")
	public ResponseEntity<ResponseDTO> findEmpBefore(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		log.info(METHOD_NAME + "- findEmpBefore");
		Long id = principalDetails.getEmployee().getId();

		return ResponseEntity.ok().body(vehicleService.findEmpBefore(id));
	}

	@GetMapping(path = "/list/own/current")
	public ResponseEntity<ResponseDTO> findEmpAfter(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		log.info(METHOD_NAME + "- findEmpAfter");
		Long id = principalDetails.getEmployee().getId();

		return ResponseEntity.ok().body(vehicleService.findEmpAfter(id));
	}

	@GetMapping(path = "/best/vehicle")
	public ResponseEntity<ResponseDTO> findWeekVehicle() {
		log.info(METHOD_NAME + "- findWeekVehicle");

		return ResponseEntity.ok().body(vehicleService.findWeekVehicle());
	}

	@GetMapping(path = "/best/time")
	public ResponseEntity<ResponseDTO> findWeekDate() {
		log.info(METHOD_NAME + "- findWeekDate");

		return ResponseEntity.ok().body(vehicleService.findWeekDate());
	}

	@GetMapping(path = "/list/recent")
	public ResponseEntity<ResponseDTO> findRecentVehicle() {
		log.info(METHOD_NAME + "- findRecentVehicle");

		return ResponseEntity.ok().body(vehicleService.findRecentVehicle());
	}

	@GetMapping(path = "/list/own/mark")
	public ResponseEntity<ResponseDTO> findMarkVehicle(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		log.info(METHOD_NAME + "- findMarkVehicle");
		String empNo = principalDetails.getEmployee().getEmpNo();

		return ResponseEntity.ok().body(vehicleService.findMarkVehicle(empNo));
	}

	@GetMapping(path = "/best/mark")
	public ResponseEntity<ResponseDTO> findMarkBest() {
		log.info(METHOD_NAME + "- findMarkBest");

		return ResponseEntity.ok().body(vehicleService.findMarkBest());
	}

	@PatchMapping(path = "/modification")
	public ResponseEntity<ResponseDTO> updateReserved(@RequestBody @Valid VehicleReqDTO vehicleReqDTO,
													  @AuthenticationPrincipal PrincipalDetails principalDetails) {
		log.info(METHOD_NAME + "- updateReserved");

		Long id = principalDetails.getEmployee().getId();

		return ResponseEntity.ok().body(vehicleService.updateReserved(vehicleReqDTO, id));
	}

	@DeleteMapping(path = "/elimination")
	public ResponseEntity<ResponseDTO> deleteReserved(@RequestParam("id") Long id, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		log.info(METHOD_NAME + "- deleteReserved");
		Long empId = principalDetails.getEmployee().getId();
		return ResponseEntity.ok().body(vehicleService.deleteReserved(id, empId));
	}

	@DeleteMapping(path = "/elimination/mark")
	public ResponseEntity<ResponseDTO> deleteMark(@RequestParam("id") Long id) {
		log.info(METHOD_NAME + "- deleteMark");

		return ResponseEntity.ok().body(vehicleService.deleteMark(id));
	}

	@GetMapping(path = "/reservation")
	public ResponseEntity<ResponseDTO> findVehicleReserved(@RequestParam("id") Long id) {
		log.info(METHOD_NAME + "- findVehicleReserved");

		return ResponseEntity.ok().body(vehicleService.findVehicleReserved(id));
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
