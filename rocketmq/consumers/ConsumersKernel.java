package com.lamp.commons.lang.rocketmq.consumers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsumersKernel {
	private static final Logger logger = LoggerFactory.getLogger( ConsumersFactory.class );

	private Map< String , ConsumersPerform< Object > > consumersPerformMap = new ConcurrentHashMap<>( );

	private Map< String , OffsetManage > offsetManageMap = new ConcurrentHashMap<>( );;

	private List< ConsumersConfiguration > consumersConfigurationList = new ArrayList<>( );

	private Map< String , ConsumersProcessor > consumersProcessorMap = new ConcurrentHashMap<>( );

	public void init( ) {
		for ( ConsumersConfiguration consumersConfiguration : consumersConfigurationList ) {
			if ( consumersConfiguration.getCouponReceivePerform( ) != null ) {
				if ( consumersPerformMap.containsKey( consumersConfiguration.getCouponReceivePerform( ) ) ) {

				}
			}
			if ( consumersConfiguration.getOffsetManage( ) != null ) {
				if ( offsetManageMap.containsKey( consumersConfiguration.getOffsetManage( ) ) ) {

				}
			}
			ConsumersProcessor consumersProcessor = new ConsumersProcessor(
					consumersPerformMap.get( consumersConfiguration.getCouponReceivePerform( ) ) ,
					consumersConfiguration , offsetManageMap.get( consumersConfiguration.getOffsetManage( ) ) );
			consumersProcessorMap.put( consumersConfiguration.getName( ) , consumersProcessor );
		}
	}

	
	public void setConsumersConfiguration(ConsumersConfiguration consumersConfiguration){
		consumersConfigurationList.add( consumersConfiguration );
	}
	
	public void setConsumersConfiguration(List<ConsumersConfiguration> consumersConfiguration){
		consumersConfigurationList.addAll( consumersConfiguration );
	}
	
	public void setOffsetManage(Map< String , OffsetManage > offsetManageMap){
		this.offsetManageMap.putAll( offsetManageMap );
	}
	
	public void setOffsetManage(OffsetManage offsetManage){
		this.offsetManageMap.put( offsetManage.getName( ) , offsetManage );
	}
	
}
