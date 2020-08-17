package com.example.sprinkle.repository;

import com.example.sprinkle.common.BaseConstants;
import com.example.sprinkle.entity.SprinkleInfo;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.time.LocalDateTime;

import static com.example.sprinkle.entity.QSprinkleInfo.sprinkleInfo;

public class SprinkleInfoRepositoryCustomImpl extends QuerydslRepositorySupport implements SprinkleInfoRepositoryCustom {

	public SprinkleInfoRepositoryCustomImpl() {
		super(SprinkleInfo.class);
	}

	@Override
	public long getActiveSprinkleInfoCount(String roomId) {
		return from(sprinkleInfo)
				.select(sprinkleInfo.count()
				).where(
						sprinkleInfo.roomId.eq(roomId),
						sprinkleInfo.regDate.after(LocalDateTime.now().minusDays(BaseConstants.SEARCH_TIMEOUT_DAYS))
				).fetchOne();
	}
}
