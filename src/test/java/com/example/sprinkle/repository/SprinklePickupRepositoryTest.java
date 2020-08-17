package com.example.sprinkle.repository;

import com.example.sprinkle.entity.SprinkleDistribute;
import com.example.sprinkle.entity.SprinklePickup;
import com.example.sprinkle.model.PickupDetail;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class SprinklePickupRepositoryTest {

	@Autowired
	private SprinklePickupRepository sprinklePickupRepository;

	@Autowired
	private SprinkleDistributeRepository sprinkleDistributeRepository;

	@Test
	void saveSprinklePickup() {
		SprinklePickup sprinklePickup = new SprinklePickup()
				.setDistributeIdx(1L)
				.setUserId(1L)
				.setRegDate(LocalDateTime.now());
		SprinklePickup savedSprinklePickup = sprinklePickupRepository.save(sprinklePickup);
		assertThat(savedSprinklePickup, is(notNullValue()));
	}

	@Test
	void getUserSprinklePickup() {
		Long userId = 201L;
		Long sprinkleIdx = 1L;
		Long distributeIdx = 1L;
		SprinkleDistribute sprinkleDistribute = new SprinkleDistribute()
				.setIdx(distributeIdx)
				.setSprinkleIdx(sprinkleIdx)
				.setDistributeAmount(1000);
		sprinkleDistributeRepository.save(sprinkleDistribute);

		SprinklePickup sprinklePickup = new SprinklePickup()
				.setDistributeIdx(distributeIdx)
				.setUserId(userId)
				.setRegDate(LocalDateTime.now());
		sprinklePickupRepository.save(sprinklePickup);

		SprinklePickup userSprinklePickup = sprinklePickupRepository.getUserSprinklePickup(userId, sprinkleIdx);
		log.info("# userSprinklePickup = {}", userSprinklePickup);
		assertThat(userSprinklePickup, is(notNullValue()));
	}

	@Test
	void getSprinklePickup() {
		Long sprinkleIdx = 1L;
		List<SprinkleDistribute> sprinkleDistributes = Arrays.asList(
				new SprinkleDistribute().setIdx(1L).setSprinkleIdx(sprinkleIdx).setDistributeAmount(600),
				new SprinkleDistribute().setIdx(2L).setSprinkleIdx(sprinkleIdx).setDistributeAmount(300),
				new SprinkleDistribute().setIdx(3L).setSprinkleIdx(sprinkleIdx).setDistributeAmount(100)
		);
		sprinkleDistributeRepository.saveAll(sprinkleDistributes);

		List<SprinklePickup> sprinklePickups = Arrays.asList(
				new SprinklePickup().setUserId(201L).setDistributeIdx(1L).setRegDate(LocalDateTime.now()),
				new SprinklePickup().setUserId(202L).setDistributeIdx(2L).setRegDate(LocalDateTime.now())
		);
		sprinklePickupRepository.saveAll(sprinklePickups);

		List<PickupDetail> pickupDetails = sprinklePickupRepository.getSprinklePickup(sprinkleIdx);
		log.info("# pickupDetails = {}", pickupDetails);
		assertThat(pickupDetails, is(notNullValue()));
	}

	@Test
	void getAvailableSprinkleDistribute() {
		Long sprinkleIdx = 1L;
		List<SprinkleDistribute> sprinkleDistributes = Arrays.asList(
				new SprinkleDistribute().setIdx(1L).setSprinkleIdx(sprinkleIdx).setDistributeAmount(600),
				new SprinkleDistribute().setIdx(2L).setSprinkleIdx(sprinkleIdx).setDistributeAmount(300),
				new SprinkleDistribute().setIdx(3L).setSprinkleIdx(sprinkleIdx).setDistributeAmount(100)
		);
		sprinkleDistributeRepository.saveAll(sprinkleDistributes);

		List<SprinklePickup> sprinklePickups = Arrays.asList(
				new SprinklePickup().setUserId(201L).setDistributeIdx(1L).setRegDate(LocalDateTime.now()),
				new SprinklePickup().setUserId(202L).setDistributeIdx(2L).setRegDate(LocalDateTime.now())
		);
		sprinklePickupRepository.saveAll(sprinklePickups);

		SprinkleDistribute sprinkleDistribute = sprinklePickupRepository.getAvailableSprinkleDistribute(sprinkleIdx);
		log.info("# sprinkleDistribute = {}", sprinkleDistribute);
		assertThat(sprinkleDistribute, is(notNullValue()));
	}
}