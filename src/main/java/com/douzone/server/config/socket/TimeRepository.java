package com.douzone.server.config.socket;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TimeRepository extends JpaRepository<Time, Long> {

	List<Time> findByCalendar_Uid(String uid);

	List<Time> findByCalendar_UidAndRoomId(String uid, Integer roomId);

	@Query("select tv from Time tv where tv.calendar.uid = :uid and tv.roomId = :rid")
	Optional<List<Time>> selectByUidAndVid(@Param("uid") String uid, @Param("rid") Integer rid);
}
