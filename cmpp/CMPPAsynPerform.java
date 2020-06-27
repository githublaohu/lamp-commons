package com.lamp.commons.lang.cmpp;

import java.util.ArrayList ;
import java.util.LinkedList ;
import java.util.List ;
import java.util.concurrent.Executors ;
import java.util.concurrent.ScheduledExecutorService ;
import java.util.concurrent.ThreadFactory ;
import java.util.concurrent.TimeUnit ;
import java.util.concurrent.atomic.AtomicBoolean ;
import java.util.concurrent.atomic.AtomicInteger ;


public class CMPPAsynPerform {

	
	/**
	 * 用于唤醒线程
	 */
	private AtomicBoolean  dormancyBoo = new AtomicBoolean();
	
	/**
	 *  初始化锁
	 */
	private  AtomicBoolean isInitCSS = new AtomicBoolean();
	
	ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(new ThreadFactory( ) {
		
		@Override
		public Thread newThread ( Runnable r ) {
			return new Thread( r , "ConfigurationRefresh" ) ;
		}
	});
	
	
	private ConfigurationRefresh configurationRefresh = new ConfigurationRefresh( );
	
	
	public void init(){
		long start     = System.currentTimeMillis( );
		long sleepTime = 1000 - start%1000;
		logger.info( "scheduledExecutorService init configurationRefresh initialDelay {}   startTime {}" ,sleepTime  ,start);
		scheduledExecutorService.scheduleAtFixedRate(configurationRefresh , sleepTime , 1000 , TimeUnit.MILLISECONDS);
	}
	
	
	public boolean perform( List< Notification > list ) {
		CmppSenderSeparate css = getCmppSenderSeparate();
		long startTime = System.currentTimeMillis( );
		int cssSize = this.cssSize ,count = 0 , size = list.size( );
		try {
			insertBatch( list );
			List< Notification > failure = new ArrayList<>( );
			LinkedList<CmppMsg>  cmppMsgList = getCmppMsgList( list );
			AtomicInteger sendCount;
			for( ; ; ){
				sendCount = css.getConfig( ).getSendCount( );
				count = sendCount.getAndAdd( -size );
				//logger.info( "count : {}  size {}  sendCount : {} " , count , size , sendCount );
				if( count >= size ){
					send( cmppMsgList , css , cmppMsgList.size( ) , failure);
					break;
				}else if( count <= 0 ){
					sendCount.getAndAdd( size );
					setCmppSenderSeparate( css );
					cssSize = sleep( startTime , cssSize );
					css = getCmppSenderSeparate( );
				}else {
					size = size - count;
					send( cmppMsgList , css , count  , failure);
					css = getAndSetCmppSenderSeparate( css );
					cssSize--;
				}				
			}
			if( !failure.isEmpty( ) ){
				notificationMapper.sendFailure( failure );
				logger.warn( "the failure  Notification is " ,  failure.toString( ));
			}
			return true;
		} catch ( Exception e ) {
			logger.error( e.getMessage( ) ,e );
		}finally{
			logger.info( "send time : {}" , System.currentTimeMillis( )  - startTime );
			setCmppSenderSeparate( css );
		}
		return false;
	}
	
	private int sleep( long startTime , int cssSize ) throws InterruptedException{
		if( cssSize <= 1 ){
			dormancyBoo.compareAndSet( false , true );
			logger.info( "enter the await , time is {} " , System.currentTimeMillis( ) );
			dormancy( false );
			logger.info( "leave the await , time is {}"  , System.currentTimeMillis( ) );
			return this.cssSize;
		}
		return cssSize;
	}
	
	private void send( LinkedList<CmppMsg>  cmppMsgList ,  CmppSenderSeparate css , int count ,List< Notification > failure) throws Exception{
		LinkedList<CmppMsg> newCmppMsgList = new LinkedList<>( );
		int size  = cmppMsgList.size( );
		int newCount = count;
		CmppMsg cmppMsg;
		try{
			if(logger.isDebugEnabled( )){
				//logger.debug( "css sendCount : {} ,  spCode : {} , send count : {}" ,css.getConfig( ).getSendCount( ).get( ) , css.getConfig( ).getSpCode( ) , count );
			}
			for( ; ; ){
				cmppMsg = cmppMsgList.remove( );
				NOTIFICATION_ID_MAP.put( cmppMsg.getNotification( ).getId( ) , cmppMsg.getNotification( ) );
				boolean isSucceed = css.send( cmppMsg );
				if(logger.isDebugEnabled( )){
					logger.debug( "sequenceId : {} , phone : {}  , isSucceed : {} "  , cmppMsg.getSequenceId( ) ,cmppMsg.getPhone( ) ,isSucceed);
				}
				if( !isSucceed ){					
					if( cmppMsg.getNotification( ).getSendCount( ) > 0){
						logger.error( "send failure : {}" ,  cmppMsg.getNotification( ));
						cmppMsg.getNotification( ).setStatus( 901 );
						cmppMsg.getNotification( ).sendCount();
						heapConsumersProcessor.add( cmppMsg.getNotification( ) );
						failure.add( cmppMsg.getNotification( )  );						
						if(!newCmppMsgList.isEmpty( )){
							addNotificationAsynEntity( newCmppMsgList , css );
							newCmppMsgList = new LinkedList<>( );
						}
						NOTIFICATION_ID_MAP.remove( cmppMsg.getNotification( ).getId( ) );
					}else{
						logger.error( "To failure number : {}", cmppMsg.getNotification( ) );
					}
				}else{					
					newCmppMsgList.add( cmppMsg );
				}
				if( count-- == 1  ){				
					break;
				}
			}
		}catch(Exception e){
			logger.error( "Exception cmppMsgList size is {}  count is{}  newCount is{} " ,size ,count ,  newCount );
			logger.error( e.getMessage( ) , e );
		}finally{
			addNotificationAsynEntity( newCmppMsgList , css );
		}
	}
	
	
	private CmppSenderSeparate getCmppSenderSeparate(){
		if( !isInitCSS.get( ) ){
			synchronized( isInitCSS ){
				List< CmppConfig > cmppConfigList = this.cmppConfigList;
				for(CmppConfig cmppConfig : cmppConfigList){
					CmppSenderSeparate css = new  CmppSenderSeparate();
					css.init( cmppConfig );
					cssList.add( css );
					cssGetDataList.add( css );
					cssSize++;
					logger.info( cmppConfig.toString( ) );
				}
				
				heapConsumersProcessor = ( HeapConsumersProcessor ) consumersFactory.getConsumersProcessor( "couponCodeReceive" );
				Thread thread = new Thread( new  AsysnRead() , "asysnRead");
				thread.start( );
				isInitCSS.set( true );
			}
		}				
		CmppSenderSeparate css = cssList.poll( );
		css.heartbeatCheck( );
		return css;
	}
	
	
	private void setCmppSenderSeparate(CmppSenderSeparate css){
		cssList.add( css );
	}

