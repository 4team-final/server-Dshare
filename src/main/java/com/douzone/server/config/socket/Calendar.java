package com.douzone.server.config.socket;

import com.douzone.server.config.utils.BaseAtTime;
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
@Table(name = "calendar")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Calendar extends BaseAtTime {
	@Id
	@Column(name = "uId", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String uId;

	private String year;
	private String month;
	private String day;
	private String name;


}
