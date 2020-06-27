package com.lamp.commons.lang.rocketmq.consumers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="consumers")
public class ConsumersFactory implements ApplicationContextAware{
	
	private static  final Logger  logger = LoggerFactory.getLogger( ConsumersFactory.class );
	
	private ApplicationContext applicationContext;
	
	private Map<String , ConsumersPerform<Object>> consumersPerformMap = new ConcurrentHashMap<>( );
	
	@Autowired
	private Map<String , OffsetManage> offsetManageMap;
	
	private List<String> stringList = new ArrayList<>( );
	
	private List<ConsumersConfiguration> consumersConfigurationList = new ArrayList<>( );
	
	private Map<String , ConsumersProcessor>  consumersProcessorMap = new ConcurrentHashMap<>( );
	
	private ConsumersConfiguration consumersConfiguration = new ConsumersConfiguration( );
	
	@PostConstruct
	public void init(){
		logger.info( "ConsumersFactory init ........" );
		getConsumersPerformSubclass();
		for(ConsumersConfiguration consumersConfiguration : consumersConfigurationList){			
			if(consumersConfiguration.getCouponReceivePerform( ) != null ){
				if ( consumersPerformMap.containsKey( consumersConfiguration.getCouponReceivePerform( ) ) ){
					
				}
			}
			if(consumersConfiguration.getOffsetManage( ) != null ){
				if ( offsetManageMap.containsKey( consumersConfiguration.getOffsetManage( ) ) ){
					
				}
			}
			ConsumersProcessor consumersProcessor = new ConsumersProcessor(  consumersPerformMap.get( consumersConfiguration.getCouponReceivePerform( ) ) , 
																			 consumersConfiguration ,
																			 offsetManageMap.get( consumersConfiguration.getOffsetManage( ) ));
			consumersProcessorMap.put( consumersConfiguration.getName() , consumersProcessor );
		}
	}

	
	
	@SuppressWarnings ( "unchecked" )
	private void getConsumersPerformSubclass(){
		String[ ] strArray = applicationContext.getBeanNamesForType( ConsumersPerform.class );
		for( String str : strArray ){
			consumersPerformMap.put( str,  (ConsumersPerform<Object>)applicationContext.getBean( str ));
		}
	}

	@Override
	public void setApplicationContext( ApplicationContext applicationContext ) throws BeansException {
		this.applicationContext = applicationContext;		
	}



	public List< String > getStringList( ) {
		return stringList;
	}



	public List< ConsumersConfiguration > getConsumersConfigurationList( ) {
		return consumersConfigurationList;
	}



	public ConsumersConfiguration getConsumersConfiguration( ) {
		return consumersConfiguration;
	}



	public Map< String , ConsumersPerform< Object > > getConsumersPerformMap( ) {
		return consumersPerformMap;
	}
	
	
	
	
}
