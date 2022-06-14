package com.douzone.server.repository;

import com.douzone.server.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface TokenRepository extends JpaRepository<Token, Long> {
	Token findByEmpNo(String token);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("UPDATE Token t SET t.refreshToken = :token WHERE t.empNo= :empNo")
	Integer updateToken(@Param("token") String token, @Param("empNo") String empNo);
}
