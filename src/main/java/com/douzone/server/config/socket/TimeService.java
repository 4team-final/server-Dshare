package com.douzone.server.config.socket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TimeService {
	private final TimeRepository timeRepository;

	@Transactional
	public void updateTime(String uid, Integer[] time, String empNo, Integer roomId) {
		List<Time> timeList = timeRepository.findByCalendar_UidAndRoomId(uid, roomId);
		for (int i = 0; i < timeList.size(); i++) {
			if (time[i] == 0) continue;
			timeList.get(i).updateIsSeat(time[i], empNo, roomId);
		}
		log.info("updateTime - success");
	}

	@Transactional
	public List<TimeMessageResDTO> selectTime(String uid) {
		List<TimeMessageResDTO> timeMessageResDTOList = timeRepository.findByCalendar_Uid(uid)
				.stream().map(time -> {
					return TimeMessageResDTO.builder().build().of(time);
				}).collect(Collectors.toList());
		log.info("selectTime - success");
		return timeMessageResDTOList;
	}
}
