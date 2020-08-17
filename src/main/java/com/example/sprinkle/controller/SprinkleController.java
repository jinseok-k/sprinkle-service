package com.example.sprinkle.controller;

import com.example.sprinkle.common.BaseConstants;
import com.example.sprinkle.common.response.RestResult;
import com.example.sprinkle.model.SprinkleDetail;
import com.example.sprinkle.model.SprinkleHeader;
import com.example.sprinkle.model.SprinkleRequest;
import com.example.sprinkle.service.SprinkleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/sprinkle")
@RequiredArgsConstructor
@RestController
public class SprinkleController extends BaseController {

	private final SprinkleService sprinkleService;

	@PostMapping
	public ResponseEntity<RestResult> sprinkleMoney(
			@RequestHeader(BaseConstants.USER_HEADER) int userId,
			@RequestHeader(BaseConstants.ROOM_HEADER) String roomId,
			@RequestBody SprinkleRequest sprinkleRequest) {
		SprinkleHeader sprinkleHeader = new SprinkleHeader()
				.setUserId(userId)
				.setRoomId(roomId);
		long sprinkleIdx = sprinkleService.sprinkleMoney(sprinkleHeader, sprinkleRequest);
		// 고유 토큰 발행
		String token = null;
		while (token == null) {
			try {
				token = sprinkleService.createToken(sprinkleIdx, roomId);
			} catch (DataIntegrityViolationException e) {
				log.warn("# Retry creating token...");
			}
		}
		return success(token);
	}

	@PostMapping("/pickup")
	public ResponseEntity<RestResult> pickupSprinkleMoney(
			@RequestHeader(BaseConstants.USER_HEADER) int userId,
			@RequestHeader(BaseConstants.ROOM_HEADER) String roomId,
			@RequestParam String token) {
		SprinkleHeader sprinkleHeader = new SprinkleHeader()
				.setUserId(userId)
				.setRoomId(roomId);
		long sprinkleIdx = sprinkleService.checkPickupSprinkleMoney(sprinkleHeader, token);
		// 분배 건 할당
		int pickupAmount = 0;
		while (pickupAmount == 0) {
			try {
				pickupAmount = sprinkleService.pickupSprinkleMoney(userId, sprinkleIdx);
			} catch (DataIntegrityViolationException e) {
				log.warn("# Retry pickup amount...");
			}
		}
		return success(pickupAmount);
	}

	@GetMapping
	public ResponseEntity<RestResult> getSprinkleDetail(
			@RequestHeader(BaseConstants.USER_HEADER) int userId,
			@RequestHeader(BaseConstants.ROOM_HEADER) String roomId,
			@RequestParam String token) {
		SprinkleHeader sprinkleHeader = new SprinkleHeader()
				.setUserId(userId)
				.setRoomId(roomId);
		SprinkleDetail sprinkleDetail = sprinkleService.getSprinkleDetail(sprinkleHeader, token);
		return success(sprinkleDetail);
	}
}
