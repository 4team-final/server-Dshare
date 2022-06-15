package com.douzone.server.service;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.douzone.server.config.s3.AwsS3;
import com.douzone.server.config.security.handler.DecodeEncodeHandler;
import com.douzone.server.dto.employee.SignupReqDTO;
import com.douzone.server.dto.vehicle.VehicleReservationDTO;
import com.douzone.server.entity.Employee;
import com.douzone.server.entity.Vehicle;
import com.douzone.server.entity.VehicleReservation;
import com.douzone.server.exception.EmpAlreadyExistException;
import com.douzone.server.exception.EmpNotFoundException;
import com.douzone.server.exception.ErrorCode;
import com.douzone.server.exception.ImgFileNotFoundException;
import com.douzone.server.repository.EmployeeRepository;
import com.douzone.server.repository.VehicleReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {
    private final EmployeeRepository employeeRepository;
    private final VehicleReservationRepository vehicleReservationRepository;
    private final DecodeEncodeHandler decodeEncodeHandler;
    private final AwsS3 awsS3;

    @Value(value = "${year.current}")
    private String year;

    @Value(value = "${aws-client.path}")
    private String awsPath;

    @Transactional
    public Long register(SignupReqDTO signupReqDTO) {

        Employee employee = employeeRepository.findTop1ByOrderByIdDesc().orElseThrow(() -> new EmpNotFoundException(ErrorCode.EMP_NOT_FOUND));

        //년도 + 부서 + 사번
        StringBuilder sb = new StringBuilder();
        String deptId = String.format("%02d", signupReqDTO.getDeptId());
        String empId = String.format("%05d", employee.getId() + 1);
        sb.append(year).append(deptId).append(empId);

        String empNo = sb.toString();
        boolean exists = employeeRepository.existsByEmpNo(empNo);
        if (exists) {
            throw new EmpAlreadyExistException(ErrorCode.EMP_ALREADY_EXIST);
        }
        String password = decodeEncodeHandler.passwordEncode(signupReqDTO.getPassword());
        long id = employeeRepository.save(signupReqDTO.of(empNo, password)).getId();

        return id;
    }

    @Transactional
    public Long uploadProfileImg(List<MultipartFile> files, long id) {

        if (files.isEmpty() || files == null) {
            throw new ImgFileNotFoundException(ErrorCode.IMG_NOT_FOUND);
        }
        String basePath = "profile/";

        ArrayList<String> fileName = new ArrayList<String>();
        ArrayList<String> fileType = new ArrayList<String>();
        ArrayList<Long> fileLength = new ArrayList<Long>();
        try {
            for (int i = 0; i < files.size(); i++) {
                fileName.add(LocalDateTime.now().toString() + "_" + files.get(i).getOriginalFilename());
                fileType.add(files.get(i).getContentType());
                fileLength.add(files.get(i).getSize());
            }
            // 업로드 될 버킷 객체 url
            String[] uploadUrl = new String[files.size()];

            for (int i = 0; i < files.size(); i++) {
                try {
                    uploadUrl[i] = awsS3.upload(files.get(i), basePath + fileName.get(i), fileType.get(i), fileLength.get(i));
                } catch (AmazonS3Exception e) {
                    log.error("AmazonS3Exception : AdminService - uploadProfileImg " + e.getMessage());
                    e.printStackTrace();
                } catch (IOException e) {
                    log.error("IOException : AdminService - uploadProfileImg " + e.getMessage());
                    e.printStackTrace();
                }
            }

            //데이터 베이스에 들어갈 url
            String profileImg = awsPath + uploadUrl[0];
            Employee employee = employeeRepository.findById(id).orElseThrow(() -> new EmpNotFoundException(ErrorCode.EMP_NOT_FOUND));

            employee.updateProfileImg(profileImg);
            return employee.getId();

        } catch (IllegalStateException e) {
            log.error("IllegalStateException : AdminService - uploadProfileImg " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            log.error("Exception : AdminService - uploadProfileImg " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public Integer createVehicleReservation(VehicleReservationDTO vehicleReservationDTO) {
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
}
