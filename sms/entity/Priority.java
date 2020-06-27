package com.lamp.commons.lang.sms.entity;

public class Priority {

	private Integer id;
	
	private Long institutions;
	
	private Integer priority;
	
	private Integer delayTime;

	public Integer getId( ) {
		return id;
	}

	public void setId( Integer id ) {
		this.id = id;
	}

	

	public Long getInstitutions( ) {
		return institutions;
	}

	public void setInstitutions( Long institutions ) {
		this.institutions = institutions;
	}

	public Integer getPriority( ) {
		return priority;
	}

	public void setPriority( Integer priority ) {
		this.priority = priority;
	}

	public Integer getDelayTime( ) {
		return delayTime;
	}

	public void setDelayTime( Integer delayTime ) {
		this.delayTime = delayTime;
	}
	
	
	
}
