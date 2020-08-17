package com.example.sprinkle.service;

import com.example.sprinkle.common.BaseConstants;
import com.example.sprinkle.common.code.ErrorCode;
import com.example.sprinkle.common.exception.BadRequestException;
import com.example.sprinkle.common.exception.BusinessException;
import com.example.sprinkle.entity.SprinkleDistribute;
import com.example.sprinkle.entity.SprinkleInfo;
import com.example.sprinkle.entity.SprinklePickup;
import com.example.sprinkle.entity.TokenInfo;
import com.example.sprinkle.entity.pk.TokenInfoPk;
import com.example.sprinkle.model.PickupDetail;
import com.example.sprinkle.model.SprinkleDetail;
import com.example.sprinkle.model.SprinkleHeader;
import com.example.sprinkle.model.SprinkleRequest;
import com.example.sprinkle.repository.SprinkleDistributeRepository;
import com.example.sprinkle.repository.SprinkleInfoRepository;
import com.example.sprinkle.repository.SprinklePickupRepository;
import com.example.sprinkle.repository.TokenInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@Slf4j
@SpringBootTest(classes = SprinkleService.class)
class SprinkleServiceTest {

	@Autowired
	private SprinkleService sprinkleService;

	@MockBean
	private SprinkleInfoRepository sprinkleInfoRepository;

	@MockBean
	private SprinkleDistributeRepository sprinkleDistributeRepository;

	@MockBean
	private SprinklePickupRepository sprinklePickupRepository;

	@MockBean
	private TokenInfoRepository tokenInfoRepository;

	@Test
	void sprinkleMoney_Success() {
		// given
		long userId = 101L;
		String roomId = "Room1";
		SprinkleHeader sprinkleHeader = new SprinkleHeader()
				.setUserId(userId)
				.setRoomId(roomId);
		SprinkleRequest sprinkleRequest = new SprinkleRequest()
				.setSprinkleAmount(1000)
				.setSprinkleCount(10);
		long activeCount = 5;
		given(sprinkleInfoRepository.getActiveSprinkleInfoCount(sprinkleHeader.getRoomId())).willReturn(activeCount);

		SprinkleInfo sprinkleInfo = new SprinkleInfo()
				.setIdx(1L)
				.setUserId(sprinkleHeader.getUserId())
				.setRoomId(sprinkleHeader.getRoomId())
				.setSprinkleAmount(sprinkleRequest.getSprinkleAmount())
				.setSprinkleCount(sprinkleRequest.getSprinkleCount())
				.setRegDate(LocalDateTime.now());
		given(sprinkleInfoRepository.save(ArgumentMatchers.any(SprinkleInfo.class))).willReturn(sprinkleInfo);

		// when
		long sprinkleIdx = sprinkleService.sprinkleMoney(sprinkleHeader, sprinkleRequest);
		log.info("# sprinkleIdx = {}", sprinkleIdx);

		// then
		assertThat(sprinkleIdx, is(greaterThan(0L)));
	}

	@Test
	void sprinkleMoney_Error_KP0101() {
		// given
		long userId = 101L;
		String roomId = "Room1";
		SprinkleHeader sprinkleHeader = new SprinkleHeader()
				.setUserId(userId)
				.setRoomId(roomId);
		SprinkleRequest sprinkleRequest = new SprinkleRequest()
				.setSprinkleAmount(1000)
				.setSprinkleCount(10);
		long activeCount = BaseConstants.SPRINKLE_ACTIVE_MAX + 1;
		given(sprinkleInfoRepository.getActiveSprinkleInfoCount(sprinkleHeader.getRoomId())).willReturn(activeCount);

		// when
		BusinessException exception = assertThrows(BusinessException.class, () -> {
			sprinkleService.sprinkleMoney(sprinkleHeader, sprinkleRequest);
		});

		// then
		assertThat(exception.getErrorCode(), is(ErrorCode.KP0101));
	}

	@Test
	void sprinkleMoney_Error_KP0104() {
		// given
		long userId = 101L;
		String roomId = "Room1";
		SprinkleHeader sprinkleHeader = new SprinkleHeader()
				.setUserId(userId)
				.setRoomId(roomId);
		SprinkleRequest sprinkleRequest = new SprinkleRequest()
				.setSprinkleAmount(1000)
				.setSprinkleCount(0);
		long activeCount = 5;
		given(sprinkleInfoRepository.getActiveSprinkleInfoCount(sprinkleHeader.getRoomId())).willReturn(activeCount);

		// when
		BusinessException exception = assertThrows(BusinessException.class, () -> {
			sprinkleService.sprinkleMoney(sprinkleHeader, sprinkleRequest);
		});

		// then
		assertThat(exception.getErrorCode(), is(ErrorCode.KP0104));
	}

