package com.example.sprinkle.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class SprinkleDetail {

	//뿌린 시간
	private LocalDateTime sprinkleTime;

	//뿌린 금액
	private int sprinkleAmount;

	//받기 완료된 금액
	private int pickupAmount;

	//받기 완료된 정보
	private List<PickupDetail> pickupDetails;
}
