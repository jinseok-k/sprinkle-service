package com.example.sprinkle.controller;

import com.example.sprinkle.common.BaseConstants;
import com.example.sprinkle.common.util.Utils;
import com.example.sprinkle.model.PickupDetail;
import com.example.sprinkle.model.SprinkleDetail;
import com.example.sprinkle.model.SprinkleHeader;
import com.example.sprinkle.model.SprinkleRequest;
import com.example.sprinkle.service.SprinkleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SprinkleController.class)
class SprinkleControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private SprinkleService sprinkleService;

	@Test
	void sprinkleMoney_Success() throws Exception {
		// given
		long userId = 101L;
		long sprinkleIdx = 1L;
		String roomId = "Room1";
		SprinkleHeader sprinkleHeader = new SprinkleHeader()
				.setUserId(userId)
				.setRoomId(roomId);
		SprinkleRequest sprinkleRequest = new SprinkleRequest()
				.setSprinkleAmount(1000)
				.setSprinkleCount(10);
		given(sprinkleService.sprinkleMoney(sprinkleHeader, sprinkleRequest)).willReturn(sprinkleIdx);
		given(sprinkleService.createToken(sprinkleIdx, roomId)).willReturn("ABC");

		// when
		final ResultActions actions = mockMvc.perform(post("/sprinkle")
				.header(BaseConstants.USER_HEADER, userId)
				.header(BaseConstants.ROOM_HEADER, roomId)
				.content(Utils.toJson(sprinkleRequest))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print());

		// then
		actions.andExpect(status().isOk());
	}

	@Test
	void sprinkleMoney_DuplicateOnce() throws Exception {
		// given
		long userId = 101L;
		long sprinkleIdx = 1L;
		String roomId = "Room1";
		SprinkleHeader sprinkleHeader = new SprinkleHeader()
				.setUserId(userId)
				.setRoomId(roomId);
		SprinkleRequest sprinkleRequest = new SprinkleRequest()
				.setSprinkleAmount(1000)
				.setSprinkleCount(10);
		given(sprinkleService.sprinkleMoney(sprinkleHeader, sprinkleRequest)).willReturn(sprinkleIdx);
		given(sprinkleService.createToken(sprinkleIdx, roomId)).willThrow(new DataIntegrityViolationException("Duplicate Entry...")).willReturn("ABC");

		// when
		final ResultActions actions = mockMvc.perform(post("/sprinkle")
				.header(BaseConstants.USER_HEADER, userId)
				.header(BaseConstants.ROOM_HEADER, roomId)
				.content(Utils.toJson(sprinkleRequest))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print());

		// then
		actions.andExpect(status().isOk());
	}

	@Test
	void pickupSprinkleMoney_Success() throws Exception {
		// given
		long userId = 211L;
		long sprinkleIdx = 1L;
		String roomId = "Room1";
		String token = "ABC";
		SprinkleHeader sprinkleHeader = new SprinkleHeader()
				.setUserId(userId)
				.setRoomId(roomId);
		int pickupAmount = 100;
		given(sprinkleService.checkPickupSprinkleMoney(sprinkleHeader, token)).willReturn(sprinkleIdx);
		given(sprinkleService.pickupSprinkleMoney(userId, sprinkleIdx)).willReturn(pickupAmount);

		// when
		final ResultActions actions = mockMvc.perform(post("/sprinkle/pickup")
				.header(BaseConstants.USER_HEADER, userId)
				.header(BaseConstants.ROOM_HEADER, roomId)
				.param(BaseConstants.TOKEN_PARAM, token)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print());

		// then
		actions.andExpect(status().isOk());
	}

	@Test
	void pickupSprinkleMoney_DuplicateOnce() throws Exception {
		// given
		long userId = 211L;
		long sprinkleIdx = 1L;
		String roomId = "Room1";
		String token = "ABC";
		SprinkleHeader sprinkleHeader = new SprinkleHeader()
				.setUserId(userId)
				.setRoomId(roomId);
		int pickupAmount = 100;
		given(sprinkleService.checkPickupSprinkleMoney(sprinkleHeader, token)).willReturn(sprinkleIdx);
		given(sprinkleService.pickupSprinkleMoney(userId, sprinkleIdx)).willThrow(new DataIntegrityViolationException("Duplicate Entry...")).willReturn(pickupAmount);

		// when
		final ResultActions actions = mockMvc.perform(post("/sprinkle/pickup")
				.header(BaseConstants.USER_HEADER, userId)
				.header(BaseConstants.ROOM_HEADER, roomId)
				.param(BaseConstants.TOKEN_PARAM, token)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print());

		// then
		actions.andExpect(status().isOk());
	}

	@Test
	void getSprinkleDetail() throws Exception {
		// given
		long userId = 101L;
		String roomId = "Room1";
		String token = "ABC";
		SprinkleHeader sprinkleHeader = new SprinkleHeader()
				.setUserId(userId)
				.setRoomId(roomId);
		List<PickupDetail> pickupDetails = Arrays.asList(
				new PickupDetail().setPickupAmount(200).setUserId(211L),
				new PickupDetail().setPickupAmount(300).setUserId(212L)
		);
		SprinkleDetail sprinkleDetail = new SprinkleDetail()
				.setSprinkleTime(LocalDateTime.now())
				.setSprinkleAmount(1000)
				.setPickupAmount(500)
				.setPickupDetails(pickupDetails);

		given(sprinkleService.getSprinkleDetail(sprinkleHeader, token)).willReturn(sprinkleDetail);

		// when
		final ResultActions actions = mockMvc.perform(get("/sprinkle")
				.header(BaseConstants.USER_HEADER, userId)
				.header(BaseConstants.ROOM_HEADER, roomId)
				.param(BaseConstants.TOKEN_PARAM, token)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print());

		// then
		actions.andExpect(status().isOk());
	}
}