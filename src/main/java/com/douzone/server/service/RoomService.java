package com.douzone.server.service;

import com.douzone.server.dto.reservation.RecentResDTO;
import com.douzone.server.repository.RoomRepository;
import com.douzone.server.repository.querydsl.RoomReservationQueryDSL;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomService {

	private final RoomRepository roomRepository;
	private final RoomReservationQueryDSL reservationQueryDSL;

	@Transactional
	public List<RecentResDTO> recentReservation() {

		return null;
	}
}
