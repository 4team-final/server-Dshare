package com.douzone.server.repository;

import com.douzone.server.dto.vehicle.*;
import com.douzone.server.dto.vehicle.impl.VehicleWeekTimeDTO;
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
			"v as vehicle, vi.path as vehicleImg, e.empNo as empNo, e.name as name " +
			"from VehicleReservation vr " +
			"left join fetch Vehicle v on vr.vehicle.id = v.id " +
			"left join fetch Employee e on vr.employee.id = e.id " +
			"left join fetch VehicleImg vi on vi.vehicle.id = v.id " +
			"where startedAt > current_time " +
			"order by id desc")
	List<IVehicleListResDTO> findByAllReservation();

	@Query("select vr.id as id, vr.startedAt as startedAt, vr.endedAt as endedAt, " +
			"vr.createdAt as createdAt, vr.modifiedAt as modifiedAt, " +
			"vr.reason as reason, vr.title as title, " +
			"v as vehicle, vi.path as vehicleImg, e.empNo as empNo, e.name as name " +
			"from VehicleReservation vr " +
			"left join fetch Vehicle v on vr.vehicle.id = v.id " +
			"left join fetch Employee e on vr.employee.id = e.id " +
			"left join fetch VehicleImg vi on vi.vehicle.id = v.id " +
			"where startedAt > current_time " +
			"order by id desc")
	List<IVehicleListResDTO> findByPaginationReservation(Pageable pageable);

	@Query("select distinct v.id as id, v.name as name, v.number as number, " +
			"v.model as model, v.color as color, v.capacity as capacity, vi.path as vehicleImg from Vehicle v " +
			"left outer join VehicleReservation vr on vr.vehicle.id = v.id " +
			"left join fetch VehicleImg vi on v.id = vi.vehicle.id " +
			"where vr.endedAt < current_time or vr.id is null and vr.startedAt > :date")
	List<IVehicleResDTO> findByAllNotReservation(@Param("date") LocalDateTime date);

	@Query("select vr.id as id, vr.startedAt as startedAt, vr.endedAt as endedAt, " +
			"vr.createdAt as createdAt, vr.modifiedAt as modifiedAt, " +
			"vr.reason as reason, vr.title as title, " +
			"v as vehicle, vi.path as vehicleImg, e.empNo as empNo, e.name as name " +
			"from VehicleReservation vr " +
			"left join fetch Vehicle v on vr.vehicle.id = v.id " +
			"left join fetch Employee e on vr.employee.id = e.id " +
			"left join fetch VehicleImg vi on vi.vehicle.id = v.id " +
			"where v.model = :model " +
			"order by id desc ")
	List<IVehicleListResDTO> findByModelReservation(@Param("model") String model);

	@Query("select vr.id as id, vr.startedAt as startedAt, vr.endedAt as endedAt, " +
			"vr.createdAt as createdAt, vr.modifiedAt as modifiedAt, " +
			"vr.reason as reason, vr.title as title, " +
			"v as vehicle, vi.path as vehicleImg, e.empNo as empNo, e.name as name " +
			"from VehicleReservation vr " +
			"left join fetch Vehicle v on vr.vehicle.id = v.id " +
			"left join fetch Employee e on vr.employee.id = e.id " +
			"left join fetch VehicleImg vi on vi.vehicle.id = v.id " +
			"where startedAt between :startDate and :endDate " +
			"order by id desc ")
	List<IVehicleListResDTO> findByDateTimeReservation(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

	@Query("select vr.id as id, vr.startedAt as startedAt, vr.endedAt as endedAt, " +
			"vr.createdAt as createdAt, vr.modifiedAt as modifiedAt, " +
			"vr.reason as reason, vr.title as title, v as vehicle, vi.path as vehicleImg " +
			"from VehicleReservation vr " +
			"left join fetch Vehicle v on vr.vehicle.id = v.id " +
			"left join fetch Employee e on vr.employee.id = e.id " +
			"left join fetch VehicleImg vi on vi.vehicle.id = v.id " +
			"where e.id = :id and vr.endedAt < current_time  ")
	List<IVehicleEmpResDTO> findByMyPastReservation(@Param("id") Long id);

	@Query("select vr.id as id, vr.startedAt as startedAt, vr.endedAt as endedAt, " +
			"vr.createdAt as createdAt, vr.modifiedAt as modifiedAt, " +
			"vr.reason as reason, vr.title as title, v as vehicle, vi.path as vehicleImg " +
			"from VehicleReservation vr " +
			"left join fetch Vehicle v on vr.vehicle.id = v.id " +
			"left join fetch Employee e on vr.employee.id = e.id " +
			"left join fetch VehicleImg vi on vi.vehicle.id = v.id " +
			"where e.id = :id and vr.startedAt > current_time  ")
	List<IVehicleEmpResDTO> findByMyCurrentReservation(@Param("id") Long id);

	@Query("select vr.id as id, vr.startedAt as startedAt, vr.endedAt as endedAt, " +
			"vr.createdAt as createdAt, vr.modifiedAt as modifiedAt, vr.reason as reason, vr.title as title, " +
			"vr.vehicle as vehicle, count(vr.vehicle.id) as vcount, vi.path as vehicleImg " +
			"from VehicleReservation vr " +
			"left join fetch VehicleImg vi on vr.vehicle.id = vi.vehicle.id " +
			"where vr.startedAt > :date  group by vr.vehicle.id order by vcount desc")
	List<IVehicleWeekDTO> weekMostReservedVehicle(@Param("date") LocalDateTime date);

	@Query("select substring(vr.startedAt, 12, 2) as substring, vr.id as id, " +
			"vr.startedAt as startedAt, vr.endedAt as endedAt, vr.reason as reason, vr.title as title, " +
			"vr.createdAt as createdAt, vr.modifiedAt as modifiedAt, v as vehicle, vi.path as vehicleImg, " +
			"e.empNo as empNo, e.name as name " +
			"from VehicleReservation vr " +
			"left join fetch Vehicle v on vr.vehicle.id = v.id " +
			"left join fetch Employee e on vr.employee.id = e.id " +
			"left join fetch VehicleImg vi on v.id = vi.id " +
			"where startedAt > :date and endedAt < :end")
	List<VehicleWeekTimeDTO> weekMostReservedTime(@Param("date") LocalDateTime date, @Param("end") LocalDateTime end);

	@Query("select distinct vr.endedAt as endedAt, v as vehicle, vi.path as vehicleImg " +
			"from Vehicle v " +
			"left join fetch VehicleReservation vr on v.id = vr.vehicle.id " +
			"left join fetch VehicleImg vi on v.id = vi.vehicle.id " +
			"where endedAt is not null order by vr.modifiedAt desc")
	List<IVehicleDateResDTO> findByRecentReservedVehicle();


	@Query("select vr.id as id, vr.startedAt as startedAt, vr.endedAt as endedAt, " +
			"vr.createdAt as createdAt, vr.modifiedAt as modifiedAt, " +
			"vr.reason as reason, vr.title as title, " +
			"v as vehicle, vi.path as vehicleImg, e.empNo as empNo, e.name as name " +
			"from VehicleReservation vr " +
			"left join fetch Vehicle v on vr.vehicle.id = v.id " +
			"left join fetch Employee e on vr.employee.id = e.id " +
			"left join fetch VehicleImg vi on vi.vehicle.id = v.id " +
			"where vr.id = :id")
	Optional<IVehicleListResDTO> selectByVehicleReservation(@Param("id") Long id);

	@Query("select vr.id as id, vr.title as title, vr.startedAt as timeTime " +
			"from VehicleReservation vr " +
			"left join fetch Vehicle v on vr.vehicle.id = v.id " +
			"left join fetch Employee e on vr.employee.id = e.id " +
			"where e.id = :empId and vr.startedAt > current_time")
	List<IVehicleTimeResDTO> soonReservationMyTime(@Param("empId") Long empId, Pageable pageable);

	@Query("select vr.id as id, vr.title as title, vr.endedAt as timeTime " +
			"from VehicleReservation vr " +
			"left join fetch Vehicle v on vr.vehicle.id = v.id " +
			"left join fetch Employee e on vr.employee.id = e.id " +
			"where e.id = :empId and vr.startedAt < current_time and current_time < vr.endedAt")
	List<IVehicleTimeResDTO> ingReservationMyTime(@Param("empId") Long empId, Pageable pageable);

	@Query(value = "select vr.id as id, vr.reason as reason, vr.title as title, vr.startedAt as startedAt, vr.endedAt as endedAt, " +
			"vr.createdAt as createdAt, vr.modifiedAt as modifiedAt, " +
			"v.name as vName, v.number as vNumber, v.model as model, v.color as color, v.capacity as capacity, vi.path as vehicleImg, " +
			"e.empNo as empNo, e.name eName, e.email as email, e.tel as tel, e.birthday as birthday, e.profileImg as profileImg, " +
			"t.name as team, d.name as dept , p.name as position " +
			"from vehicle_reservation vr " +
			"left join vehicle v on vr.vehicleId = v.id " +
			"left join vehicle_img vi on v.id = vi.vehicleId " +
			"left join employee e on vr.empId = e.id " +
			"left join position p on p.id = e.positionId " +
			"left join team t on e.teamId = t.id " +
			"left join department d on t.deptId = d.id " +
			"where 1=1 and " +
			"(case when :#{#dto.vehicleId} is not null then vr.vehicleId = :#{#dto.vehicleId} " +
			"when :#{#dto.capacity} is not null then v.capacity = :#{#dto.capacity} " +
			"when :#{#dto.positionId} is not null then e.positionId = :#{#dto.positionId} " +
			"when :#{#dto.deptId} is not null then t.deptId = :#{#dto.deptId} " +
			"when :#{#dto.teamId} is not null then t.id = :#{#dto.teamId} " +
			"when :#{#dto.empNo} is not null then e.empNo = :#{#dto.empNo} " +
			"when :#{#dto.startedAt} is not null then vr.startedAt <= :#{#dto.startedAt} " +
			"when :#{#dto.endedAt} is not null then vr.startedAt >= :#{#dto.endedAt} END)" +
			"order by vr.modifiedAt desc", nativeQuery = true)
	List<IVehicleVariousDTO> selectByVariousColumns(@Param("dto") VehicleSearchDTO vehicleSearchDTO);
}
