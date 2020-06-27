package com.lamp.commons.lang.rocketmq.producer;

import org.apache.rocketmq.client.producer.SendResult;

public interface ProducerFilter<T> {

	boolean before(T t);
	
	boolean after(SendResult sendResult); 
	
}
