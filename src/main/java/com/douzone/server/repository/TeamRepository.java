package com.douzone.server.repository;


import com.douzone.server.entity.Employee;
import com.douzone.server.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

	@Query("select max(t.id) from Team t")
	long findLastTeam();

}
