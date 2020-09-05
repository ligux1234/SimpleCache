package com.ssojub.cache;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;

public class SimpleCache {
	private static ConcurrentMap<String, Cup> dataStore = new ConcurrentHashMap<>();
	
	
	/**
	 * 정보를 테스트 하기 위해서 사용한다. 많은 정보가 담길 경우 큰 I/O가 발생할 수 있다.
	 * @return
	 */
	@Deprecated
	public static String toTestDataString() {
		return dataStore.toString();
	}
	
	
	/**
	 * 고유키값을 생성한다.
	 * @param srchMap
	 * @return
	 */
	private static String genKey(Map<String, String> srchMap) {
		List<String> keyList = srchMap.keySet().stream().sorted().collect(Collectors.toList());
		
		StringBuilder sb = new StringBuilder();
		for (String key : keyList) {
			sb.append("&" + key + "=" + StringUtils.defaultString(srchMap.get(key)));
		}
		
		return sb.toString();
	}
	
	
	/**
	 * 데이터를 가져온다.
	 * @param srchMap
	 * @return
	 */
	public static String getData(Map<String, String> srchMap) {
		String key = genKey(srchMap);
		Cup cup = dataStore.get(key);
		
		if(cup == null) {
			System.out.println("캐쉬정보 없음");
			return null;
		}
		
		Date now = new Date();
		
		if(now.before(cup.getExpiredTime())) {
			System.out.println("캐쉬정보 조회");
			return cup.getJsonString();
		} else {
			System.out.println("오래된 정보");
			dataStore.remove(key);
			return null;
		}
		
	}
	
	
	/**
	 * 데이터를 객체로 변환하여 가져온다.
	 * @param srchMap
	 * @param t
	 * @return
	 */
	public static <T> T getData(Map<String, String> srchMap, Class<T> t) {
		String json = getData(srchMap);
		return new Gson().fromJson(json, t);
	}
	
	
	/**
	 * json 문자열 정보를 저장한다.
	 * @param srchMap
	 * @param jsonString
	 */
	public static void setData(Map<String, String> srchMap, String jsonString) {
		String key = genKey(srchMap);
		Cup cup = new Cup(key, jsonString);
		dataStore.put(key, cup);
	}
	
	
	/**
	 * 객체 정보를 저장한다.
	 * @param srchMap
	 * @param t
	 */
	public static <T> void setData(Map<String, String> srchMap, T t) {
		setData(srchMap, new Gson().toJson(t));
	}
	
	
	/**
	 * 모든 정보 삭제
	 * - 다시 초기화를 시킨다.
	 */
	public static <T> void allClear() {
		System.out.println("allClear");
		dataStore = new ConcurrentHashMap<>();
	}
}
