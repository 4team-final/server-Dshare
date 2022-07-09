package com.douzone.server.config.socket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
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
			timeList.get(i).updateIsSeat(time[i], empNo);
		}
		log.info("updateTime - success");
	}

	@Transactional
	public List<TimeMessageResDTO> selectTime(String uid, Integer roomId, TimeMessageReqDTO.MessageType messageType) {

		List<TimeMessageResDTO> timeMessageResDTOList = timeRepository.findByCalendar_UidAndRoomId(uid, roomId)
				.stream().map(time -> {
					return TimeMessageResDTO.builder().build().of(time);
				}).collect(Collectors.toList());
		log.info("selectTime - success");
		return timeMessageResDTOList;
	}

	@Transactional
	public void resetIsSeat(Integer rid, String uid, String empNo, String startTime, String endTime) {
		Optional.ofNullable(uid)
				.map(v -> timeRepository.selectByUidAndVid(uid, rid))
				.filter(Optional::isPresent)
				.map(res -> {
					int s = 0, e = 0;
					for (int i = res.get().size() - 1; i >= 0; i--) {
						if (res.get().get(i).getTime().equals(endTime)) {
							e = i;
							break;
						}
					}
					for (int i = 0; i < res.get().size(); i++) {
						if (res.get().get(i).getTime().equals(startTime)) {
							s = i;
							break;
						}
					}
					for (int i = s; i < e; i++) {
						if (res.get().get(i).getEmpNo().equals(empNo)) {
							res.get().get(i).updateIsSeat(0, "");
						}
					}
					return res.get();
				})
				.orElseThrow(RuntimeException::new);
	}

	@Transactional
	public void resetIsSeat(Integer rid, String uid, String empNo, String time, int k) {
		Optional.ofNullable(uid)
				.map(v -> timeRepository.selectByUidAndVid(uid, rid))
				.filter(Optional::isPresent)
				.map(res -> {
					if (k == 0) {
						int j = 0;
						for (int i = 0; i < res.get().size(); i++) {
							if (res.get().get(i).getTime().equals(time)) {
								j = i;
								break;
							}
						}
						for (int i = j; i < res.get().size(); i++) {
							if (res.get().get(i).getEmpNo().equals(empNo)) {
								res.get().get(i).updateIsSeat(0, "");
							}
						}
					} else if (k == 1) {
						int j = 0;
						for (int i = res.get().size() - 1; i >= 0; i--) {
							if (res.get().get(i).getTime().equals(time)) {
								j = i;
								break;
							}
						}
						for (int i = j; i >= 0; i--) {
							if (res.get().get(i).getEmpNo().equals(empNo)) {
								res.get().get(i).updateIsSeat(0, "");
							}
						}
					}
					return res.get();
				})
				.orElseThrow(RuntimeException::new);
	}
}
