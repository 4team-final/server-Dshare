package com.douzone.server.config.utils;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UploadDTO {

	private Object object;
	private String path;
	private String type;
	private String imgSize;

	@Builder
	public UploadDTO(Object object, String path, String type, String imgSize) {
		this.object = object;
		this.path = path;
		this.type = type;
		this.imgSize = imgSize;

	}

}
