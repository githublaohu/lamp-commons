package com.lamp.commons.lang.rocketmq.consumers;

import org.apache.rocketmq.common.message.MessageQueue;

public class ConsumersEntity extends MessageQueue{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6901038833134535766L;

	private long offset;
	
	private long nextOffset;
	
	private String group;
	
	private long time;
	
	public ConsumersEntity(){
		
	}
	
	public ConsumersEntity (MessageQueue messageQueue , String group,long offset){
		super( messageQueue.getTopic( ) , messageQueue.getBrokerName( ) , messageQueue.getQueueId( ) );
		this.offset = offset;
		this.group = group;
	}

	public long getOffset( ) {
		return offset;
	}

	public void setOffset( long offset ) {
		this.offset = offset;
	}

	public String getGroup( ) {
		return group;
	}

	public void setGroup( String group ) {
		this.group = group;
	}

	public long getNextOffset( ) {
		return nextOffset;
	}

	public void setNextOffset( long nextOffset ) {
		this.nextOffset = nextOffset;
	}

	public long getTime( ) {
		return time;
	}

	public void setTime( long time ) {
		this.time = time;
	}
	
	
	
}
