package com.example.sprinkle.repository;

import com.example.sprinkle.entity.SprinkleInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class SprinkleInfoRepositoryTest {

	@Autowired
	private SprinkleInfoRepository sprinkleInfoRepository;

	@Test
	void SprinkleInfo() {
		SprinkleInfo sprinkleInfo = new SprinkleInfo()
				.setUserId(1L)
				.setRoomId("Room1")
				.setSprinkleAmount(1000)
				.setSprinkleCount(10);
		SprinkleInfo savedSprinkleInfo = sprinkleInfoRepository.save(sprinkleInfo);
		assertThat(savedSprinkleInfo.getIdx(), is(notNullValue()));
	}

	@Test
	void getActiveSprinkleInfoCount() {
		String roomId = "Room1";
		List<SprinkleInfo> sprinkleInfos = Arrays.asList(
				new SprinkleInfo().setIdx(1L).setUserId(1L).setRoomId(roomId).setSprinkleAmount(1000).setSprinkleCount(5).setRegDate(LocalDateTime.now()),
				new SprinkleInfo().setIdx(2L).setUserId(2L).setRoomId(roomId).setSprinkleAmount(5000).setSprinkleCount(3).setRegDate(LocalDateTime.now()),
				new SprinkleInfo().setIdx(3L).setUserId(3L).setRoomId(roomId).setSprinkleAmount(3000).setSprinkleCount(2).setRegDate(LocalDateTime.now())
		);
		sprinkleInfoRepository.saveAll(sprinkleInfos);

		long activeCount = sprinkleInfoRepository.getActiveSprinkleInfoCount(roomId);
		log.info("# activeCount = {}", activeCount);
		assertThat(activeCount, is(greaterThan(0L)));
	}
}