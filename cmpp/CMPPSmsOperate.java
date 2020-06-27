package com.lamp.commons.lang.cmpp;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lamp.commons.lang.cmpp.utils.CmppProtocol;
import com.lamp.commons.lang.cmpp.utils.MsgConfig;
import com.lamp.commons.lang.cmpp.utils.SocketConnect;
import com.lamp.commons.lang.sms.SmsOperate;
import com.lamp.commons.lang.sms.entity.MessageEneity;

public class CMPPSmsOperate implements SmsOperate {

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
	
	
    public boolean init(MsgConfig config) {
    	this.config = config;
    	this.socketConnect = new SocketConnect( config.getIsmgIp( ) , config.getIsmgPort( ) );
    	this.socketConnectInfo = socketConnect.toString( );
    	link();
    	return true;
    }
    
	@Override
	public boolean send( MessageEneity messageEneity ) {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	public boolean sendList( List<MessageEneity> messageEneity ) {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	public boolean link( ) {
		if( this.write( CmppProtocol.connectISMG( config ) )){
    		this.loginTime = System.currentTimeMillis( );
    		this.loginStatus =1;
	    	return true;
    	}
    	return false;   

	}

	@Override
	public void deliver( ) {
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

	@Override
	public void query( ) {
		// TODO 自动生成的方法存根

	}

	@Override
	public List queryList( ) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public void queryFee( ) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void queryFeeList( ) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void startTime( ) {
		// TODO 自动生成的方法存根

	}
	
	
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
	
	public void checkConnection() throws IOException{
    	if( !socketConnect.isSocketFlag() ){
    		socketConnect.info( );
    		link();
    	}
    }
	
	public boolean heartbeat(){
    	if( this.write( CmppProtocol.activityTestISMG( ) )){
	    	this.heartbeatNum++;
	    	return true;
    	}
    	return false;   	
    }

}
