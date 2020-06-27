package com.lamp.commons.lang.cmpp.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lamp.commons.lang.cmpp.domain.CmppCommand;
import com.lamp.commons.lang.cmpp.domain.CmppMsg;
import com.lamp.commons.lang.cmpp.domain.MsgDeliver;
import com.lamp.commons.lang.cmpp.domain.MsgDeliverResp;
import com.lamp.commons.lang.cmpp.domain.MsgResponse;

/**
 * laohu
 */
public final class CmppSenderSeparate {
    //日志
    private static Logger logger = LoggerFactory.getLogger("cmppSenderSeparate");

    
    private MsgConfig config;
    
    private SocketConnect socketConnect;
    
    private ByteBuffer buffer = ByteBuffer.allocate( 1024<<4 );
    
    private byte[]  by = buffer.array( );
    
    private String socketConnectInfo;
    
    private long heartbeatTime;
    
    private int heartbeatNum;
    
    private long loginTime;
    
    private int  loginStatus = 0;
    
    private long heartbeat = 6*1000;
    
    private long sendCount = 0;
        
    private AtomicInteger sendCalculate = new AtomicInteger( );
    
    private boolean printOnCMPP_SUBMIT_RESP = false;
    
    public boolean init(MsgConfig config) {
    	this.config = config;
    	this.socketConnect = new SocketConnect( config.getIsmgIp( ) , config.getIsmgPort( ) );
    	this.socketConnectInfo = socketConnect.toString( );
    	login();
    	return true;
    }

    public boolean login(){
    	if( this.write( CmppProtocol.connectISMG( config ) )){
    		this.loginTime = System.currentTimeMillis( );
    		this.loginStatus =1;
    		this.get( 0 );
	    	return true;
    	}
    	return false;   	
    	
    }
    
    public void checkConnection() throws IOException{
    	if( !socketConnect.isSocketFlag() ){
    		socketConnect.info( );
    		this.login( );
    	}
    }
    
    public boolean heartbeat(){
    	if( this.write( CmppProtocol.activityTestISMG( ) )){
    		
    		this.get( 0 );
	    	this.heartbeatNum++;
	    	return true;
    	}
    	return false;   	
    }
    
    
    public boolean unInit() {
        return true;
    }

    /**
     * 发送单个数据包
     *
     * @param data
     * @return
     * @throws Exception
     */
    private boolean write(byte[] data) {
        try {
        	checkConnection( );
        	sendCount++;
        	socketConnect.write( data );
        	heartbeatTime = System.currentTimeMillis( );
            return true;
        }  catch (Exception e) {
        	try {
        		checkConnection( );
			} catch ( IOException e1 ) {
				logger.error("[" + config.getServiceCode() + "]"  + e.getMessage( ), e);
			}
        	logger.error("write error  [" + config.getServiceCode() + "]"  + e.getMessage( ), e);
            return false;
        }        
    }

    /**
     * 短信发送
     *
     * @return
     * @throws Exception 
     * @throws IOException
     */
    public boolean send(CmppMsg msg) throws Exception {
    	sendProtocal( msg );
    	
    	sendCalculate.getAndIncrement( );
    	//logger.info("sendCalculate {} " , sendCalculate.get( ) );
    	byte[][] by = msg.getDataArray( );
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	for(byte[]  b : by){
    		baos.write( b );
    	}
    	return this.write( baos.toByteArray( ) );
    }
    
    public void send(List<CmppMsg> msgList) throws Exception {
        for(CmppMsg msg : msgList){
        	send( msg );
        }
    }

    public void sendProtocal(CmppMsg msg) throws UnsupportedEncodingException{
			CmppProtocol.getByteMsg( msg , config );
    }
    
