package com.example.sprinkle.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
public class SprinkleDistribute {

	//분배 인덱스
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "sprinkle_distribute")
	@TableGenerator(table = "hibernate_sequences", name = "sprinkle_distribute")
	private Long idx;

	//뿌리기 인덱스
	private Long sprinkleIdx;

	//분배 금액
	private Integer distributeAmount;
}
