package com.example.sprinkle.simple;

import com.example.sprinkle.common.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@Slf4j
public class UtilsTest {

	@Test
	void getToken() {
		String token = Utils.getToken(3);
		log.info("# token = {}", token);
	}

	@Test
	void getRandomNumber() {
		int randomNumber = Utils.getRandomNumber(1000);
		log.info("# randomNumber = {}", randomNumber);
	}

	@Test
	void getDistributeNumbers() {
		int totalAmount = 1000;
		int totalCount = 10;
		List<Integer> distributeNumbers = Utils.getDistributeNumbers(totalAmount, totalCount, 1);
		int distributeTotal = distributeNumbers.stream().reduce(0, Integer::sum);
		log.info("# distributeNumbers = {}", distributeNumbers);
		log.info("# distributeTotal = {}", distributeTotal);
		assertThat(distributeTotal, is(totalAmount));
	}
}
