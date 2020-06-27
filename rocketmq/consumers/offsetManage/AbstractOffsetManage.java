package com.lamp.commons.lang.rocketmq.consumers.offsetManage;

import org.apache.rocketmq.client.consumer.MQPullConsumer;

import com.lamp.commons.lang.rocketmq.consumers.ConsumersConfiguration;
import com.lamp.commons.lang.rocketmq.consumers.OffsetManage;

public abstract class AbstractOffsetManage<T> implements OffsetManage {

	ConsumersConfiguration consumersConfiguration;
	
	MQPullConsumer  consumer;


	public void setMQPullConsumer(MQPullConsumer consumer){
		this.consumer = consumer;
	}
	
	public void setConsumersConfiguration(ConsumersConfiguration consumersConfiguration){
		this.consumersConfiguration = consumersConfiguration;
	}
}