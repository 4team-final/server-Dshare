package com.douzone.server.service.method;

import com.douzone.server.dto.room.RoomBookmarkResDTO;
import com.douzone.server.dto.room.RoomImgResDTO;
import com.douzone.server.dto.room.RoomObjectResDTO;
import com.douzone.server.dto.room.RoomResDTO;
import com.douzone.server.entity.MeetingRoom;
import com.douzone.server.repository.RoomImgRepository;
import com.douzone.server.repository.RoomObjectRepository;
import com.douzone.server.repository.RoomRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BookmarkMethod {
	public static List<RoomResDTO> getBookmarkRoomList(List<RoomBookmarkResDTO> roomBookmarkResDTOList, RoomRepository roomRepository, RoomImgRepository roomImgRepository, RoomObjectRepository roomObjectRepository) {
		int size = roomBookmarkResDTOList.size();
		List<RoomResDTO> meetingRoomList = new ArrayList<>();
		for(int i = 0 ; i < size ; i++){
			MeetingRoom meetingRoom = roomRepository.roomList(roomBookmarkResDTOList.get(i).getRoomId()).get(0);
			List<RoomImgResDTO> roomImgResList = roomImgRepository.findByMeetingRoom_Id(roomBookmarkResDTOList.get(i).getRoomId()).stream().map(roomImg -> {
				RoomImgResDTO roomImgResDTO = RoomImgResDTO.builder().build().of(roomImg);
				return roomImgResDTO;
			}).collect(Collectors.toList());
			List<RoomObjectResDTO> roomObjectResList = roomObjectRepository.findByMeetingRoom_Id(roomBookmarkResDTOList.get(i).getRoomId()).stream().map(roomObject -> {
				RoomObjectResDTO roomObjectResDTO = RoomObjectResDTO.builder().build().of(roomObject);
				return roomObjectResDTO;
			}).collect(Collectors.toList());;

			meetingRoomList.add(
					RoomResDTO.builder()
							.categoryName(meetingRoom.getCategoryName())
							.capacity(meetingRoom.getCapacity())
							.roomNo(meetingRoom.getRoomNo())
							.modifiedAt(meetingRoom.getModifiedAt())
							.roomId(meetingRoom.getId())
							.content(meetingRoom.getContent())
							.roomImgResDTOList(roomImgResList)
							.roomObjectResDTOList(roomObjectResList)
							.build());
		}
		return meetingRoomList;
	}
}
