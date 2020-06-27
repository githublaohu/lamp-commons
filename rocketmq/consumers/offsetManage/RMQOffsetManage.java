package com.lamp.commons.lang.rocketmq.consumers.offsetManage;

import java.util.List;
import java.util.Set;

import org.apache.rocketmq.common.message.MessageQueue;

import com.lamp.commons.lang.rocketmq.consumers.ConsumersEntity;

public class RMQOffsetManage extends AbstractOffsetManage<Object> {


	@Override
	public void init( ) {
		// TODO 自动生成的方法存根
		
	}
	
	@Override
	public boolean init( Set< MessageQueue > messageQueue ) {

		return false;
	}

	@Override
	public ConsumersEntity get( ) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public int add( ConsumersEntity consumersEntity ) {
		// TODO 自动生成的方法存根
		return 0;
	}

	@Override
	public boolean deleteConsumption( ConsumersEntity consumersEntity ) {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	public List< ConsumersEntity > checkConsumption( ) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public boolean isStop( ) {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	public String getName( ) {
		// TODO 自动生成的方法存根
		return null;
	}



	

}
