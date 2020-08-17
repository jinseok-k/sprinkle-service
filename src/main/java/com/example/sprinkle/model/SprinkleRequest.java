package com.example.sprinkle.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class SprinkleRequest {

	//뿌릴 금액
	private int sprinkleAmount;

	//뿌릴 인원
	private int sprinkleCount;
}
