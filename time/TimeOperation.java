package com.lamp.commons.lang.time;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

public class TimeOperation {

	public static TimeTuple sameHour() {
		return someHour(new Date());
	}
	
	public static TimeTuple someHour(Date date){
		return truncareToCeiling(date , Calendar.HOUR);
	}
	
	public static TimeTuple today() {
		return someday(new Date());
	}

	public static TimeTuple someday(Date date) {
		return truncareToCeiling(date , Calendar.DATE);
	}

	public static TimeTuple sameMonth() {
		return someMonth(new Date());
	}

	public static TimeTuple someMonth(Date date) {
		return truncareToCeiling(date , Calendar.MONTH);
	}

	
	public static TimeTuple truncareToCeiling(Date date , int field){
		return truncareToCeiling(date, field , date , field);
		
	}
	public static TimeTuple truncareToCeiling(Date beginTime , int beginField, Date endTime , int endField ){
		return new TimeTuple(DateUtils.truncate(beginTime, beginField), DateUtils.ceiling(endTime, endField));		
	}
}
