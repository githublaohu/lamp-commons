package com.lamp.commons.lang.time;

public class TimeTupleLong {
	private long beginTime;

	private long endTime;

	
	public TimeTupleLong(long beginTime , long endTime){
		if( beginTime < 0){
			throw new NullPointerException("beginTime can not be less than zero");
		}
		this.beginTime = beginTime;
		this.endTime   = endTime;
	}
	
	public long getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(long beginTime) {
		this.beginTime = beginTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	
	
}
