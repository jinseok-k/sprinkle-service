package com.example.sprinkle.repository;

import com.example.sprinkle.entity.SprinkleDistribute;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class SprinkleDistributeRepositoryTest {

	@Autowired
	private SprinkleDistributeRepository sprinkleDistributeRepository;

	@Test
	void saveSprinkleDistribute() {
		SprinkleDistribute sprinkleDistribute = new SprinkleDistribute()
				.setSprinkleIdx(1L)
				.setDistributeAmount(1000);
		SprinkleDistribute savedSprinkleDistribute = sprinkleDistributeRepository.save(sprinkleDistribute);
		assertThat(savedSprinkleDistribute.getIdx(), is(notNullValue()));
	}
}
