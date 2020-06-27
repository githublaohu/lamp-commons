package com.lamp.commons.lang.rocketmq.consumers;

import java.util.List;
import java.util.Set;

import org.apache.rocketmq.client.consumer.MQPullConsumer;
import org.apache.rocketmq.common.message.MessageQueue;

public interface OffsetManage {

	void init( ) throws Exception;
	
	public void setMQPullConsumer(MQPullConsumer consumer);
	
	public void setConsumersConfiguration(ConsumersConfiguration consumersConfiguration);
	
	public boolean init(Set< MessageQueue > messageQueue );
	
	public ConsumersEntity get();
		
	public int add(ConsumersEntity consumersEntity);
	
	public boolean deleteConsumption(ConsumersEntity consumersEntity);
	
	public List<ConsumersEntity> checkConsumption();
	
	public boolean isStop();
	
	public String getName();
	
}