package com.douzone.server.repository;

import com.douzone.server.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee findByEmpNo(String empNo);

    boolean existsByEmpNo(String empNo);

    Optional<Employee> findTop1ByOrderByIdDesc();

    @Query("update Employee e set e.team.id = :teamId, e.position.id = :positionId where e.id= :id")
    @Modifying(clearAutomatically = true)
    int updateTP(@Param("teamId")long teamId,@Param("positionId")long positionId, @Param("id")long id);

    @Query("update Employee e set e.team.id = :teamId where e.id= :id")
    @Modifying(clearAutomatically = true)
    int updateT(@Param("teamId")long teamId, @Param("id")long id);

    @Query("update Employee e set e.name = :name where e.id= :id")
    @Modifying(clearAutomatically = true)
    int updateName(@Param("name")String name, @Param("id")long id);

}
