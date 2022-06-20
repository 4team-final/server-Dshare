package com.douzone.server.entity;

import com.douzone.server.config.utils.BaseAtTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "meeting_room")
@Getter
@NoArgsConstructor
public class MeetingRoom extends BaseAtTime {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String content;
	private String categoryName;
	private Integer roomNo;
	private Integer capacity;

	@OneToMany(mappedBy = "meetingRoom", cascade = CascadeType.ALL)
	List<RoomObject> roomObjectList = new ArrayList<>();

	@OneToMany(mappedBy = "meetingRoom", cascade = CascadeType.ALL)
	List<RoomImg> roomImgList = new ArrayList<>();

	public void updateRoom(String content, String categoryName, Integer roomNo, Integer capacity) {
		this.content = content;
		this.categoryName = categoryName;
		this.roomNo = roomNo;
		this.capacity = capacity;
	}

	@Builder
	public MeetingRoom(Long id, String content, String categoryName, Integer roomNo, Integer capacity) {
		this.id = id;
		this.content = content;
		this.categoryName = categoryName;
		this.roomNo = roomNo;
		this.capacity = capacity;
	}
}
