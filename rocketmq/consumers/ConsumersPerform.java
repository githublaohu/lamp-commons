package com.lamp.commons.lang.rocketmq.consumers;

import java.util.List;

import com.alibaba.fastjson.TypeReference;

public interface ConsumersPerform<T> {

	
	public Class<T> getType();
	
	public boolean perform(List<T> list);
	
	public void combination(List<T> list);
	
	public TypeReference< List<T> > getTypeReference();
	
	public boolean finish();
	
	public boolean heartbeat();
	
	public boolean failureData();
}
