package com.example.sprinkle.repository;

import com.example.sprinkle.entity.SprinkleDistribute;
import com.example.sprinkle.entity.SprinklePickup;
import com.example.sprinkle.model.PickupDetail;

import java.util.List;

public interface SprinklePickupRepositoryCustom {
	SprinklePickup getUserSprinklePickup(Long userId, Long sprinkleIdx);
	List<PickupDetail> getSprinklePickup(Long sprinkleIdx);
	SprinkleDistribute getAvailableSprinkleDistribute(Long sprinkleIdx);
}
