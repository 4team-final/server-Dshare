package com.douzone.server.config.socket;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar, Long> {
//
//	@Transactional
//	Calendar findByUId(String uId);
}
