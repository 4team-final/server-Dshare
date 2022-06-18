package com.douzone.server.repository;

import com.douzone.server.dto.vehicle.IVehicleDateResDTO;
import com.douzone.server.dto.vehicle.IVehicleEmpResDTO;
import com.douzone.server.dto.vehicle.IVehicleListResDTO;
import com.douzone.server.dto.vehicle.IVehicleTimeResDTO;
import com.douzone.server.entity.Vehicle;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

	@Query("select vr.id as id, vr.startedAt as startedAt, vr.endedAt as endedAt, " +
			"vr.createdAt as createdAt, vr.modifiedAt as modifiedAt, " +
			"vr.reason as reason, vr.title as title, " +
			"v as vehicle, e.empNo as empNo, e.name as name " +
			"from VehicleReservation vr " +
			"join fetch Vehicle v on vr.vehicle.id = v.id " +
			"join fetch Employee e on vr.employee.id = e.id " +
			"where startedAt > current_time " +
			"order by id desc")
	List<IVehicleListResDTO> findAllReserved();

	@Query("select vr.id as id, vr.startedAt as startedAt, vr.endedAt as endedAt, " +
			"vr.createdAt as createdAt, vr.modifiedAt as modifiedAt, " +
			"vr.reason as reason, vr.title as title, " +
			"v as vehicle, e.empNo as empNo, e.name as name " +
			"from VehicleReservation vr " +
			"join fetch Vehicle v on vr.vehicle.id = v.id " +
			"join fetch Employee e on vr.employee.id = e.id " +
			"where startedAt > current_time " +
			"order by id desc")
	List<IVehicleListResDTO> findAllReservedPaging(Pageable pageable);

	@Query("select distinct v from Vehicle v " +
			"left outer join VehicleReservation vr on vr.vehicle.id = v.id " +
			"where vr.endedAt < current_time or vr.id is null and vr.startedAt > :date")
	List<Vehicle> findAllUnreserved(@Param("date") LocalDateTime date);

	@Query("select vr.id as id, vr.startedAt as startedAt, vr.endedAt as endedAt, " +
			"vr.createdAt as createdAt, vr.modifiedAt as modifiedAt, " +
			"vr.reason as reason, vr.title as title, " +
			"v as vehicle, e.empNo as empNo, e.name as name " +
			"from VehicleReservation vr " +
			"join fetch Vehicle v on vr.vehicle.id = v.id " +
			"join fetch Employee e on vr.employee.id = e.id " +
			"where v.model = :model " +
			"order by id desc ")
	List<IVehicleListResDTO> findTypeReserved(@Param("model") String model);

	@Query("select vr.id as id, vr.startedAt as startedAt, vr.endedAt as endedAt, " +
			"vr.createdAt as createdAt, vr.modifiedAt as modifiedAt, " +
			"vr.reason as reason, vr.title as title, " +
			"v as vehicle, e.empNo as empNo, e.name as name " +
			"from VehicleReservation vr " +
			"join fetch Vehicle v on vr.vehicle.id = v.id " +
			"join fetch Employee e on vr.employee.id = e.id " +
			"where startedAt between :startDate and :endDate " +
			"order by id desc ")
	List<IVehicleListResDTO> findDateReserved(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

	@Query("select vr.id as id, vr.startedAt as startedAt, vr.endedAt as endedAt, " +
			"vr.createdAt as createdAt, vr.modifiedAt as modifiedAt, " +
			"vr.reason as reason, vr.title as title, v as vehicle " +
			"from VehicleReservation vr " +
			"join fetch Vehicle v on vr.vehicle.id = v.id " +
			"join fetch Employee e on vr.employee.id = e.id " +
			"where e.id = :id and vr.endedAt < current_time  ")
	List<IVehicleEmpResDTO> findEmpBefore(@Param("id") Long id);

	@Query("select vr.id as id, vr.startedAt as startedAt, vr.endedAt as endedAt, " +
			"vr.createdAt as createdAt, vr.modifiedAt as modifiedAt, " +
			"vr.reason as reason, vr.title as title, v as vehicle " +
			"from VehicleReservation vr " +
			"join fetch Vehicle v on vr.vehicle.id = v.id " +
			"join fetch Employee e on vr.employee.id = e.id " +
			"where e.id = :id and vr.startedAt > current_time  ")
	List<IVehicleEmpResDTO> findEmpAfter(@Param("id") Long id);

	@Query("select vr.id as id, vr.startedAt as startedAt, vr.endedAt as endedAt, " +
			"vr.createdAt as createdAt, vr.modifiedAt as modifiedAt, vr.reason as reason, vr.title as title, " +
			"vr.vehicle as vehicle, count(vr.vehicle.id) as vcount " +
			"from VehicleReservation vr " +
			"where vr.startedAt > :date  group by vr.vehicle.id order by vcount desc")
	List<IVehicleTimeResDTO> findWeekVehicle(@Param("date") LocalDateTime date);

	@Query("select substring(vr.startedAt, 12, 2) from VehicleReservation vr where vr.startedAt > :date")
	List<String> findWeekDate(@Param("date") LocalDateTime date);

	@Query("select distinct vr.endedAt as endedAt, v as vehicle from Vehicle v " +
			"left join VehicleReservation vr on v.id = vr.vehicle.id " +
			"where endedAt is not null order by vr.modifiedAt desc")
	List<IVehicleDateResDTO> findRecentVehicle();


	@Query("select vr.id as id, vr.startedAt as startedAt, vr.endedAt as endedAt, " +
			"vr.createdAt as createdAt, vr.modifiedAt as modifiedAt, " +
			"vr.reason as reason, vr.title as title, " +
			"v as vehicle, e.empNo as empNo, e.name as name " +
			"from VehicleReservation vr " +
			"join fetch Vehicle v on vr.vehicle.id = v.id " +
			"join fetch Employee e on vr.employee.id = e.id " +
			"where vr.id = :id")
	Optional<IVehicleListResDTO> findCustom(@Param("id") Long id);

	@Query("select vr.id as id, vr.startedAt as dateTime " +
			"from VehicleReservation vr " +
			"join fetch Vehicle v on vr.vehicle.id = v.id " +
			"join fetch Employee e on vr.employee.id = e.id " +
			"where e.id = :empId and dateTime > current_time")
	List<IVehicleTimeResDTO> soonReservationMyTime(@Param("empId") Long empId, Pageable pageable);

	@Query("select vr.id as id, vr.endedAt as dateTime " +
			"from VehicleReservation vr " +
			"join fetch Vehicle v on vr.vehicle.id = v.id " +
			"join fetch Employee e on vr.employee.id = e.id " +
			"where e.id = :empId and vr.startedAt < current_time and current_time < dateTime")
	List<IVehicleTimeResDTO> ingReservationMyTime(@Param("empId") Long empId, Pageable pageable);
}
