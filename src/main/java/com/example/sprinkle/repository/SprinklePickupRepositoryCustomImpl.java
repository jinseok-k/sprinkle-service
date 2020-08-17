package com.example.sprinkle.repository;

import com.example.sprinkle.entity.SprinkleDistribute;
import com.example.sprinkle.entity.SprinklePickup;
import com.example.sprinkle.model.PickupDetail;
import com.querydsl.core.types.Projections;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

import static com.example.sprinkle.entity.QSprinkleDistribute.sprinkleDistribute;
import static com.example.sprinkle.entity.QSprinklePickup.sprinklePickup;

public class SprinklePickupRepositoryCustomImpl extends QuerydslRepositorySupport implements SprinklePickupRepositoryCustom {

	public SprinklePickupRepositoryCustomImpl() {
		super(SprinklePickup.class);
	}

	@Override
	public SprinklePickup getUserSprinklePickup(Long userId, Long sprinkleIdx) {
		return from(sprinkleDistribute).join(sprinklePickup).on(sprinkleDistribute.idx.eq(sprinklePickup.distributeIdx))
				.select(Projections.fields(SprinklePickup.class,
						sprinklePickup.distributeIdx,
						sprinklePickup.userId,
						sprinklePickup.regDate)
				).where(
						sprinkleDistribute.sprinkleIdx.eq(sprinkleIdx),
						sprinklePickup.userId.eq(userId)
				).fetchOne();
	}

	@Override
	public List<PickupDetail> getSprinklePickup(Long sprinkleIdx) {
		return from(sprinkleDistribute).join(sprinklePickup).on(sprinkleDistribute.idx.eq(sprinklePickup.distributeIdx))
				.select(Projections.constructor(PickupDetail.class,
						sprinkleDistribute.distributeAmount,
						sprinklePickup.userId)
				).where(
						sprinkleDistribute.sprinkleIdx.eq(sprinkleIdx)
				).orderBy(sprinklePickup.regDate.asc()).fetch();
	}

	@Override
	public SprinkleDistribute getAvailableSprinkleDistribute(Long sprinkleIdx) {
		return from(sprinkleDistribute).leftJoin(sprinklePickup).on(sprinkleDistribute.idx.eq(sprinklePickup.distributeIdx))
				.select(Projections.fields(SprinkleDistribute.class,
						sprinkleDistribute.idx,
						sprinkleDistribute.distributeAmount)
				).where(
						sprinkleDistribute.sprinkleIdx.eq(sprinkleIdx),
						sprinklePickup.userId.isNull()
				).orderBy(sprinkleDistribute.idx.asc()).fetchFirst();
	}
}
