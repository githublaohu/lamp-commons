package com.lamp.commons.lang.rocketmq.consumers;

public class ConsumersConfiguration {

	private String name;
	
	private String namesrvAddr;
	
	private String consumerGroup;
	
	private String topic;
	
	private int   concurrency;
	
	private int   pullBatchSize;
	
	private String couponReceivePerform;

	private String offsetManage;
	
	
	
	
	public String getName( ) {
		return name;
	}

	public void setName( String name ) {
		this.name = name;
	}

	public String getNamesrvAddr( ) {
		return namesrvAddr;
	}

	public void setNamesrvAddr( String namesrvAddr ) {
		this.namesrvAddr = namesrvAddr;
	}

	public String getConsumerGroup( ) {
		return consumerGroup;
	}

	public void setConsumerGroup( String consumerGroup ) {
		this.consumerGroup = consumerGroup;
	}

	public String getTopic( ) {
		return topic;
	}

	public void setTopic( String topic ) {
		this.topic = topic;
	}

	public int getConcurrency( ) {
		return concurrency;
	}

	public void setConcurrency( int concurrency ) {
		this.concurrency = concurrency;
	}

	public String getCouponReceivePerform( ) {
		return couponReceivePerform;
	}

	public void setCouponReceivePerform( String couponReceivePerform ) {
		this.couponReceivePerform = couponReceivePerform;
	}


	public String getOffsetManage( ) {
		return offsetManage;
	}

	public void setOffsetManage( String offsetManage ) {
		this.offsetManage = offsetManage;
	}

	public int getPullBatchSize( ) {
		return pullBatchSize;
	}

	public void setPullBatchSize( int pullBatchSize ) {
		this.pullBatchSize = pullBatchSize;
	}

	@Override
	public String toString( ) {
		return "ConsumersConfiguration [name=" + name + ", namesrvAddr=" + namesrvAddr + ", consumerGroup="
				+ consumerGroup + ", topic=" + topic + ", concurrency=" + concurrency + ", pullBatchSize="
				+ pullBatchSize + ", couponReceivePerform=" + couponReceivePerform + ", offsetManage=" + offsetManage
				+ "]";
	}
	
	
}
