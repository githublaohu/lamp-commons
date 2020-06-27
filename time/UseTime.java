package com.lamp.commons.lang.time;

import java.util.ArrayList;
import java.util.List;

public class UseTime {

	private TimeTupleLong firstTimeTupleLong;
	
	private TimeTupleLong timeTupleLong;
	
	private List<TimeTupleLong> timeTupleLongList;

	/**
	 * 在一定的环境需要 纳秒。怎么完善
	 */
	public UseTime() {
		this( System.currentTimeMillis());
	}

	public UseTime(long beginTime) {
		this(beginTime , 0L);
	}

	public UseTime(long beginTime ,long endTime) {
		this.timeTupleLong = new TimeTupleLong(beginTime , endTime);
		this.firstTimeTupleLong = this.timeTupleLong;
	}

	
	public long getMillis(){
		checkEndTime();
		return this.timeTupleLong.getEndTime() - this.timeTupleLong.getBeginTime();
	}
	
	/**
	 * 如果需要 年，月怎么办
	 * @return
	 */
	public String getTime(){
		//long time = getMillis();
		//TimeUnit.MILLISECONDS.toNanos(duration)
		return null;
	}
	
	private void isEndTime(){
		if( !checkEndTime()){
			throw new NullPointerException("not is endTime");
		}
	}
	
	private boolean checkEndTime(){
		if( this.timeTupleLong.getEndTime() <= 0){
			this.timeTupleLong.setEndTime( System.currentTimeMillis() );
			return true;
		}
		return false;
	}
	
	/**
	 * 重置，开始时间 从调用 reset开始。
	 */
	public void reset(){
		checkEndTime();
		addTimeTupleLongList();
		this.timeTupleLong = new TimeTupleLong( System.currentTimeMillis() , 0);
	}
	/**
	 * 持续， 开始时间不改变。
	 */
	public void continued(){
		addTimeTupleLongList();
		this.timeTupleLong = new TimeTupleLong( this.timeTupleLong.getBeginTime() , 0);
	}
	
	/**
	 * 连接
	 * 新的开始时间，是上个操作的结束时间
	 */
	public void hinged(){
		addTimeTupleLongList();
		this.timeTupleLong = new TimeTupleLong( this.timeTupleLong.getEndTime() , 0);
	}
	private void addTimeTupleLongList(){
		isEndTime();
		if(this.timeTupleLongList == null){
			this.timeTupleLongList = new ArrayList<>();
		}
		this.timeTupleLongList.add( this.timeTupleLong );
	}
	
}
