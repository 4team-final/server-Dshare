package com.douzone.server.repository;

import com.douzone.server.entity.Vehicle;
import com.douzone.server.entity.VehicleReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

	@Query("select v from VehicleReservation vr left join Vehicle v on vr.vehicle.id = v.id")
	List<Vehicle> findAllReserved();

	@Query("select v from Vehicle v left outer join VehicleReservation vr on vr.vehicle.id = v.id where vr.id is null")
	List<Vehicle> findAllUnreserved();

	@Query("select v from VehicleReservation vr left join Vehicle v on v.id = vr.vehicle.id where v.model = :model")
	List<Vehicle> findTypeReserved(@Param("model") String model);

	@Query("select v from VehicleReservation vr left join Vehicle v on v.id = vr.vehicle.id where vr.startedAt < :date and vr.endedAt > :date")
	List<Vehicle> findDateReserved(@Param("date") Date date);

	@Query("select vr from VehicleReservation vr left join Employee e on vr.employee.id = e.id where e.id = :id and vr.endedAt < :date")
	List<VehicleReservation> findEmpBefore(@Param("id") Long id, @Param("date") Date date);

	@Query("select vr from VehicleReservation vr left join Employee e on vr.employee.id = e.id where e.id = :id and vr.startedAt > :date")
	List<VehicleReservation> findEmpAfter(@Param("id") Long id, @Param("date") Date date);

	@Query(nativeQuery = true, value = "select count(v) as vc from VehicleReservation vr left join Vehicle v on vr.vehicle.id = v.id group by v.id order by vc desc limit 1")
	Vehicle findWeekVehicle();

	@Query(nativeQuery = true, value = "select count(hour(vr.startedAt)) as h from VehicleReservation vr order by h desc limit 1")
	Integer findWeekDate();

	@Query(nativeQuery = true, value = "select v from Vehicle v left join VehicleReservation vr on v.id = vr.vehicle.id order by vr.startedAt desc limit 1")
	Vehicle findRecentVehicle();
}
