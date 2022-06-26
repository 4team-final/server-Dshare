package com.douzone.server.config.socket;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SocketResDTO {

	private List<?> results;

	private String message;

	@Builder
	public SocketResDTO(List<?> results, String message) {
		this.results = results;
		this.message = message;
	}
}
