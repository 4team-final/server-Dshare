package com.douzone.server.controller;

import com.douzone.server.config.utils.ResponseDTO;
import com.douzone.server.dto.vehicle.VehicleReservationDTO;
import com.douzone.server.entity.Vehicle;
import com.douzone.server.entity.VehicleReservation;
import com.douzone.server.service.VehicleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * Read:
 * findAllReserved - 차량 전체 예약 현황 조회 /list_all
 * findAllUnreserved - 차량 전체 미예약 현황 조회 /list_un
 * findTypeReserved - 차량 종류별 예약 현황 조회 /list_type
 * findDateReserved - 특정 일시 차량 예약 현황 조회 /list_date
 * createReservation - 차량 예약 등록 /create_reservation
 * findEmpBefore - 내 예약 조회 - 과거 /list_pre
 * findEmpAfter - 내 예약 조회 - 미래 /list_post
 * findWeekVehicle - 7일 동안 가장 많이 예약된 차량 /best_vehicle
 * findWeekDate - 7일 동안 가장 많이 예약한 시간대 /best_time
 * findRecentVehicle - 최근 예약된 차량 조회 /recent
 * findMarkVehicle - 내가 즐겨찾기한 차량 조회 /mark
 * findMarkBest - 즐겨찾기가 많은 차량 Top 3 조회 /best_mark
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
        List<Vehicle> result = vehicleService.findAllReserved();

        if (result == null) return ResponseDTO.fail(HttpStatus.BAD_REQUEST, "");

        return ResponseDTO.of(HttpStatus.OK, "", result);
    }

    @GetMapping(path = "/list_un")
    public ResponseDTO findAllUnreserved() {
        log.info(METHOD_NAME + "- findAllUnreserved");
        List<Vehicle> result = vehicleService.findAllUnreserved();

        if (result == null) return ResponseDTO.fail(HttpStatus.BAD_REQUEST, "");

        return ResponseDTO.of(HttpStatus.OK, "", result);
    }

    @PostMapping(path = "/list_type")
    public ResponseDTO findTypeReserved(@RequestBody String model) {
        log.info(METHOD_NAME + "- findTypeReserved");
        List<Vehicle> result = vehicleService.findTypeReserved(model);

        if (result == null) return ResponseDTO.fail(HttpStatus.BAD_REQUEST, "");

        return ResponseDTO.of(HttpStatus.OK, "", result);
    }

    @PostMapping(path = "/list_date")
    public ResponseDTO findDateReserved(@RequestBody Date date) {
        log.info(METHOD_NAME + "- findDateReserved");
        List<Vehicle> result = vehicleService.findDateReserved(date);

        if (result == null) return ResponseDTO.fail(HttpStatus.BAD_REQUEST, "");

        return ResponseDTO.of(HttpStatus.OK, "", result);
    }

    @PostMapping(path = "/list_pre")
    public ResponseDTO findEmpBefore(@RequestBody Long id) {
        log.info(METHOD_NAME + "- findEmpBefore");
        List<VehicleReservation> result = vehicleService.findEmpBefore(id, new Date());

        if (result == null) return ResponseDTO.fail(HttpStatus.BAD_REQUEST, "");

        return ResponseDTO.of(HttpStatus.OK, "", result);
    }

    @PostMapping(path = "/list_post")
    public ResponseDTO findEmpAfter(@RequestBody Long id) {
        log.info(METHOD_NAME + "- findEmpAfter");
        List<VehicleReservation> result = vehicleService.findEmpAfter(id, new Date());

        if (result == null) return ResponseDTO.fail(HttpStatus.BAD_REQUEST, "");

        return ResponseDTO.of(HttpStatus.OK, "", result);
    }

    @GetMapping(path = "/best_vehicle")
    public ResponseDTO findWeekVehicle() {
        log.info(METHOD_NAME + "- findWeekVehicle");
        Vehicle result = vehicleService.findWeekVehicle();

        if (result == null) return ResponseDTO.fail(HttpStatus.BAD_REQUEST, "");

        return ResponseDTO.of(HttpStatus.OK, "", result);
    }

    @GetMapping(path = "/best_time")
    public ResponseDTO findWeekDate() {
        log.info(METHOD_NAME + "- findWeekDate");
        Integer result = vehicleService.findWeekDate();

        if (result == null) return ResponseDTO.fail(HttpStatus.BAD_REQUEST, "");

        return ResponseDTO.of(HttpStatus.OK, "", result);
    }

    @GetMapping(path = "/recent")
    public ResponseDTO findRecentVehicle() {
        log.info(METHOD_NAME + "- findRecentVehicle");
        Vehicle result = vehicleService.findRecentVehicle();

        if (result == null) return ResponseDTO.fail(HttpStatus.BAD_REQUEST, "");

        return ResponseDTO.of(HttpStatus.OK, "", result);
    }

    @GetMapping(path = "/mark")
    public ResponseDTO findMarkVehicle(@RequestBody String empNo) {
        log.info(METHOD_NAME + "- findMarkVehicle");
        List<Vehicle> result = vehicleService.findMarkVehicle(empNo);

        if (result == null) return ResponseDTO.fail(HttpStatus.BAD_REQUEST, "");

        return ResponseDTO.of(HttpStatus.OK, "", result);
    }

    @GetMapping(path = "/best_mark")
    public ResponseDTO findMarkBest() {
        log.info(METHOD_NAME + "- findMarkBest");
        List<Vehicle> result = vehicleService.findMarkBest();

        if (result == null) return ResponseDTO.fail(HttpStatus.BAD_REQUEST, "");

        return ResponseDTO.of(HttpStatus.OK, "", result);
    }

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
}
