package com.douzone.server.service;

import com.douzone.server.config.utils.ResponseDTO;
import com.douzone.server.dto.vehicle.VehicleReqDTO;
import com.douzone.server.dto.vehicle.VehicleReservationDTO;
import com.douzone.server.entity.Employee;
import com.douzone.server.entity.Vehicle;
import com.douzone.server.entity.VehicleBookmark;
import com.douzone.server.entity.VehicleReservation;
import com.douzone.server.repository.VehicleBookmarkRepository;
import com.douzone.server.repository.VehicleRepository;
import com.douzone.server.repository.VehicleReservationRepository;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.Local;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static com.douzone.server.config.utils.Msg.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class VehicleService {
	private static final String METHOD_NAME = VehicleService.class.getName();
	private final VehicleRepository vehicleRepository;
	private final VehicleReservationRepository vehicleReservationRepository;
	private final VehicleBookmarkRepository vehicleBookmarkRepository;

	@Transactional
	public ResponseDTO createReservation(VehicleReservationDTO vehicleReservationDTO, Long empId) {
		log.info(METHOD_NAME + "- createReservation");

		return Optional.of(new ResponseDTO())
				.filter(u -> empId > 0)
				.map(res -> {
					vehicleReservationRepository.save(
							VehicleReservation.builder()
									.vehicle(Vehicle.builder().id((long) vehicleReservationDTO.getVehicleId()).build())
									.employee(Employee.builder().id((long) vehicleReservationDTO.getEmpId()).build())
									.reason(vehicleReservationDTO.getReason())
									.title(vehicleReservationDTO.getTitle())
									.startedAt(vehicleReservationDTO.getStartedAt())
									.endedAt(vehicleReservationDTO.getEndedAt())
									.build()
					);
					return ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_RESERVE);
				}).orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_RESERVE));
	}

	@Transactional
	public ResponseDTO createBookmark(Long empId, Long vId) {
		log.info(METHOD_NAME + "- createBookmark");

		return Optional.of(new ResponseDTO())
				.filter(u -> empId > 0)
				.filter(v -> vId > 0)
				.map(v -> {
					vehicleBookmarkRepository.save(
							VehicleBookmark.builder()
									.vehicle(Vehicle.builder().id(vId).build())
									.employee(Employee.builder().id(empId).build())
									.build()
					);
					return ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_BOOKMARK);
				}).orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_BOOKMARK));
	}

	@Transactional(readOnly = true)
	public ResponseDTO findAllReserved() {
		log.info(METHOD_NAME + "- findAllReserved");

		return Optional.of(new ResponseDTO())
				.map(u -> ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_FIND_ALL, vehicleRepository.findAllReserved()))
				.orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_FIND_ALL + "결과값이 존재하지 않습니다."));
	}

	@Transactional(readOnly = true)
	public ResponseDTO findAllReservedPaging(int pageNum) {
		log.info(METHOD_NAME + "- findAllReservedPaging");

		return Optional.of(new ResponseDTO())
				.map(u -> (pageNum < 0) ?
						ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_FIND_ALL + "잘못된 파라미터가 전달되었습니다.") :
						ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_FIND_ALL + " 페이지번호 : " + pageNum, vehicleRepository.findAllReservedPaging(PageRequest.of(pageNum, 5))))
				.orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_FIND_ALL + "결과값이 존재하지 않습니다."));
	}

	@Transactional(readOnly = true)
	public ResponseDTO findAllUnreserved() {
		log.info(METHOD_NAME + "- findAllUnreserved");

		return Optional.of(new ResponseDTO())
				.map(u -> ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_FIND_NONE, vehicleRepository.findAllUnreserved(LocalDateTime.now().plusHours(1L))))
				.orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_FIND_NONE + "결과값이 존재하지 않습니다."));
	}

	@Transactional(readOnly = true)
	public ResponseDTO findTypeReserved(String model) {
		log.info(METHOD_NAME + "- findTypeReserved");
		return Optional.of(new ResponseDTO())
				.map(u -> (model == null || model.equals("")) ?
						ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_FIND_TYPE + "파라미터가 전달되지 않았습니다.") :
						ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_FIND_TYPE, vehicleRepository.findTypeReserved(model)))
				.orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_FIND_TYPE + "결과값이 존재하지 않습니다."));
	}

	@Transactional(readOnly = true)
	public ResponseDTO findDateReserved(String start, String end) {
		log.info(METHOD_NAME + "- findDateReserved");

		return Optional.of(new ResponseDTO())
				.map(u -> (start == null || start.equals("") || end == null || end.equals("")) ?
						ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_FIND_DATE + "파라미터가 전달되지 않았습니다.") :
						ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_FIND_DATE, vehicleRepository.findDateReserved(
								LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), LocalDateTime.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))))
				.orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_FIND_DATE + "결과값이 존재하지 않습니다."));
	}

	@Transactional(readOnly = true)
	public ResponseDTO findEmpBefore(Long id) {
		log.info(METHOD_NAME + "- findEmpBefore");

		return Optional.of(new ResponseDTO())
				.map(u -> (id != null) ?
						ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_BEFORE, vehicleRepository.findEmpBefore(id)) :
						ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_FIND_DATE + "파라미터가 전달되지 않았습니다.")
				).orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_BEFORE + "결과값이 존재하지 않습니다."));
	}

	@Transactional(readOnly = true)
	public ResponseDTO findEmpAfter(Long id) {
		log.info(METHOD_NAME + "- findEmpAfter");

		return Optional.of(new ResponseDTO())
				.map(u -> (id != null) ?
						ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_AFTER, vehicleRepository.findEmpAfter(id)) :
						ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_FIND_DATE + "파라미터가 전달되지 않았습니다.")
				).orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_AFTER + "결과값이 존재하지 않습니다."));
	}

	@Transactional(readOnly = true)
	public ResponseDTO findWeekVehicle() {
		log.info(METHOD_NAME + "- findWeekVehicle");

		return Optional.of(new ResponseDTO())
				.map(u -> ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_BEST_WEEK, vehicleRepository.findWeekVehicle(LocalDateTime.now().minusDays(7L))))
				.orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_BEST_WEEK + "결과값이 존재하지 않습니다."));
	}

	@Transactional(readOnly = true)
	public ResponseDTO findWeekDate() {
		log.info(METHOD_NAME + "- findWeekDate");

		return Optional.of(new ResponseDTO()).map(u -> {
			Map<String, Integer> map = new HashMap<>();
			List<String> result = vehicleRepository.findWeekDate(LocalDateTime.now().minusDays(7L));
			for (String s : result) {
				if (!map.containsKey(s)) map.put(s, 1);
				map.put(s, map.get(s) + 1);
			}
			List<Map.Entry<String, Integer>> entry = new LinkedList<>(map.entrySet());
			entry.sort((o1, o2) -> o2.getValue() - o1.getValue());
			return ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_BEST_DATE, entry);
		}).orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_BEST_DATE + "결과값을 조회에 실패하였습니다."));
	}

	@Transactional(readOnly = true)
	public ResponseDTO findRecentVehicle() {
		log.info(METHOD_NAME + "- findRecentVehicle");

		return Optional.of(new ResponseDTO())
				.map(u -> ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_BEST_DATE, vehicleRepository.findRecentVehicle()))
				.orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_RECENT + "결과값이 존재하지 않습니다."));
	}

	@Transactional(readOnly = true)
	public ResponseDTO findMarkVehicle(String empNo) {
		log.info(METHOD_NAME + "- findMarkVehicle");

		return Optional.of(new ResponseDTO())
				.map(u -> ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_FIND_MARK, vehicleBookmarkRepository.findMarkVehicle(empNo)))
				.orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_FIND_MARK + "결과값이 존재하지 않습니다."));
	}

	@Transactional
	public ResponseDTO findMarkBest() {
		log.info(METHOD_NAME + "- findMarkBest");

		return Optional.of(new ResponseDTO())
				.map(u -> ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_BEST_MARK, vehicleBookmarkRepository.findMarkBest(PageRequest.of(0, 3))))
				.orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_BEST_MARK + "결과값이 존재하지 않습니다."));
	}

	@Transactional
	public ResponseDTO updateReserved(VehicleReqDTO vehicleReqDTO, Long id) {
		log.info(METHOD_NAME + "- updateReserved");

		return Optional.of(new ResponseDTO())
				.map(u -> Optional.ofNullable(vehicleReqDTO).map(VehicleReqDTO::getId))
				.filter(Optional::isPresent)
				.map(v -> vehicleReservationRepository.findById(vehicleReqDTO.getId()))
				.filter(Optional::isPresent)
				.filter(res -> res.get().getEmployee().getId() == id)
				.map(ans -> {
					ans.get().updateReserved(vehicleReqDTO);
					return ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_UPDATE);
				}).orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_UPDATE + "결과값이 존재하지 않습니다."));
	}

	@Transactional
	public ResponseDTO deleteReserved(Long id, Long empId) {
		log.info(METHOD_NAME + "- deleteReserved");

		return Optional.of(new ResponseDTO())
				.filter(u -> (id != null))
				.map(v -> vehicleReservationRepository.findById(id))
				.map(res -> res.isPresent() ? res.get() : -1L)
				.filter(uid -> uid == empId)
				.map(fi -> {
					vehicleReservationRepository.deleteById(id);
					return (vehicleReservationRepository.findById(id).isPresent()) ?
							ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_DELETE + "결과값을 조회에 실패하였습니다.") :
							ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_DELETE);
				}).orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_DELETE + "결과값이 존재하지 않습니다."));
	}

	@Transactional
	public ResponseDTO deleteMark(Long id) {
		log.info(METHOD_NAME + "- deleteMark");

		return Optional.of(new ResponseDTO())
				.filter(u -> (id != null))
				.map(v -> {
					vehicleBookmarkRepository.deleteById(id);
					return (vehicleBookmarkRepository.findById(id).isPresent()) ?
							ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_DELETE_MARK + "결과값을 조회에 실패하였습니다.") :
							ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_DELETE_MARK);
				}).orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_DELETE_MARK + "결과값이 존재하지 않습니다."));
	}

	@Transactional
	public ResponseDTO findVehicleReserved(Long id) {
		log.info(METHOD_NAME + "- findVehicleReserved");

		return Optional.of(new ResponseDTO())
				.filter(u -> (id != null))
				.map(v -> vehicleRepository.findCustom(id))
				.map(res -> res.map(iVehicleListResDTO -> ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_FIND_NO, iVehicleListResDTO)).orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_FIND_NO + "결과값을 조회에 실패하였습니다.")))
				.orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_FIND_NO + "결과값이 존재하지 않습니다."));
	}

	@Transactional
	public ResponseDTO soonReservationMyTime(Long empId) {
		log.info(METHOD_NAME + "- soonReservationMyTime");

		LocalDateTime soon = vehicleReservationRepository.soonReservationMyTime(empId, PageRequest.of(0,1)).getStartedAt();
		LocalDateTime now = LocalDateTime.now();
		Long time = ChronoUnit.SECONDS.between(now, soon);

		return Optional.of(new ResponseDTO())
				.filter(u -> (empId != null))
				.map(u -> ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_SOON, time))
				.orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_SOON + "결과값이 존재하지 않습니다."));
	}

	@Transactional
	public ResponseDTO ingReservationMyTime(Long empId) {
		log.info(METHOD_NAME + "- IngReservationMyTime");

		LocalDateTime ing = vehicleReservationRepository.ingReservationMyTime(empId, PageRequest.of(0,1)).getEndedAt();
		LocalDateTime now = LocalDateTime.now();
		Long time = ChronoUnit.SECONDS.between(now, ing);

		return Optional.of(new ResponseDTO())
				.filter(u -> (empId != null))
				.map(u -> ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_ING, time))
				.orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_ING + "결과값이 존재하지 않습니다."));

	}
}