	@Test
	void sprinkleMoney_Error_KP0102() {
		// given
		long userId = 101L;
		String roomId = "Room1";
		SprinkleHeader sprinkleHeader = new SprinkleHeader()
				.setUserId(userId)
				.setRoomId(roomId);
		SprinkleRequest sprinkleRequest = new SprinkleRequest()
				.setSprinkleAmount(1)
				.setSprinkleCount(2);
		long activeCount = 5;
		given(sprinkleInfoRepository.getActiveSprinkleInfoCount(sprinkleHeader.getRoomId())).willReturn(activeCount);

		// when
		BusinessException exception = assertThrows(BusinessException.class, () -> {
			sprinkleService.sprinkleMoney(sprinkleHeader, sprinkleRequest);
		});

		// then
		assertThat(exception.getErrorCode(), is(ErrorCode.KP0102));
	}

	@Test
	void sprinkleMoney_Error_KP0103() {
		// given
		long userId = 101L;
		String roomId = "Room1";
		SprinkleHeader sprinkleHeader = new SprinkleHeader()
				.setUserId(userId)
				.setRoomId(roomId);
		SprinkleRequest sprinkleRequest = new SprinkleRequest()
				.setSprinkleAmount(BaseConstants.SPRINKLE_MONEY_MAX + 1)
				.setSprinkleCount(5);
		long activeCount = 5;
		given(sprinkleInfoRepository.getActiveSprinkleInfoCount(sprinkleHeader.getRoomId())).willReturn(activeCount);

		// when
		BusinessException exception = assertThrows(BusinessException.class, () -> {
			sprinkleService.sprinkleMoney(sprinkleHeader, sprinkleRequest);
		});

		// then
		assertThat(exception.getErrorCode(), is(ErrorCode.KP0103));
	}

	@Test
	void checkPickupSprinkleMoney_Success() {
		// given
		long userId = 211L;
		String roomId = "Room1";
		String token = "ABC";
		SprinkleHeader sprinkleHeader = new SprinkleHeader()
				.setUserId(userId)
				.setRoomId(roomId);
		TokenInfoPk tokenInfoPk = new TokenInfoPk(roomId, token);
		TokenInfo tokenInfo = new TokenInfo()
				.setRoomId(roomId)
				.setToken(token)
				.setSprinkleIdx(1L);
		given(tokenInfoRepository.findById(tokenInfoPk)).willReturn(Optional.of(tokenInfo));

		SprinkleInfo sprinkleInfo = new SprinkleInfo()
				.setIdx(1L)
				.setUserId(101L)
				.setRoomId(sprinkleHeader.getRoomId())
				.setSprinkleAmount(1000)
				.setSprinkleCount(10)
				.setRegDate(LocalDateTime.now());
		given(sprinkleInfoRepository.findById(tokenInfo.getSprinkleIdx())).willReturn(Optional.of(sprinkleInfo));

		// when
		long sprinkleIdx = sprinkleService.checkPickupSprinkleMoney(sprinkleHeader, token);
		log.info("# sprinkleIdx = {}", sprinkleIdx);

		// then
		assertThat(sprinkleIdx, is(greaterThan(0L)));
	}

	@Test
	void checkPickupSprinkleMoney_Error_KP0002() {
		// given
		long userId = 211L;
		String roomId = "Room1";
		String token = "ABC";
		SprinkleHeader sprinkleHeader = new SprinkleHeader()
				.setUserId(userId)
				.setRoomId(roomId);
		TokenInfoPk tokenInfoPk = new TokenInfoPk(roomId, token);
		TokenInfo tokenInfo = new TokenInfo()
				.setRoomId(roomId)
				.setToken(token)
				.setSprinkleIdx(1L);
		given(tokenInfoRepository.findById(tokenInfoPk)).willReturn(Optional.of(tokenInfo));

		SprinkleInfo sprinkleInfo = new SprinkleInfo()
				.setIdx(1L)
				.setUserId(101L)
				.setRoomId("Room9")
				.setSprinkleAmount(1000)
				.setSprinkleCount(10)
				.setRegDate(LocalDateTime.now());
		given(sprinkleInfoRepository.findById(tokenInfo.getSprinkleIdx())).willReturn(Optional.of(sprinkleInfo));

		// when
		BadRequestException exception = assertThrows(BadRequestException.class, () -> {
			sprinkleService.checkPickupSprinkleMoney(sprinkleHeader, token);
		});

		// then
		assertThat(exception.getErrorCode(), is(ErrorCode.KP0002));
	}

