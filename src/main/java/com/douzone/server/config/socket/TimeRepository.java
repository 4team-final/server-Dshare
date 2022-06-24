package com.douzone.server.config.socket;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimeRepository extends JpaRepository<Time, Long> {

	List<Time> findByCalendar_Uid(String uid);

}
