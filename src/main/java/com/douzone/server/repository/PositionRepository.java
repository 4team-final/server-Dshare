package com.douzone.server.repository;


import com.douzone.server.entity.Position;
import com.douzone.server.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {

	@Query("select max(p.id) from Position p")
	long findLastPosition();

}
