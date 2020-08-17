package com.example.sprinkle.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class PickupDetail {

	//받기 금액
	private int pickupAmount;

	//받은 사용자 아이디
	private long userId;
}
