package com.douzone.server.service.method;

import com.douzone.server.dto.reservation.ReservationResDTO;
import com.douzone.server.dto.room.RoomBookmarkResDTO;
import com.douzone.server.dto.room.RoomImgResDTO;
import com.douzone.server.dto.room.RoomObjectResDTO;
import com.douzone.server.dto.room.RoomResDTO;
import com.douzone.server.entity.MeetingRoom;
import com.douzone.server.entity.RoomReservation;
import com.douzone.server.repository.RoomImgRepository;
import com.douzone.server.repository.RoomObjectRepository;
import com.douzone.server.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class RoomServiceMethod {

	private final RoomRepository roomRepository;
	private final RoomObjectRepository roomObjectRepository;
	private final RoomImgRepository roomImgRepository;

	@Transactional
	public List<RoomResDTO> RoomImgListAndRoomObjectList(List<RoomBookmarkResDTO> roomBookmarkResDTOList) {
		int size = roomBookmarkResDTOList.size();
		List<RoomResDTO> meetingRoomList = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			MeetingRoom meetingRoom = roomRepository.roomList(roomBookmarkResDTOList.get(i).getRoomId()).get(0);

			List<RoomImgResDTO> roomImgResList = roomImgRepository.findByMeetingRoom_Id(roomBookmarkResDTOList.get(i).getRoomId()).stream().map(roomImg -> {
				RoomImgResDTO roomImgResDTO = RoomImgResDTO.builder().build().of(roomImg);
				return roomImgResDTO;
			}).collect(Collectors.toList());

			List<RoomObjectResDTO> roomObjectResList = roomObjectRepository.findByMeetingRoom_Id(roomBookmarkResDTOList.get(i).getRoomId()).stream().map(roomObject -> {
				RoomObjectResDTO roomObjectResDTO = RoomObjectResDTO.builder().build().of(roomObject);
				return roomObjectResDTO;
			}).collect(Collectors.toList());
			;

			meetingRoomList.add(new RoomResDTO().of(meetingRoom, roomObjectResList, roomImgResList,roomBookmarkResDTOList.get(i).getCount()));
		}
		return meetingRoomList;
	}

	public List<List<?>> RoomImgListAndRoomObjectList(RoomReservation roomReservation) {
		Long roomId = roomReservation.getMeetingRoom().getId();
		return roomObjectMapping(roomId);
	}

	public List<List<?>> RoomImgListAndRoomObjectList(ReservationResDTO reservationResDTO) {
		Long roomId = reservationResDTO.getRoom().getRoomId();
		return roomObjectMapping(roomId);
	}

	@Transactional
	public List<List<?>> roomObjectMapping(Long roomId) {
		List<List<?>> result = new ArrayList<>();

		//회의실 물건들
		List<RoomObjectResDTO> roomObjectResDTOList = roomObjectRepository.findByMeetingRoom_Id(roomId).stream().map(
				roomObject -> {
					RoomObjectResDTO roomObjectResDTO = RoomObjectResDTO.builder().build().of(roomObject);
					return roomObjectResDTO;
				}).collect(Collectors.toList());
		//회의실 이미지들
		List<RoomImgResDTO> roomImgResDTOList = roomImgRepository.findByMeetingRoom_Id(roomId).stream().map(
				roomImg -> {
					RoomImgResDTO roomImgResDTO = RoomImgResDTO.builder().build().of(roomImg);
					return roomImgResDTO;
				}).collect(Collectors.toList());

		result.add(roomObjectResDTOList);
		result.add(roomImgResDTOList);
		return result;
	}


}