	@Test
	void checkPickupSprinkleMoney_Error_KP0201() {
		// given
		long userId = 101L;
		String roomId = "Room1";
		String token = "ABC";
		SprinkleHeader sprinkleHeader = new SprinkleHeader()
				.setUserId(userId)
				.setRoomId(roomId);
		TokenInfoPk tokenInfoPk = new TokenInfoPk(roomId, token);
		TokenInfo tokenInfo = new TokenInfo()
				.setRoomId(roomId)
				.setToken(token)
				.setSprinkleIdx(1L);
		given(tokenInfoRepository.findById(tokenInfoPk)).willReturn(Optional.of(tokenInfo));

		SprinkleInfo sprinkleInfo = new SprinkleInfo()
				.setIdx(1L)
				.setUserId(101L)
				.setRoomId(sprinkleHeader.getRoomId())
				.setSprinkleAmount(1000)
				.setSprinkleCount(10)
				.setRegDate(LocalDateTime.now());
		given(sprinkleInfoRepository.findById(tokenInfo.getSprinkleIdx())).willReturn(Optional.of(sprinkleInfo));

		// when
		BusinessException exception = assertThrows(BusinessException.class, () -> {
			sprinkleService.checkPickupSprinkleMoney(sprinkleHeader, token);
		});

		// then
		assertThat(exception.getErrorCode(), is(ErrorCode.KP0201));
	}

	@Test
	void checkPickupSprinkleMoney_Error_KP0202() {
		// given
		long userId = 211L;
		String roomId = "Room1";
		String token = "ABC";
		SprinkleHeader sprinkleHeader = new SprinkleHeader()
				.setUserId(userId)
				.setRoomId(roomId);
		TokenInfoPk tokenInfoPk = new TokenInfoPk(roomId, token);
		TokenInfo tokenInfo = new TokenInfo()
				.setRoomId(roomId)
				.setToken(token)
				.setSprinkleIdx(1L);
		given(tokenInfoRepository.findById(tokenInfoPk)).willReturn(Optional.of(tokenInfo));

		SprinkleInfo sprinkleInfo = new SprinkleInfo()
				.setIdx(1L)
				.setUserId(101L)
				.setRoomId(sprinkleHeader.getRoomId())
				.setSprinkleAmount(1000)
				.setSprinkleCount(10)
				.setRegDate(LocalDateTime.now().minusMinutes(BaseConstants.PICKUP_TIMEOUT_MINUTES));
		given(sprinkleInfoRepository.findById(tokenInfo.getSprinkleIdx())).willReturn(Optional.of(sprinkleInfo));

		// when
		BusinessException exception = assertThrows(BusinessException.class, () -> {
			sprinkleService.checkPickupSprinkleMoney(sprinkleHeader, token);
		});

		// then
		assertThat(exception.getErrorCode(), is(ErrorCode.KP0202));
	}

	@Test
	void pickupSprinkleMoney_Success() {
		// given
		long userId = 211L;
		long sprinkleIdx = 1L;
		given(sprinklePickupRepository.getUserSprinklePickup(userId, sprinkleIdx)).willReturn(null);

		SprinkleDistribute sprinkleDistribute = new SprinkleDistribute()
				.setIdx(30L)
				.setSprinkleIdx(1L)
				.setDistributeAmount(10);
		given(sprinklePickupRepository.getAvailableSprinkleDistribute(sprinkleIdx)).willReturn(sprinkleDistribute);

		// when
		int distributeAmount = sprinkleService.pickupSprinkleMoney(userId, sprinkleIdx);
		log.info("# distributeAmount = {}", distributeAmount);

		// then
		assertThat(distributeAmount, is(greaterThan(0)));
	}

	@Test
	void pickupSprinkleMoney_Error_KP0203() {
		// given
		long userId = 211L;
		long sprinkleIdx = 1L;
		SprinklePickup sprinklePickup = new SprinklePickup()
				.setDistributeIdx(1L)
				.setUserId(userId)
				.setRegDate(LocalDateTime.now());
		given(sprinklePickupRepository.getUserSprinklePickup(userId, sprinkleIdx)).willReturn(sprinklePickup);

		// when
		BusinessException exception = assertThrows(BusinessException.class, () -> {
			sprinkleService.pickupSprinkleMoney(userId, sprinkleIdx);
		});

		// then
		assertThat(exception.getErrorCode(), is(ErrorCode.KP0203));
	}

	@Test
	void pickupSprinkleMoney_Error_KP0204() {
		// given
		long userId = 211L;
		long sprinkleIdx = 1L;
		given(sprinklePickupRepository.getUserSprinklePickup(userId, sprinkleIdx)).willReturn(null);
		given(sprinklePickupRepository.getAvailableSprinkleDistribute(sprinkleIdx)).willReturn(null);

		// when
		BusinessException exception = assertThrows(BusinessException.class, () -> {
			sprinkleService.pickupSprinkleMoney(userId, sprinkleIdx);
		});

		// then
		assertThat(exception.getErrorCode(), is(ErrorCode.KP0204));
	}

