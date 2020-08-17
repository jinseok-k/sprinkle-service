package com.example.sprinkle.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
public class Utils {

	private static Random random = new Random();

	private Utils(){
		throw new IllegalStateException("Utils class");
	}

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	static {
		OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		OBJECT_MAPPER.registerModule(new JavaTimeModule());
	}

	/**
	 * 오브젝트를 json 문자열로 리턴한다.
	 * @param o : 오브젝트
	 * @return : json 문자열
	 */
	public static String toJson(Object o) {
		String json = null;
		try {
			json = OBJECT_MAPPER.writeValueAsString(o);
		} catch (JsonProcessingException e) {
			log.warn(e.getMessage());
		}
		return json;
	}

	/**
	 * json 문자열을 T 오브젝트로 리턴한다.
	 * @param json : json 문자열
	 * @param type : T 클래스
	 * @return : T 오브젝트
	 */
	public static <T> T fromJson(String json, Class<T> type) {
		if(json == null || type == null) {
			return null;
		}
		T result = null;
		try {
			result = OBJECT_MAPPER.readValue(json, type);
		} catch (IOException e) {
			log.warn(e.getMessage());
		}
		return result;
	}

	/**
	 * json 문자열을 T 오브젝트로 리턴한다.
	 * @param json : json 문자열
	 * @param type : T 타입레퍼런스(Generic class 용도)
	 * @return : T 오브젝트
	 */
	public static <T> T fromJson(String json, TypeReference<T> type) {
		if(json == null || type == null) {
			return null;
		}
		T result = null;
		try {
			result = OBJECT_MAPPER.readValue(json, type);
		} catch (IOException e) {
			log.warn(e.getMessage());
		}
		return result;
	}

	public static String getToken() {
		return getToken(3);
	}

	public static String getToken(int length) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			String randomStr = String.valueOf((char)(getRandomNumber(26) + 65));
			sb.append(randomStr);
		}
		return sb.toString();
	}

	public static List<Integer> getDistributeNumbers(int totalAmount, int totalCount, int minimumAmount) {
		// 주어진 값을 주어진 수로 분배, 분배 최소값 보장
		int remainAmount = totalAmount;
		int bound = totalAmount;
		List<Integer> distributeNumbers = new ArrayList<>();
		for(int i = 0; i < totalCount - 1; i++) {
			bound -= minimumAmount * (totalCount - i);
			int distributeAmount = getRandomNumber(bound) + minimumAmount;
			distributeNumbers.add(distributeAmount);
			remainAmount -= distributeAmount;
			bound = remainAmount;
		}
		// 마지막 분배는 남은 값으로 처리
		distributeNumbers.add(remainAmount);
		return distributeNumbers;
	}

	public static int getRandomNumber(int bound) {
		return random.nextInt(bound);
	}
}
