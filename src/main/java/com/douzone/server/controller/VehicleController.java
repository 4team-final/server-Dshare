package com.douzone.server.controller;

import com.douzone.server.config.security.auth.PrincipalDetails;
import com.douzone.server.config.utils.ResponseDTO;
import com.douzone.server.dto.vehicle.VehicleParseDTO;
import com.douzone.server.dto.vehicle.VehicleSearchDTO;
import com.douzone.server.service.VehicleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
 * selectByVariousColumns - 변수 조건을 통한 차량 예약 조회 /list/reservation/various - GET
 * Update:
 * updateByMyReservation - 내 차량 예약 현황 수정 /modification - PATCH
 * earlyReturnOfVehicle - 이용 차량 조기 반납 /modification/return - PATCH
 * Delete:
 * deleteByMyReservation - 내 차량 예약 삭제 /elimination
 * deleteByMyBookMark - 내 즐겨찾기 차량 삭제 /elimination/mark
 */
//dshare 화이팅!!!!

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
		return ResponseEntity.ok().body(vehicleService.registerByVehicleReservation(vehicleParseDTO, principalDetails.getEmployee().getId()));
	}

	@PostMapping(path = "/creation/bookmark")
	public ResponseEntity<ResponseDTO> registerByVehicleBookmark(@RequestParam(value = "vId") Long vId,
																 @AuthenticationPrincipal PrincipalDetails principalDetails) {
		log.info(METHOD_NAME + "- registerByVehicleBookmark");
		return ResponseEntity.ok().body(vehicleService.registerByVehicleBookmark(principalDetails.getEmployee().getId(), vId));
	}

	@GetMapping(path = "/list/reservation")
	public ResponseEntity<ResponseDTO> findByAllReservation() {
		log.info(METHOD_NAME + "- findByAllReservation");
		return ResponseEntity.ok().body(vehicleService.findByAllReservation());
	}

	@GetMapping(path = "/list/reservation/paging")
	public ResponseEntity<ResponseDTO> findByPaginationReservation(@RequestParam("id") Long id) {
		log.info(METHOD_NAME + "- findByPaginationReservation");
		return ResponseEntity.ok().body(vehicleService.findByPaginationReservation(id));
	}
	@GetMapping(path = "/list/reservation/paging2")
	public ResponseEntity<ResponseDTO> findByPaginationReservation2(@RequestParam("id") Long id) {
		log.info(METHOD_NAME + "- findByPaginationReservation");
		return ResponseEntity.ok().body(vehicleService.findByPaginationReservation2(id));
	}


	@GetMapping(path = "/list/stock")
	public ResponseEntity<ResponseDTO> findByAllNotReservation() {
		log.info(METHOD_NAME + "- findByAllNotReservation");
		return ResponseEntity.ok().body(vehicleService.findByAllNotReservation());
	}

	@GetMapping(path = "/list/type")
	public ResponseEntity<ResponseDTO> findByModelReservation(@RequestParam("model") String model) {
		log.info(METHOD_NAME + "- findByModelReservation");
		return ResponseEntity.ok().body(vehicleService.findByModelReservation(model));
	}

	@GetMapping(path = "/list/time/{start}/{end}")
	public ResponseEntity<ResponseDTO> findByDateTimeReservation(@PathVariable("start") String start,
																 @PathVariable("end") String end) {
		log.info(METHOD_NAME + "- findByDateTimeReservation");
		return ResponseEntity.ok().body(vehicleService.findByDateTimeReservation(start, end));
	}

	@GetMapping(path = "/list/time2/{start}/{end}")
	public ResponseEntity<ResponseDTO> findByDateTimeReservation2(@PathVariable("start") String start,
																  @PathVariable("end") String end) {
		log.info(METHOD_NAME + "- findByDateTimeReservation");
		return ResponseEntity.ok().body(vehicleService.findByDateTimeReservation2(start, end));
	}

	@GetMapping(path = "/list/own/past")
	public ResponseEntity<ResponseDTO> findByMyPastReservation(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		log.info(METHOD_NAME + "- findByMyPastReservation");
		return ResponseEntity.ok().body(vehicleService.findByMyPastReservation(principalDetails.getEmployee().getId()));
	}

	@GetMapping(path = "/list/own/current")
	public ResponseEntity<ResponseDTO> findByMyCurrentReservation(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		log.info(METHOD_NAME + "- findByMyCurrentReservation");
		return ResponseEntity.ok().body(vehicleService.findByMyCurrentReservation(principalDetails.getEmployee().getId()));
	}

	@GetMapping(path = "/list/own/all")
	public ResponseEntity<ResponseDTO> findByMyReservation(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		log.info(METHOD_NAME + "- findByMyReservation");
		return ResponseEntity.ok().body(vehicleService.findByMyReservation(principalDetails.getEmployee().getId()));
	}

	@GetMapping(path = "/list/own/paging")
	public ResponseEntity<ResponseDTO> findByMyReservationPaging(@RequestParam("id") Long id, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		log.info(METHOD_NAME + "- findByMyReservationPaging");
		return ResponseEntity.ok().body(vehicleService.findByMyReservationPaging(id, principalDetails.getEmployee().getId()));
	}

	@GetMapping(path = "/best/vehicle/{datetime}")
	public ResponseEntity<ResponseDTO> weekMostReservedVehicle(@PathVariable("datetime") Long datetime) {
		log.info(METHOD_NAME + "- weekMostReservedVehicle");
		return ResponseEntity.ok().body(vehicleService.weekMostReservedVehicle(datetime));
	}

	@GetMapping(path = "/best/time/{datetime}")
	public ResponseEntity<ResponseDTO> weekMostReservedTime(@PathVariable("datetime") Long datetime) {
		log.info(METHOD_NAME + "- weekMostReservedTime");
		return ResponseEntity.ok().body(vehicleService.weekMostReservedTime(datetime));
	}

	@GetMapping(path = "/start/best/time/{datetime}")
	public ResponseEntity<ResponseDTO> weekstartMostReservedTime(@PathVariable("datetime") Long datetime) {
		log.info(METHOD_NAME + "- weekMostReservedTime");
		return ResponseEntity.ok().body(vehicleService.weekstartMostReservedTime(datetime));
	}

	@GetMapping(path = "/list/recent")
	public ResponseEntity<ResponseDTO> findByRecentReservedVehicle() {
		log.info(METHOD_NAME + "- findByRecentReservedVehicle");
		return ResponseEntity.ok().body(vehicleService.findByRecentReservedVehicle());
	}

	@GetMapping(path = "/list/own/mark")
	public ResponseEntity<ResponseDTO> findByMyBookMarkVehicle(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		log.info(METHOD_NAME + "- findByMyBookMarkVehicle");
		return ResponseEntity.ok().body(vehicleService.findByMyBookMarkVehicle(principalDetails.getEmployee().getEmpNo()));
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
		return ResponseEntity.ok().body(vehicleService.updateByMyReservation(vehicleParseDTO, principalDetails.getEmployee().getId()));
	}

	@DeleteMapping(path = "/elimination")
	public ResponseEntity<ResponseDTO> deleteByMyReservation(@RequestParam("id") Long id, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		log.info(METHOD_NAME + "- deleteByMyReservation");
		return ResponseEntity.ok().body(vehicleService.deleteByMyReservation(id, principalDetails.getEmployee().getId()));
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
		return ResponseEntity.ok().body(vehicleService.soonReservationMyTime(principalDetails.getEmployee().getId()));
	}

	@GetMapping(path = "/own/reservation/ongoing")
	public ResponseEntity<ResponseDTO> ingReservationMyTime(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		log.info(METHOD_NAME + "- ingReservationMyTime");
		return ResponseEntity.ok().body(vehicleService.ingReservationMyTime(principalDetails.getEmployee().getId()));
	}

	@PatchMapping("/modification/return")
	public ResponseEntity<ResponseDTO> earlyReturnOfVehicle(@RequestParam("id") Long id,
															@AuthenticationPrincipal PrincipalDetails principalDetails) {
		log.info(METHOD_NAME + "- earlyReturnOfVehicle");
		return ResponseEntity.ok().body(vehicleService.earlyReturnOfVehicle(id, principalDetails.getEmployee().getId()));
	}

	@PostMapping("/list/reservation/various")
	public ResponseEntity<ResponseDTO> selectByVariousColumns(@RequestBody VehicleSearchDTO vehicleSearchDTO) {
		log.info(METHOD_NAME + "- selectByVariousColumns");
		return ResponseEntity.ok().body(vehicleService.selectByVariousColumns(vehicleSearchDTO));
	}
	@PostMapping("/list/reservation/various/{page}")
	public ResponseEntity<ResponseDTO> selectByVariousColumns(@RequestBody VehicleSearchDTO vehicleSearchDTO, @PathVariable("page")long page) {
		log.info(METHOD_NAME + "- selectByVariousColumns");
		return ResponseEntity.ok().body(vehicleService.selectByVariousColumns(vehicleSearchDTO, page));
	}

	@GetMapping("/list/vehicle/all")
	public ResponseEntity<ResponseDTO> selectByAllVehicle() {
		log.info(METHOD_NAME + "- selectByAllVehicle");
		return ResponseEntity.ok().body(vehicleService.selectByAllVehicle());
	}
}
