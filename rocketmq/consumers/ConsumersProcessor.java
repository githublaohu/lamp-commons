package com.lamp.commons.lang.rocketmq.consumers;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.consumer.PullResult;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;


public class ConsumersProcessor implements Runnable{

	private static  final Logger  logger = LoggerFactory.getLogger( ConsumersProcessor.class );
	
	private DefaultMQPullConsumer consumer;
	
	private ConsumersPerform<Object> consumersPerform;
	
	private ConsumersConfiguration consumersConfiguration;

	private OffsetManage offsetManage;
	
	private ExecutorService executors;
	
	public ConsumersProcessor( ConsumersPerform<Object> consumersPerform ,
			ConsumersConfiguration consumersConfiguration ,OffsetManage offsetManage) {
		super( );
		this.consumersPerform = consumersPerform;
		this.consumersConfiguration = consumersConfiguration;
		this.offsetManage = offsetManage;
		init();
	}
	
	private void init( ) {
		try {
			DefaultMQPullConsumer consumer = new DefaultMQPullConsumer( consumersConfiguration.getConsumerGroup( ) );
			this.offsetManage.setMQPullConsumer( consumer );
			this.offsetManage.setConsumersConfiguration( this.consumersConfiguration );
			this.offsetManage.init( );
			consumer.setNamesrvAddr( consumersConfiguration.getNamesrvAddr( ) );
			consumer.setMessageModel( MessageModel.CLUSTERING );			
			consumer.start( );
			Set< MessageQueue > mqs = consumer.fetchSubscribeMessageQueues( consumersConfiguration.getTopic( ) );
			offsetManage.init( mqs );
			this.consumer = consumer;
			logger.info( "consumersConfiguration : " + consumersConfiguration.toString( ) );
			executors = Executors.newFixedThreadPool( consumersConfiguration.getConcurrency( ) );
			for(int i = 0 , j = consumersConfiguration.getConcurrency( ); i < j ; i++ ){
				executors.execute( this );
			}
			
			
		} catch ( Exception e ) {
			logger.error( e.getMessage( ) , e );
		}
	}

	public void run( ) {
		OffsetManage offsetManage = this.offsetManage;
		DefaultMQPullConsumer consumer = this.consumer;
		ConsumersPerform<Object> consumersPerform = this.consumersPerform;
		PullResult pullResult = null;ConsumersEntity consumersEntity;List< MessageExt > msgList;
		ByteBuffer byteBuffer = ByteBuffer.allocate( 1024 << 6 );
		Thread thread = Thread.currentThread( );
		long offset ,tid = thread.getId( ),startTime;
		logger.info( " ----- thread : {}  " , tid );
		while ( true ) {
			try {
				while ( true ) {
					if ( offsetManage.isStop( ) ){
						consumersPerform.finish();
						executors.shutdown( );
					}
					if( (consumersEntity = offsetManage.get( ) ) != null){
						try {
							startTime = System.currentTimeMillis( );
							pullResult = consumer.pullBlockIfNotFound( consumersEntity , null ,offset = consumersEntity.getOffset( ) , 100 );
							consumersEntity.setTime( System.currentTimeMillis( ) );
							consumersEntity.setNextOffset( pullResult.getNextBeginOffset( ) );
							offsetManage.add( consumersEntity );
							msgList = pullResult.getMsgFoundList( );
							if ( offset != pullResult.getNextBeginOffset( ) ) {
								byteBuffer.clear( );
								combination( byteBuffer , msgList );
								boolean boo = consumersPerform.perform( JSON.parseObject(new String(  byteBuffer.array( ) , 0 , byteBuffer.position( )) , consumersPerform.getTypeReference( ) ));
								if( !boo ){
									
								}
								offsetManage.deleteConsumption( consumersEntity );
							}
							logger.info( String.format( "tid : %d  usr time is : %d " , tid , (System.currentTimeMillis( ) -startTime)) );
						} catch ( MQClientException e ) {
							
							e.printStackTrace( );
						} catch ( RemotingException e ) {
							e.printStackTrace( );
						} catch ( MQBrokerException e ) {
							e.printStackTrace( );
						} catch ( InterruptedException e ) {
							e.printStackTrace( );
						}catch ( Exception e ) {
							e.printStackTrace( );
						}
					}
					
					if ( consumersEntity == null ) {
						try {
							this.wait( 100 );
						} catch ( InterruptedException e ) {
							e.printStackTrace( );
						}
					}
					
				}
			} catch ( Exception e ) {
				logger.error( e.getMessage( ) , e );
			}

		}

	}

	private void combination( ByteBuffer byteBuffer , List< MessageExt > msgList ) {
		
		int msgListSize = msgList.size( ) - 1 , i = 0;
		MessageExt me;
		byteBuffer.put( ( byte ) '[' );
		for ( ;; ) {
			me = msgList.get( i++ );
			logger.info( "msgId : {}  keys:{}" , me.getMsgId( ) , me.getKeys( ) );
			byteBuffer.put( me.getBody( ) );
			if ( i >= msgListSize ) {
				i = 0;
				byteBuffer.put( ( byte ) ']' );
				break;
			}
			byteBuffer.put( ( byte ) ',' );
		}
	}
	
	
	
}
