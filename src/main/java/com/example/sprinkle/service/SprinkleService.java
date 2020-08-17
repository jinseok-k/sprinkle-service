package com.example.sprinkle.service;

import com.example.sprinkle.common.BaseConstants;
import com.example.sprinkle.common.code.ErrorCode;
import com.example.sprinkle.common.exception.BadRequestException;
import com.example.sprinkle.common.exception.BusinessException;
import com.example.sprinkle.common.util.Utils;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class SprinkleService {

	private final SprinkleInfoRepository sprinkleInfoRepository;

	private final SprinkleDistributeRepository sprinkleDistributeRepository;

	private final SprinklePickupRepository sprinklePickupRepository;

	private final TokenInfoRepository tokenInfoRepository;

	public long sprinkleMoney(SprinkleHeader sprinkleHeader, SprinkleRequest sprinkleRequest) {
		// 대화방 내 뿌리기 진행중인 건수 확인
		String roomId = sprinkleHeader.getRoomId();
		long activeCount = sprinkleInfoRepository.getActiveSprinkleInfoCount(roomId);
		log.info("# activeCount = {}", activeCount);
		if(activeCount > BaseConstants.SPRINKLE_ACTIVE_MAX) {
			throw new BusinessException(ErrorCode.KP0101);
		}
		int sprinkleAmount = sprinkleRequest.getSprinkleAmount();
		int sprinkleCount = sprinkleRequest.getSprinkleCount();

		// 뿌리기 요청 최소 인원수 확인
		if(sprinkleCount < 1) {
			throw new BusinessException(ErrorCode.KP0104);
		}

		// 뿌리는 금액은 인원수 보다 커야함
		if(sprinkleAmount < sprinkleCount) {
			throw new BusinessException(ErrorCode.KP0102);
		}

		// 뿌리는 금액 최대값 한정
		if(sprinkleAmount > BaseConstants.SPRINKLE_MONEY_MAX) {
			throw new BusinessException(ErrorCode.KP0103);
		}

		// 뿌리기 생성
		SprinkleInfo sprinkleInfo = new SprinkleInfo()
				.setUserId(sprinkleHeader.getUserId())
				.setRoomId(roomId)
				.setSprinkleAmount(sprinkleAmount)
				.setSprinkleCount(sprinkleCount)
				.setRegDate(LocalDateTime.now());
		sprinkleInfo = sprinkleInfoRepository.save(sprinkleInfo);

		// 뿌릴 금액을 인원 수에 맞게 분배해서 저장
		final long sprinkleIdx = sprinkleInfo.getIdx();
		List<Integer> distributeNumbers = Utils.getDistributeNumbers(sprinkleAmount, sprinkleCount, 1);
		List<SprinkleDistribute> sprinkleDistributes = distributeNumbers.stream().map(x -> new SprinkleDistribute()
				.setSprinkleIdx(sprinkleIdx)
				.setDistributeAmount(x)).collect(Collectors.toList());
		sprinkleDistributeRepository.saveAll(sprinkleDistributes);
		return sprinkleIdx;
	}

	public String createToken(long sprinkleIdx, String roomId) {
		// 뿌리기 고유 토큰 생성
		String token = Utils.getToken();
		log.info("# tokenInfoPk: roomId = {}, token = {}", roomId, token);
		TokenInfo tokenInfo = new TokenInfo()
				.setRoomId(roomId)
				.setToken(token)
				.setSprinkleIdx(sprinkleIdx);
		tokenInfoRepository.save(tokenInfo);
		return token;
	}

	public long checkPickupSprinkleMoney(SprinkleHeader sprinkleHeader, String token) {
		// 뿌리기 조회
		long userId = sprinkleHeader.getUserId();
		TokenInfo tokenInfo = getTokenInfo(sprinkleHeader.getRoomId(), token);
		SprinkleInfo sprinkleInfo = getSprinkleInfo(tokenInfo.getSprinkleIdx());
		log.info("# tokenInfo = {}", tokenInfo);
		log.info("# sprinkleInfo = {}", sprinkleInfo);

		// 뿌리기가 호출된 대화방인지 확인, 잘못된 요청으로 처리(토큰 조회 시 대화방 아이디를 사용하므로 이 조건을 충족할 일은 없음)
		if(!sprinkleInfo.getRoomId().equals(sprinkleHeader.getRoomId())) {
			log.error("뿌리기 호출된 대화방에서만 받기가 가능합니다.");
			throw new BadRequestException(ErrorCode.KP0002);
		}

		// 뿌리기의 소유 확인, 자신은 받을 수 없음
		if(userId == sprinkleInfo.getUserId().longValue()) {
			throw new BusinessException(ErrorCode.KP0201);
		}

		// 뿌리기의 받기 유효시간 확인
		if(LocalDateTime.now().isAfter(sprinkleInfo.getRegDate().plusMinutes(BaseConstants.PICKUP_TIMEOUT_MINUTES))) {
			throw new BusinessException(ErrorCode.KP0202);
		}

		return sprinkleInfo.getIdx();
	}

	public int pickupSprinkleMoney(long userId, long sprinkleIdx) {
		// 뿌리기를 이미 받았는지 확인
		SprinklePickup sprinklePickup = sprinklePickupRepository.getUserSprinklePickup(userId, sprinkleIdx);
		if(sprinklePickup != null) {
			throw new BusinessException(ErrorCode.KP0203);
		}

		// 분배 건 할당
		SprinkleDistribute sprinkleDistribute = sprinklePickupRepository.getAvailableSprinkleDistribute(sprinkleIdx);
		// 이미 분배값을 모두 소진한 경우 마감 처리
		if(sprinkleDistribute == null) {
			throw new BusinessException(ErrorCode.KP0204);
		}
		sprinklePickup = new SprinklePickup()
				.setDistributeIdx(sprinkleDistribute.getIdx())
				.setUserId(userId)
				.setRegDate(LocalDateTime.now());
		sprinklePickupRepository.save(sprinklePickup);

		return sprinkleDistribute.getDistributeAmount();
	}

	public SprinkleDetail getSprinkleDetail(SprinkleHeader sprinkleHeader, String token) {
		// 뿌리기 조회
		TokenInfo tokenInfo = getTokenInfo(sprinkleHeader.getRoomId(), token);
		SprinkleInfo sprinkleInfo = getSprinkleInfo(tokenInfo.getSprinkleIdx());
		log.info("# tokenInfo = {}", tokenInfo);
		log.info("# sprinkleInfo = {}", sprinkleInfo);

		// 뿌리기의 소유 확인, 자신만 조회 가능
		if(sprinkleInfo.getUserId().longValue() != sprinkleHeader.getUserId()) {
			log.error("뿌리기 소유자만 조회가 가능합니다.");
			throw new BadRequestException(ErrorCode.KP0002);
		}

		// 뿌리기의 조회 유효시간 확인
		if(LocalDateTime.now().isAfter(sprinkleInfo.getRegDate().plusDays(BaseConstants.SEARCH_TIMEOUT_DAYS))) {
			throw new BusinessException(ErrorCode.KP0301);
		}

		// 뿌리기 상세 조회
		List<PickupDetail> pickupDetails = sprinklePickupRepository.getSprinklePickup(sprinkleInfo.getIdx());
		int pickupAmount = pickupDetails.stream().map(PickupDetail::getPickupAmount).reduce(0, Integer::sum);

		return new SprinkleDetail()
				.setSprinkleTime(sprinkleInfo.getRegDate())
				.setSprinkleAmount(sprinkleInfo.getSprinkleAmount())
				.setPickupAmount(pickupAmount)
				.setPickupDetails(pickupDetails);
	}

	private TokenInfo getTokenInfo(String roomId, String token) {
		return getTokenInfo(roomId, token, false);
	}

	private TokenInfo getTokenInfo(String roomId, String token, boolean nullable) {
		TokenInfoPk tokenInfoPk = new TokenInfoPk(roomId, token);
		TokenInfo tokenInfo = tokenInfoRepository.findById(tokenInfoPk).orElse(null);
		if(!nullable && tokenInfo == null) {
			log.error("토큰 정보가 존재하지 않습니다.");
			throw new BadRequestException(ErrorCode.KP0002);
		}
		return tokenInfo;
	}

	private SprinkleInfo getSprinkleInfo(Long idx) {
		SprinkleInfo sprinkleInfo = sprinkleInfoRepository.findById(idx).orElse(null);
		if(sprinkleInfo == null) {
			log.error("뿌리기 정보가 존재하지 않습니다.");
			throw new BadRequestException(ErrorCode.KP0002);
		}
		return sprinkleInfo;
	}

	private SprinklePickup getSprinklePickup(Long distributeIdx) {
		return sprinklePickupRepository.findById(distributeIdx).orElse(null);
	}
}
