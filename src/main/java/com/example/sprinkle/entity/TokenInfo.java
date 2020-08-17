package com.example.sprinkle.entity;

import com.example.sprinkle.entity.pk.TokenInfoPk;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@IdClass(TokenInfoPk.class)
@Entity
public class TokenInfo {

	//대화방 아이디
	@Id
	private String roomId;

	//토큰
	@Id
	private String token;

	//뿌리기 인덱스
	private Long sprinkleIdx;
}
