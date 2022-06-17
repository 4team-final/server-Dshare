package com.douzone.server.service;

import com.douzone.server.config.utils.ResponseDTO;
import com.douzone.server.dto.vehicle.*;
import com.douzone.server.entity.Employee;
import com.douzone.server.entity.Vehicle;
import com.douzone.server.entity.VehicleBookmark;
import com.douzone.server.entity.VehicleReservation;
import com.douzone.server.repository.VehicleBookmarkRepository;
import com.douzone.server.repository.VehicleRepository;
import com.douzone.server.repository.VehicleReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
	public Integer createReservation(VehicleReservationDTO vehicleReservationDTO) {
		log.info(METHOD_NAME + "- createReservation");
		try {
			VehicleReservation vehicleReservation = VehicleReservation.builder()
					.vehicle(Vehicle.builder().id(1L).build())
					.employee(Employee.builder().id(1L).build())
					.reason(vehicleReservationDTO.getReason())
					.title(vehicleReservationDTO.getTitle())
					.build();
			VehicleReservation result = vehicleReservationRepository.save(vehicleReservation);
			if (result.getId() == null) return 0;
			return 1;
		} catch (Exception e) {
			log.error("SERVER ERROR", e);
		}
		return null;
	}

	@Transactional
	public Integer createBookmark() {
		log.info(METHOD_NAME + "- createBookmark");

		try {
			VehicleBookmark vehicleBookmark = VehicleBookmark.builder()
					.employee(Employee.builder().id(1L).build())
					.vehicle(Vehicle.builder().id(1L).build())
					.build();
			VehicleBookmark result = vehicleBookmarkRepository.save(vehicleBookmark);
			if (result.getId() == null) return 0;
			return 1;
		} catch (Exception e) {
			log.error("SERVER ERROR", e);
		}
		return null;
	}

	@Transactional(readOnly = true)
	public ResponseDTO findAllReserved() {
		log.info(METHOD_NAME + "- findAllReserved");
		try {
			List<IVehicleListResDTO> list = vehicleRepository.findAllReserved();

			if (list == null)
				return ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_FIND_ALL + "결과값이 존재하지 않습니다.");

			return ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_FIND_ALL, list);
		} catch (DataAccessException dae) {
			log.error("SQL 문법, 제약 조건 위배 혹은 DB 서버와의 연결을 실패하였습니다.", dae);
		} catch (TransactionSystemException tse) {
			log.error("트랜잭션 커밋을 실패하였습니다.", tse);
		} catch (ConversionFailedException cfe) {
			log.error("서비스로의 리턴 형식이 잘못되었습니다.", cfe);
		} catch (Exception e) {
			log.error("SERVER ERROR", e);
		}
		return ResponseDTO.fail(HttpStatus.INTERNAL_SERVER_ERROR, FAIL_VEHICLE_FIND_ALL);
	}

	@Transactional(readOnly = true)
	public ResponseDTO findAllReservedPaging(int pageNum) {
		log.info(METHOD_NAME + "- findAllReservedPaging");
		try {
			if (pageNum < 0)
				return ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_FIND_ALL + "잘못된 파라미터가 전달되었습니다.");

			PageRequest pageRequest = PageRequest.of(pageNum, 5);
			List<IVehicleListResDTO> list = vehicleRepository.findAllReservedPaging(pageRequest);

			if (list == null)
				return ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_FIND_ALL + "결과값이 존재하지 않습니다.");

			return ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_FIND_ALL + " 페이지번호 : " + pageNum, list);
		} catch (DataAccessException dae) {
			log.error("SQL 문법, 제약 조건 위배 혹은 DB 서버와의 연결을 실패하였습니다.", dae);
		} catch (TransactionSystemException tse) {
			log.error("트랜잭션 커밋을 실패하였습니다.", tse);
		} catch (ConversionFailedException cfe) {
			log.error("서비스로의 리턴 형식이 잘못되었습니다.", cfe);
		} catch (NumberFormatException ne) {
			log.error("파라미터 형식이 잘못되었습니다.", ne);
		} catch (Exception e) {
			log.error("SERVER ERROR", e);
		}
		return ResponseDTO.fail(HttpStatus.INTERNAL_SERVER_ERROR, FAIL_VEHICLE_FIND_ALL);
	}

	@Transactional(readOnly = true)
	public ResponseDTO findAllUnreserved() {
		log.info(METHOD_NAME + "- findAllUnreserved");
		try {
			List<Vehicle> list = vehicleRepository.findAllUnreserved(LocalDateTime.now().plusHours(1L));

			if (list == null)
				return ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_FIND_NONE + "결과값이 존재하지 않습니다.");

			return ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_FIND_NONE, list);
		} catch (DataAccessException dae) {
			log.error("SQL 문법, 제약 조건 위배 혹은 DB 서버와의 연결을 실패하였습니다.", dae);
		} catch (TransactionSystemException tse) {
			log.error("트랜잭션 커밋을 실패하였습니다.", tse);
		} catch (ConversionFailedException cfe) {
			log.error("서비스로의 리턴 형식이 잘못되었습니다.", cfe);
		} catch (Exception e) {
			log.error("SERVER ERROR", e);
		}
		return ResponseDTO.fail(HttpStatus.INTERNAL_SERVER_ERROR, FAIL_VEHICLE_FIND_NONE);
	}

	@Transactional(readOnly = true)
	public ResponseDTO findTypeReserved(String model) {
		log.info(METHOD_NAME + "- findTypeReserved");
		try {
			if (model == null || model.equals(""))
				return ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_FIND_TYPE + "파라미터가 전달되지 않았습니다.");

			List<IVehicleListResDTO> list = vehicleRepository.findTypeReserved(model);

			if (list == null)
				return ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_FIND_TYPE + "결과값이 존재하지 않습니다.");

			return ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_FIND_TYPE, list);
		} catch (DataAccessException dae) {
			log.error("SQL 문법, 제약 조건 위배 혹은 DB 서버와의 연결을 실패하였습니다.", dae);
		} catch (TransactionSystemException tse) {
			log.error("트랜잭션 커밋을 실패하였습니다.", tse);
		} catch (ConversionFailedException cfe) {
			log.error("서비스로의 리턴 형식이 잘못되었습니다.", cfe);
		} catch (NumberFormatException ne) {
			log.error("파라미터 형식이 잘못되었습니다.", ne);
		} catch (Exception e) {
			log.error("SERVER ERROR", e);
		}
		return ResponseDTO.fail(HttpStatus.INTERNAL_SERVER_ERROR, FAIL_VEHICLE_FIND_TYPE);
	}

	@Transactional(readOnly = true)
	public ResponseDTO findDateReserved(String start, String end) {
		log.info(METHOD_NAME + "- findDateReserved");
		try {
			if (start == null || start.equals("") || end == null || end.equals(""))
				return ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_FIND_DATE + "파라미터가 전달되지 않았습니다.");

			LocalDateTime startDate = LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			LocalDateTime endDate = LocalDateTime.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			List<IVehicleListResDTO> list = vehicleRepository.findDateReserved(startDate, endDate);

			if (list == null)
				return ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_FIND_DATE + "결과값이 존재하지 않습니다.");

			return ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_FIND_DATE, list);
		} catch (DataAccessException dae) {
			log.error("SQL 문법, 제약 조건 위배 혹은 DB 서버와의 연결을 실패하였습니다.", dae);
		} catch (TransactionSystemException tse) {
			log.error("트랜잭션 커밋을 실패하였습니다.", tse);
		} catch (ConversionFailedException cfe) {
			log.error("서비스로의 리턴 형식이 잘못되었습니다.", cfe);
		} catch (DateTimeParseException pe) {
			log.error("시간 변환에 실패하였습니다.", pe);
		} catch (NumberFormatException ne) {
			log.error("파라미터 형식이 잘못되었습니다.", ne);
		} catch (Exception e) {
			log.error("SERVER ERROR", e);
		}
		return ResponseDTO.fail(HttpStatus.INTERNAL_SERVER_ERROR, FAIL_VEHICLE_FIND_DATE);
	}

	@Transactional(readOnly = true)
	public ResponseDTO findEmpBefore(Long id) {
		log.info(METHOD_NAME + "- findEmpBefore");
		try {
			if (id == null)
				return ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_BEFORE + "파라미터가 전달되지 않았습니다.");

			List<IVehicleEmpResDTO> list = vehicleRepository.findEmpBefore(id);

			if (list == null)
				return ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_BEFORE + "결과값이 존재하지 않습니다.");

			return ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_BEFORE, list);
		} catch (DataAccessException dae) {
			log.error("SQL 문법, 제약 조건 위배 혹은 DB 서버와의 연결을 실패하였습니다.", dae);
		} catch (TransactionSystemException tse) {
			log.error("트랜잭션 커밋을 실패하였습니다.", tse);
		} catch (ConversionFailedException cfe) {
			log.error("서비스로의 리턴 형식이 잘못되었습니다.", cfe);
		} catch (NumberFormatException ne) {
			log.error("파라미터 형식이 잘못되었습니다.", ne);
		} catch (Exception e) {
			log.error("SERVER ERROR", e);
		}
		return ResponseDTO.fail(HttpStatus.INTERNAL_SERVER_ERROR, FAIL_VEHICLE_BEFORE);
	}

	@Transactional(readOnly = true)
	public ResponseDTO findEmpAfter(Long id) {
		log.info(METHOD_NAME + "- findEmpAfter");
		try {
			if (id == null)
				return ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_AFTER + "파라미터가 전달되지 않았습니다.");

			List<IVehicleEmpResDTO> list = vehicleRepository.findEmpAfter(id);

			if (list == null)
				return ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_AFTER + "결과값이 존재하지 않습니다.");

			return ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_AFTER, list);
		} catch (DataAccessException dae) {
			log.error("SQL 문법, 제약 조건 위배 혹은 DB 서버와의 연결을 실패하였습니다.", dae);
		} catch (TransactionSystemException tse) {
			log.error("트랜잭션 커밋을 실패하였습니다.", tse);
		} catch (ConversionFailedException cfe) {
			log.error("서비스로의 리턴 형식이 잘못되었습니다.", cfe);
		} catch (NumberFormatException ne) {
			log.error("파라미터 형식이 잘못되었습니다.", ne);
		} catch (Exception e) {
			log.error("SERVER ERROR", e);
		}
		return ResponseDTO.fail(HttpStatus.INTERNAL_SERVER_ERROR, FAIL_VEHICLE_AFTER);
	}

	@Transactional(readOnly = true)
	public ResponseDTO findWeekVehicle() {
		log.info(METHOD_NAME + "- findWeekVehicle");
		try {
			List<IVehicleRankResDTO> result = vehicleRepository.findWeekVehicle(LocalDateTime.now().minusDays(7L), PageRequest.of(0, 1));
			if (result == null)
				return ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_BEST_WEEK + "결과값이 존재하지 않습니다.");

			return ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_BEST_WEEK, result);
		} catch (DataAccessException dae) {
			log.error("SQL 문법, 제약 조건 위배 혹은 DB 서버와의 연결을 실패하였습니다.", dae);
		} catch (TransactionSystemException tse) {
			log.error("트랜잭션 커밋을 실패하였습니다.", tse);
		} catch (ConversionFailedException cfe) {
			log.error("서비스로의 리턴 형식이 잘못되었습니다.", cfe);
		} catch (Exception e) {
			log.error("SERVER ERROR", e);
		}
		return ResponseDTO.fail(HttpStatus.INTERNAL_SERVER_ERROR, FAIL_VEHICLE_BEST_WEEK);
	}

	@Transactional(readOnly = true)
	public ResponseDTO findWeekDate() {
		log.info(METHOD_NAME + "- findWeekDate");
		try {
			Map<String, Integer> map = new HashMap<>();
			List<String> result = vehicleRepository.findWeekDate(LocalDateTime.now().minusDays(7L));

			if (result == null)
				return ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_BEST_DATE + "결과값을 조회에 실패하였습니다.");

			for (String s : result) {
				if (!map.containsKey(s)) map.put(s, 1);
				map.put(s, map.get(s) + 1);
			}
			List<Map.Entry<String, Integer>> entry = new LinkedList<>(map.entrySet());
			entry.sort((o1, o2) -> o2.getValue() - o1.getValue());
			String res = entry.get(0).getKey();

			if (res == null || res.equals(""))
				return ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_BEST_DATE + "결과값이 존재하지 않습니다.");

			return ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_BEST_DATE, res);
		} catch (DataAccessException dae) {
			log.error("SQL 문법, 제약 조건 위배 혹은 DB 서버와의 연결을 실패하였습니다.", dae);
		} catch (TransactionSystemException tse) {
			log.error("트랜잭션 커밋을 실패하였습니다.", tse);
		} catch (ConversionFailedException cfe) {
			log.error("서비스로의 리턴 형식이 잘못되었습니다.", cfe);
		} catch (Exception e) {
			log.error("SERVER ERROR", e);
		}
		return ResponseDTO.fail(HttpStatus.INTERNAL_SERVER_ERROR, FAIL_VEHICLE_BEST_DATE);
	}

	@Transactional(readOnly = true)
	public ResponseDTO findRecentVehicle() {
		log.info(METHOD_NAME + "- findRecentVehicle");
		try {
			List<IVehicleDateResDTO> result = vehicleRepository.findRecentVehicle();

			if (result == null)
				return ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_RECENT + "결과값이 존재하지 않습니다.");

			return ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_RECENT, result);
		} catch (DataAccessException dae) {
			log.error("SQL 문법, 제약 조건 위배 혹은 DB 서버와의 연결을 실패하였습니다.", dae);
		} catch (TransactionSystemException tse) {
			log.error("트랜잭션 커밋을 실패하였습니다.", tse);
		} catch (ConversionFailedException cfe) {
			log.error("서비스로의 리턴 형식이 잘못되었습니다.", cfe);
		} catch (Exception e) {
			log.error("SERVER ERROR", e);
		}
		return ResponseDTO.fail(HttpStatus.INTERNAL_SERVER_ERROR, FAIL_VEHICLE_RECENT);
	}

	@Transactional(readOnly = true)
	public ResponseDTO findMarkVehicle(String empNo) {
		log.info(METHOD_NAME + "- findMarkVehicle");
		try {
			List<Vehicle> list = vehicleRepository.findMarkVehicle(empNo);
			if (list == null)
				return ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_FIND_MARK);

			return ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_FIND_MARK, list);
		} catch (Exception e) {
			log.error("SERVER ERROR", e);
		}
		return ResponseDTO.fail(HttpStatus.INTERNAL_SERVER_ERROR, FAIL_VEHICLE_FIND_MARK);
	}

	@Transactional
	public ResponseDTO findMarkBest() {
		log.info(METHOD_NAME + "- findMarkBest");
		try {
			List<Vehicle> list = vehicleRepository.findMarkBest();

			if (list == null)
				return ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_BEST_MARK);

			return ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_BEST_MARK, list);
		} catch (Exception e) {
			log.error("SERVER ERROR", e);
		}
		return ResponseDTO.fail(HttpStatus.INTERNAL_SERVER_ERROR, FAIL_VEHICLE_BEST_MARK);
	}

	@Transactional
	public ResponseDTO updateReserved(VehicleReqDTO vehicleReqDTO) {
		log.info(METHOD_NAME + "- updateReserved");
		try {
			if (vehicleReqDTO == null || vehicleReqDTO.getId() == null)
				return ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_UPDATE);

			Optional<VehicleReservation> data = vehicleReservationRepository.findById(vehicleReqDTO.getId());

			if (data.isPresent()) {
				VehicleReservation vehicleReservation = data.get();

				vehicleReservation.updateReserved(vehicleReqDTO);

				return ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_UPDATE);
			}
			return ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_UPDATE);
		} catch (NoSuchElementException ne) {
			log.error("값이 들어갈 공간이 없습니다." + ne);
		} catch (Exception e) {
			log.error("SERVER ERROR" + e);
		}
		return ResponseDTO.fail(HttpStatus.INTERNAL_SERVER_ERROR, FAIL_VEHICLE_UPDATE);
	}

	@Transactional
	public ResponseDTO deleteReserved(Long id) {
		log.info(METHOD_NAME + "- deleteReserved");
		try {
			vehicleReservationRepository.deleteById(id);

			if (vehicleReservationRepository.findById(id).isPresent())
				return ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_DELETE);

			return ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_DELETE);
		} catch (Exception e) {
			log.error("SERVER ERROR" + e);
		}
		return ResponseDTO.fail(HttpStatus.INTERNAL_SERVER_ERROR, FAIL_VEHICLE_DELETE);
	}

	@Transactional
	public ResponseDTO deleteMark(Long id) {
		log.info(METHOD_NAME + "- deleteMark");
		try {
			vehicleBookmarkRepository.deleteById(id);

			if (vehicleBookmarkRepository.findById(id).isPresent())
				return ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_DELETE_MARK);

			return ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_DELETE_MARK);
		} catch (Exception e) {
			log.error("SERVER ERROR" + e);
		}
		return ResponseDTO.fail(HttpStatus.INTERNAL_SERVER_ERROR, FAIL_VEHICLE_DELETE_MARK);
	}
}
