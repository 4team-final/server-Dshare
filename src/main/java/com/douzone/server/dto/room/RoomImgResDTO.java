package com.douzone.server.dto.room;

import com.douzone.server.entity.RoomImg;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RoomImgResDTO {
	private Long imgId;
	private RoomResDTO room;
	private String imgPath;
	private String imgType;
	private String imgSize;

	@Builder
	public RoomImgResDTO(Long imgId, RoomResDTO room, String imgPath, String imgType, String imgSize) {
		this.imgId = imgId;
		this.room = room;
		this.imgPath = imgPath;
		this.imgType = imgType;
		this.imgSize = imgSize;
	}

	public RoomImgResDTO of(RoomImg img) {
		return RoomImgResDTO.builder()
				.imgId(img.getId())
				.room(RoomResDTO.builder().build().of(img.getMeetingRoom()))
				.imgPath(img.getPath())
				.imgType(img.getType())
				.imgSize(img.getImgSize())
				.build();
	}
}
