package com.example.sprinkle.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
public class SprinklePickup {

	//분배 인덱스
	@Id
	private Long distributeIdx;

	//사용자 아이디
	private Long userId;

	//등록 일시
	private LocalDateTime regDate;
}
