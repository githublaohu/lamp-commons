package com.lamp.commons.lang.time;

import java.util.Date;

/**
 * @author muqi
 *
 */
public class TimeTuple<T> {

	private T beginTime;
	
	private T endTime;
	
	public TimeTuple(T beginTime , T endTime){
		this.beginTime = beginTime;
		this.endTime   = endTime;
	}

	public T getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(T beginTime) {
		this.beginTime = beginTime;
	}

	public T getEndTime() {
		return endTime;
	}

	public void setEndTime(T endTime) {
		this.endTime = endTime;
	}

	@Override
	public String toString() {
		return "TimeTuple [beginTime=" + beginTime + ", endTime=" + endTime + "]";
	}
	
	
	
}
