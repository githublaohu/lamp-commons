package com.lamp.commons.lang.cmpp;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.PostConstruct;

import org.apache.rocketmq.common.ThreadFactoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.TypeReference;

import cn.vpclub.notification.api.domain.Notification;
import cn.vpclub.notification.api.domain.NotifyAppInfo;
import cn.vpclub.notification.api.domain.cmpp.CmppConfig;
import cn.vpclub.notification.cmpp.domain.CmppMsg;
import cn.vpclub.notification.cmpp.domain.MsgResponse;
import cn.vpclub.notification.cmpp.utils.CmppSenderSeparate;
import cn.vpclub.notification.cmpp.utils.Configuration;
import cn.vpclub.notification.consumers.ConsumersPerform;
import cn.vpclub.notification.storage.mapper.NotificationMapper;


public class NotificationPerform implements ConsumersPerform<Notification> {

	
	private static  final Logger  logger = LoggerFactory.getLogger( NotificationPerform.class );
	

	private static final ThreadLocal< CmppSenderSeparate >   CSS_LOCAL = new ThreadLocal<>();

	
	
	private Configuration configuration = Configuration.getInstance( );
	
	
	
	private AtomicLong sequenceId = new AtomicLong(1000);
	
	private AtomicBoolean  dormancyBoo = new AtomicBoolean();
	
	@Autowired
	private NotificationMapper notificationMapper;
	
	CountDownLatch countDownLatch = new CountDownLatch( 10 );
	
	ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(new ThreadFactoryImpl(
	        "ConfigurationRefresh"));
	
	private ConfigurationRefresh configurationRefresh = new ConfigurationRefresh( );
	
	@Override
	public Class< Notification > getType( ) {
		return Notification.class;
	}


	@PostConstruct
	public void init(){
		//scheduledExecutorService.scheduleAtFixedRate(configurationRefresh , 50 , 1000 , TimeUnit.MILLISECONDS);
	}
	
	
	
	@Override
	public boolean perform( List< Notification > list ) {
		CmppSenderSeparate scc = getCmppSenderSeparate();
		try {
			int count = 0 , size = list.size( ) ;
			LinkedList<CmppMsg>  cmppMsgList = getCmppMsgList( list );
			AtomicInteger sendCount = scc.getConfig( ).getSendCount( );
			long startTime = System.currentTimeMillis( );
			for( ; ; ){
				count = sendCount.getAndAdd( -size );
				if( count >= size ){
					scc.send( cmppMsgList );
					logger.info( "send time : {}" , System.currentTimeMillis( )  - startTime );
					List< MsgResponse > msg = scc.get( list.size( ) );
					//logger.info("data is :{}", list.toString( ) );
					//logger.info("msg is :{}", msg.toString( ) );
					logger.info( "get time : {}" , System.currentTimeMillis( )  - startTime );
					return true;
				}else if( count < 0 ){
					dormancyBoo.compareAndSet( false , true );
					logger.info( "enter the await , time is {} " , System.currentTimeMillis( ) );
					dormancy( false );
					logger.info( "leave the await , time is {}"  , System.currentTimeMillis( ) );
				}else {
					size = size - count;
					for( ; ; ){
						scc.send( cmppMsgList.remove( ) );
						if( count-- == 1  ){
							break;
						}
					}
				}				
			}
			
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		return false;
	}

	private synchronized void dormancy(boolean boo){
		if( boo ){
			logger.info( " dormancy notifyAll ");
			this.notifyAll( );
		}else{
			try {
				this.wait( );
			} catch ( InterruptedException e ) {
				logger.error( e.getMessage( ) ,e  );
			}
		}
	}

	@Override
	public TypeReference< List< Notification > > getTypeReference( ) {
		return  new TypeReference<List<Notification>>() {};
	}


	@Override
	public void combination( List< Notification > list ) {
		
	}
	
	
	

	@Override
	public boolean finish( ) {
		
		return false;
	}

	private LinkedList<CmppMsg> getCmppMsgList(List< Notification > list){
		LinkedList<CmppMsg> cmppMsgList = new LinkedList<>( );
		CmppMsg cmppMsg;
		Date date = new Date();
		for(Notification notification : list){
			notification.setId( sequenceId.incrementAndGet( ) );
			
			NotifyAppInfo app = configuration.getNotifyAppInfo( notification.getAppId( ) );
			cmppMsg = new CmppMsg();
			cmppMsg.setPhone(notification.getUserAccount());
			cmppMsg.setMessage("【" + app.getAppName() + "】" + notification.getNoteInfo());
			cmppMsg.setPriority(notification.getPriority());
			cmppMsg.setSequenceId( notification.getId( ).intValue( ) );
			notification.setSendTime( date.toString( ) );
			cmppMsgList.add( cmppMsg );
		}
		return cmppMsgList;
	}
	
	
	private CmppSenderSeparate getCmppSenderSeparate(){
		CmppSenderSeparate scc = CSS_LOCAL.get( );
		if(scc == null){
			scc = new  CmppSenderSeparate();
			scc.init( configuration.getCmppConfig( ) );
			CSS_LOCAL.set( scc );
		}
		return scc;
	}

	
	class ConfigurationRefresh implements Runnable{
		@Override
		public void run( ) {
	
			CmppConfig cmppConfig = NotificationPerform.this.configuration.getCmppConfig( );
			if(cmppConfig.getSendCount( ).get( ) != cmppConfig.getPoolSize( )){
				logger.info( "the refresh before cmppConfig {} " , cmppConfig.getSendCount( ).get( ) );
				cmppConfig.getSendCount( ).getAndSet( cmppConfig.getPoolSize( ) );
				logger.info( "the refresh after  cmppConfig {} " , cmppConfig.getSendCount( ).get( ) );
				
			}
			
			if( dormancyBoo.compareAndSet( true	 , false ) ){
				NotificationPerform.this.dormancy( true );
			}
		}
		
		public void refresh(){
			NotificationPerform.this.configuration.configurationRefresh( );
			
		}
		
	}


	@Override
	public void noData( ) {
		getCmppSenderSeparate().heartbeatCheck( );
		
	}
	
}