	private CmppSenderSeparate getAndSetCmppSenderSeparate(CmppSenderSeparate css){
		setCmppSenderSeparate( css );
		return getCmppSenderSeparate();
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

	class AsysnRead implements Runnable{

		@Override
		public void run( ) {
			
			logger.info( "AsysnRead is run ......  toot toot toot toot.... start word " );
			dataLoad();
			
			
		}
		
		void  dataLoad(){
			LinkedList< MsgResponse > msgResponseList = new LinkedList<>( );
			LinkedList<Notification> success  = new LinkedList< Notification >();
			LinkedList<Notification> failure  = new LinkedList< Notification >();
			Notification notification;
			while( true ){
				try{
					if( !isInitCSS.get( ) ){
						Thread.sleep( 100 );
					}
					while(true){
						for( CmppSenderSeparate css  : cssGetDataList ){
							if( css.isSendCalculate( )){
								msgResponseList.addAll( css.get( ) );
							}
						}
						if( !msgResponseList.isEmpty( ) ){
							for(MsgResponse  msgResponse : msgResponseList){
								notification = NOTIFICATION_ID_MAP.remove(  Long.valueOf( msgResponse.getSequenceId( )));
								if( notification != null){
									if( msgResponse.isSuccess( ) ){
										success.add( notification );
									}else{
										if(notification.getSendCount( ) <= 0){
											
											logger.error( "To failure number : {} " , notification );
											continue;
										}
										logger.error( "To failure  msgResponse : {}", msgResponse );
										notification.sendCount( );
										failure.add( notification );
										heapConsumersProcessor.add( notification );
									}
								}else{
									logger.error( "SMS send information not found  sequenceId is : {}" , msgResponse.getSequenceId( ) );
								}
							}
							if( !success.isEmpty( )){
								notificationMapper.responseSuccessful( success );
								success.clear( );
							}
							
							if( !failure.isEmpty( )){
								notificationMapper.responseFailure( failure );
								failure.clear( );
							}
							msgResponseList.clear( );
						}else{
							Thread.sleep( 100 );
						}
					}
				}catch(Exception e){
					logger.error( e.getMessage( ) , e );
				}
			}
		}
		
		private void identify(List< MsgResponse > msgResponseList ,LinkedList< CmppMsg > cmppMsgList , CmppSenderSeparate css ){
			List<Object> success  = new ArrayList< Object >();
			List<Object> failure  = new ArrayList< Object >();
			Object notification;
			MsgResponse msgResponse;
			for(int i = 0 ; i < msgResponseList.size( ) ; i++){
				msgResponse  = msgResponseList.get( i );
				notification = cmppMsgList.get( i ).getNotification( );
				logger.debug( "msgResponse sequenceId is {} , notification is {} " , msgResponse.getSequenceId( ) , notification.getId( ) );
				if( msgResponse.isSuccess( ) ){
					success.add( notification );
				}else{
					if(notification.getSendCount( ) <= 0){
						
						//logger.error( "css spId:{}  To failure number : {} ",css.getConfig( ).getSpCode( ) , notification );
						continue;
					}
					//logger.error( "css spId:{}   To failure  msgResponse : {}",css.getConfig( ).getSpCode( ), msgResponse );
					notification.sendCount( );
					failure.add( notification );
					//heapConsumersProcessor.add( notification );
				}
			}
			if( !success.isEmpty( )){
				notificationMapper.responseSuccessful( success );
			}
			
			if( !failure.isEmpty( )){
				notificationMapper.responseFailure( failure );
			}
			
		}
		
	}
	class ConfigurationRefresh implements Runnable{
		@Override
		public void run( ) {	
			for(CmppConfig cmppConfig : cmppConfigList){
				int sendCount = cmppConfig.getSendCount( ).get( ) ;
				if( sendCount != cmppConfig.getPoolSize( )){
					cmppConfig.getSendCount( ).getAndSet( cmppConfig.getPoolSize( ) );
					logger.info( "the refresh before cmppConfig {}  after  cmppConfig {} " ,sendCount, cmppConfig.getSendCount( ).get( ) );
					
				}
			}
			
			if( dormancyBoo.compareAndSet( true	 , false ) ){
				dormancy( true );
			}
		}
		
	}
}
