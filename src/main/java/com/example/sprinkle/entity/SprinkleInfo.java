package com.example.sprinkle.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
public class SprinkleInfo {

	//뿌리기 인덱스
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "sprinkle_info")
	@TableGenerator(table = "hibernate_sequences", name = "sprinkle_info")
	private Long idx;

	//사용자 아이디
	private Long userId;

	//대화방 아이디
	private String roomId;

	//뿌릴 금액
	private Integer sprinkleAmount;

	//뿌릴 인원
	private Integer sprinkleCount;

	//등록 일시
	private LocalDateTime regDate;
}
