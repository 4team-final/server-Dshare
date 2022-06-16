package com.douzone.server.dto.position;

import com.douzone.server.entity.Position;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PositionResDTO {
	private long positionId;
	private String positionName;

	@Builder
	public PositionResDTO(long positionId, String positionName) {
		this.positionId = positionId;
		this.positionName = positionName;
	}

	public PositionResDTO of(Position position) {
		return PositionResDTO.builder()
				.positionId(position.getId())
				.positionName(position.getName())
				.build();
	}
}
