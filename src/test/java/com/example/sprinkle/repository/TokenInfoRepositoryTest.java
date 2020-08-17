package com.example.sprinkle.repository;

import com.example.sprinkle.entity.TokenInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class TokenInfoRepositoryTest {

	@Autowired
	private TokenInfoRepository tokenInfoRepository;

	@Test
	void saveTokenInfo() {
		TokenInfo tokenInfo = new TokenInfo()
				.setRoomId("Room1")
				.setToken("ABC")
				.setSprinkleIdx(1L);
		TokenInfo savedTokenInfo = tokenInfoRepository.save(tokenInfo);
		assertThat(savedTokenInfo, is(notNullValue()));
	}
}