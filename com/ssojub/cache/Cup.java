package com.ssojub.cache;

import java.util.Calendar;
import java.util.Date;

public class Cup {
	private long accessCount;  // 사용빈도수를 체크
	private String jsonString;
	private Date expiredTime;
	
	public Cup(String key, String jsonString) {
		this.jsonString = jsonString;
		this.accessCount = 0;
		
		Calendar c = Calendar.getInstance();
		//c.add(Calendar.HOUR, 1);
		c.add(Calendar.SECOND, 10);
		this.expiredTime = c.getTime();
	}

	public long getAccessCount() {
		return accessCount;
	}

	public String getJsonString() {
		return jsonString;
	}

	public Date getExpiredTime() {
		return expiredTime;
	}
}