    /**
     * 接收报文
     */
    public List<MsgResponse> get(int size) {
    	
    	List<MsgResponse> msgResponsesList = new ArrayList<>( size );
    	int position , limit,index = 0 ;
    	MsgResponse response;
    	
    	try {
    		
    		int i = 10;
    		for( ; ; ){
				index = this.socketConnect.read( by , buffer.position( ) , buffer.capacity( ) - buffer.position( ) );
				if(index == -1  ){
					if( --i == 0){
						return msgResponsesList;
					}
					Thread.sleep( 10 );
					continue;
				}
				
				buffer.limit( buffer.position( ) + index );
				buffer.position( 0 );
				for( ; ; ){
					response = CmppProtocol.readMsgResponse( buffer );
					if( response != null ){
						if( response.getCommand( ) == CmppCommand.CMPP_DELIVER){
							logger.info( "a user to reply  response is {} " , response.toString( ) );
							MsgDeliverResp mr = CmppProtocol.getMsgDeliverResp( (MsgDeliver)response);
							this.socketConnect.write( mr.toByteArray( ) );
							continue;
						}
						
						if( logger.isDebugEnabled( )){
							if( response.getCommand( ) !=  CmppCommand.CMPP_SUBMIT_RESP && printOnCMPP_SUBMIT_RESP ){
								continue;
							}
							logger.debug( response.toString( ) );
							
						}
						if( response.getCommand( ) ==  CmppCommand.CMPP_SUBMIT_RESP  ){							
							msgResponsesList.add( response );
							size -- ;
						}
					}else{
						break;
					}
					if( size <= 0){
						break;
					}
				}
				
				position = buffer.position( );
				limit    = buffer.limit( );				
				buffer.position( 0 );
				
				if( position != limit){
					buffer.put( by , position , limit - position);
				}
				buffer.position( limit - position );
				buffer.limit( buffer.capacity( ) );
				if( size <= 0){
					return msgResponsesList;
				}
				
    		}
		} catch ( Exception e ) {
			logger.info(" e buffer position is {} , limit is {} , index is {} " ,  buffer.position( ) , buffer.limit( ) , index );
			logger.error( e.getMessage( ) , e );
			return msgResponsesList;
		}    	
    }
    
    public List<MsgResponse> get() {
    	int count = sendCalculate.get( );
    	sendCalculate.addAndGet( -count );
    	return get( count );
    	
    }

    public boolean isSendCalculate(){
    	return sendCalculate.get( ) > 0;
    }
    
    /**
     * 验证当前通道状态
     *
     * @return
     * @throws InterruptedException
     */
    public boolean getSocketStatus(int times) throws InterruptedException {
       return this.socketConnect.getSocketFlag( ) == 1;
    }


    /**
     * 关闭当前socket
     */
    public void close() {
        
    }

    /**
     * 获取当前配置文件
     *
     * @return
     */
    public MsgConfig getConfig() {
        return config;
    }

	public String getSocketConnectInfo( ) {
		return socketConnectInfo;
	}

	public long getHeartbeatTime( ) {
		return heartbeatTime;
	}

	public int getHeartbeatNum( ) {
		return heartbeatNum;
	}

	
	
	
	public long getLoginTime( ) {
		return loginTime;
	}

	public int getLoginStatus( ) {
		return loginStatus;
	}

	
	
	public long getSendCount( ) {
		return sendCount;
	}

	public void heartbeatCheck(){
		try{
			if( System.currentTimeMillis( ) - this.heartbeatTime > heartbeat){
				if(logger.isDebugEnabled( )){
					//logger.debug( "spCode is {}  heartbeatCheck heartbeatTime: {}  currentTime: {}" ,config.getSpCode( ) , heartbeatTime , System.currentTimeMillis( ));
				}
				this.heartbeatTime = System.currentTimeMillis( ) ;
				this.heartbeat( );
			}
		}catch(Exception e){
			logger.error( " heartbeatCheck " + e.getMessage( ) , e );
		}
	}
	
	
	public void sendCountCheck(){	
		long time = System.currentTimeMillis( );
		logger.info( "currentTimeMillis  {}   heartbeatTime{} , " , time ,  this.heartbeatTime , time - this.heartbeatTime > 1000);
		if( time - this.heartbeatTime > 1000 ){
			this.heartbeatTime = time;
			AtomicInteger sendCount = config.getSendCount( );
			sendCount.getAndSet( config.getPoolSize( ) );
		}
	}
	
}
