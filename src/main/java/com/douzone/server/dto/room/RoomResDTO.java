package com.douzone.server.dto.room;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class RoomResDTO {
	private int id;
	private String content;
	private String categoryName;
	private int roomNo;
	private int capacity;
	private LocalDateTime modifiedAt;
	
}