	@Test
	void getSprinkleDetail_Success() {
		// given
		long userId = 101L;
		String roomId = "Room1";
		String token = "ABC";
		SprinkleHeader sprinkleHeader = new SprinkleHeader()
				.setUserId(userId)
				.setRoomId(roomId);
		TokenInfoPk tokenInfoPk = new TokenInfoPk(roomId, token);
		TokenInfo tokenInfo = new TokenInfo()
				.setRoomId(roomId)
				.setToken(token)
				.setSprinkleIdx(1L);
		given(tokenInfoRepository.findById(tokenInfoPk)).willReturn(Optional.of(tokenInfo));

		SprinkleInfo sprinkleInfo = new SprinkleInfo()
				.setIdx(1L)
				.setUserId(101L)
				.setRoomId(sprinkleHeader.getRoomId())
				.setSprinkleAmount(1000)
				.setSprinkleCount(10)
				.setRegDate(LocalDateTime.now());
		given(sprinkleInfoRepository.findById(tokenInfo.getSprinkleIdx())).willReturn(Optional.of(sprinkleInfo));

		List<PickupDetail> pickupDetails = Arrays.asList(
				new PickupDetail().setPickupAmount(200).setUserId(211L),
				new PickupDetail().setPickupAmount(300).setUserId(212L)
		);
		given(sprinklePickupRepository.getSprinklePickup(sprinkleInfo.getIdx())).willReturn(pickupDetails);

		// when
		SprinkleDetail sprinkleDetail = sprinkleService.getSprinkleDetail(sprinkleHeader, token);
		log.info("# sprinkleDetail = {}", sprinkleDetail);

		// then
		assertThat(sprinkleDetail, is(notNullValue()));
	}

	@Test
	void getSprinkleDetail_Error_KP0002() {
		// given
		long userId = 201L;
		String roomId = "Room1";
		String token = "ABC";
		SprinkleHeader sprinkleHeader = new SprinkleHeader()
				.setUserId(userId)
				.setRoomId(roomId);
		TokenInfoPk tokenInfoPk = new TokenInfoPk(roomId, token);
		TokenInfo tokenInfo = new TokenInfo()
				.setRoomId(roomId)
				.setToken(token)
				.setSprinkleIdx(1L);
		given(tokenInfoRepository.findById(tokenInfoPk)).willReturn(Optional.of(tokenInfo));

		SprinkleInfo sprinkleInfo = new SprinkleInfo()
				.setIdx(1L)
				.setUserId(101L)
				.setRoomId(sprinkleHeader.getRoomId())
				.setSprinkleAmount(1000)
				.setSprinkleCount(10)
				.setRegDate(LocalDateTime.now());
		given(sprinkleInfoRepository.findById(tokenInfo.getSprinkleIdx())).willReturn(Optional.of(sprinkleInfo));

		// when
		BadRequestException exception = assertThrows(BadRequestException.class, () -> {
			sprinkleService.getSprinkleDetail(sprinkleHeader, token);
		});

		// then
		assertThat(exception.getErrorCode(), is(ErrorCode.KP0002));
	}

	@Test
	void getSprinkleDetail_Error_KP0301() {
		// given
		long userId = 101L;
		String roomId = "Room1";
		String token = "ABC";
		SprinkleHeader sprinkleHeader = new SprinkleHeader()
				.setUserId(userId)
				.setRoomId(roomId);
		TokenInfoPk tokenInfoPk = new TokenInfoPk(roomId, token);
		TokenInfo tokenInfo = new TokenInfo()
				.setRoomId(roomId)
				.setToken(token)
				.setSprinkleIdx(1L);
		given(tokenInfoRepository.findById(tokenInfoPk)).willReturn(Optional.of(tokenInfo));

		SprinkleInfo sprinkleInfo = new SprinkleInfo()
				.setIdx(1L)
				.setUserId(101L)
				.setRoomId(sprinkleHeader.getRoomId())
				.setSprinkleAmount(1000)
				.setSprinkleCount(10)
				.setRegDate(LocalDateTime.now().minusDays(BaseConstants.SEARCH_TIMEOUT_DAYS));
		given(sprinkleInfoRepository.findById(tokenInfo.getSprinkleIdx())).willReturn(Optional.of(sprinkleInfo));

		// when
		BusinessException exception = assertThrows(BusinessException.class, () -> {
			sprinkleService.getSprinkleDetail(sprinkleHeader, token);
		});

		// then
		assertThat(exception.getErrorCode(), is(ErrorCode.KP0301));
	}
}
