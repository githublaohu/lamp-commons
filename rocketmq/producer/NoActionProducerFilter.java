package com.lamp.commons.lang.rocketmq.producer;

import org.apache.rocketmq.client.producer.SendResult;

public class NoActionProducerFilter implements ProducerFilter<Object> {

	@Override
	public boolean before( Object t ) {
		return true;
	}

	@Override
	public boolean after( SendResult sendResult ) {
		return true;
	}

}
