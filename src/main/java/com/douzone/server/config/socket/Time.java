package com.douzone.server.config.socket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "time")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Time {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "uId")
	private Calendar calendar;

	private String time;

	private Integer isSeat;

	private String empNo;

	public void updateIsSeat(Integer isSeat, String empNo) {
		this.isSeat = isSeat;
		this.empNo = empNo;
	}

}
