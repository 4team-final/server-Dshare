package com.douzone.server.config.socket;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CalendarRoom {

	private String uId;
	private String name;
	private String year;
	private String month;
	private String day;

	private List<TimeMessage> timeMessageList = new ArrayList<>();

	@Builder
	public CalendarRoom(String uId, String name, String year, String month, String day) {
		this.uId = uId;
		this.name = name;
		this.year = year;
		this.month = month;
		this.day = day;
	}

	public CalendarRoom of(Calendar calendar) {
		return CalendarRoom.builder()
				.uId(calendar.getUId())
				.year(calendar.getYear())
				.month(calendar.getMonth())
				.day(calendar.getDay())
				.name(calendar.getName())
				.build();
	}

	public Calendar of() {
		return Calendar.builder()
				.uId(uId)
				.year(year)
				.month(month)
				.day(day)
				.name(name)
				.build();
	}
}
