package com.douzone.server.dto.bookmark;

import com.douzone.server.entity.Vehicle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkResDTO {
	private Vehicle vehicle;
	private int vcount;
	private List<String> imgList;

	public BookmarkResDTO of(IBookMarkResDTO i, List<String> list) {
		return BookmarkResDTO.builder()
				.vehicle(i.getVehicle())
				.vcount(i.getVcount())
				.imgList(list)
				.build();
	}
}
