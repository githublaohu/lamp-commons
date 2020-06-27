package cn.vpclub.promotion.coupon.provider.consumers.offsetManage;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;

import org.apache.rocketmq.common.message.MessageQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lamp.commons.lang.rocketmq.consumers.ConsumersEntity;

@Component("redisOffsetManage")
public class RedisOffsetManage extends AbstractOffsetManage<RedisConnectionFactory>{

	private static final Logger logger = LoggerFactory.getLogger(RedisOffsetManage.class );
	
	
	public static byte[] hostIp=("id@" +getHostIp()).getBytes( );
	
	static String getHostIp(){
		Enumeration< NetworkInterface > ifaces;
		try {
			ifaces = NetworkInterface.getNetworkInterfaces();
			 while( ifaces.hasMoreElements() )  {  
			      NetworkInterface iface = ifaces.nextElement();  
			      Enumeration<InetAddress> addresses = iface.getInetAddresses();  
			  
			      while( addresses.hasMoreElements() )  {  
			        InetAddress addr = addresses.nextElement();
			        if( addr instanceof Inet4Address && !addr.isLoopbackAddress() )  {  
			        	
			          return addr.getHostAddress( );
			        }  
			      }  
			    }
			 return "127.0.0.1";
		} catch ( SocketException e ) {
			return "127.0.0.1";
		}  
	   
	}
	

	private static char SEGGMEMTATION = '@';
	
	private static byte[] INIT_BYTE= "init".getBytes( );
	
	private static byte[] GET_BYTE = "get".getBytes( );
	
	private static byte[] ADD_BYTE = "add".getBytes( );
	
	@Autowired
	private RedisConnectionFactory rely;
	
	private StringBuffer sb= new StringBuffer( );
	
	private String fileName = "mq.lua";
	
	private byte[]  evalSHA;
	
	private byte[] messageHashName;
		
	private byte[] messageQueueName;
	
	
	@Override
	public void init( ) throws Exception{
		URL url = Thread.currentThread( ).getContextClassLoader( ).getResource( fileName );
		if( url == null ){
			throw new RuntimeException(String.format( "%s file no exists" , fileName ));
		}		
		RedisConnection con = this.rely.getConnection( );
		try( BufferedInputStream bos = new BufferedInputStream( url.openStream( ) );ByteArrayOutputStream baos=  new ByteArrayOutputStream();){
			byte[] by = new byte[8192];
			int i = 0;
			while( ( i = bos.read( by )) > 0 ){
				baos.write( by , 0 , i );
			}
			String sha = con.scriptLoad( baos.toByteArray( ) );
			evalSHA = sha.getBytes( );
			logger.info( String.format( " script load return sha is %s " , sha ));
			con.close( );
		}
	}
	
	public void init(MessageQueue messageQueue){
		sb.append( messageQueue.getTopic( ) ).append( SEGGMEMTATION )
		  .append( consumersConfiguration.getConsumerGroup( ) ).append( SEGGMEMTATION )
		  .append(messageQueue.getBrokerName( )).append( '_' );
		messageHashName  = (sb.toString( )+"hash").getBytes( );
		messageQueueName = (sb.toString( )+"queue").getBytes( );
	}
	
	@Override
	public boolean init( Set< MessageQueue > messageQueue ) {
		init ( messageQueue.iterator( ).next( ) );
		RedisConnection con = this.rely.getConnection( );
		try{
			int i = 0;
			byte[][] byArray = new byte[ 3 + messageQueue.size( )][];
			byArray[ i++ ] = messageHashName;
			byArray[ i++ ] = messageQueueName;
			byArray[ i++ ] = INIT_BYTE;
			for( MessageQueue mq : messageQueue ){
				byArray[ i++ ] = JSON.toJSONBytes( mq  );
			}
			boolean boo = con.evalSha( evalSHA , ReturnType.BOOLEAN , 2 , byArray );
			return boo;
		}catch(Exception e){
			logger.error( e.getMessage( ) , e );
			return false;
		}finally{			
			con.close( );
		}
	}

	@Override
	public ConsumersEntity get( ) {
		RedisConnection con = this.rely.getConnection( );
		try{
			byte[ ] ged = con.evalSha( evalSHA , ReturnType.VALUE , 2 , new byte[][]{messageHashName , messageQueueName , GET_BYTE, Long.valueOf( System.currentTimeMillis( )).toString( ).getBytes( )} );
			if( ged != null && ged.length > 0){
				return JSON.parseObject( ged , ConsumersEntity.class );
			}
			return null;
		}catch(Exception e){
			logger.error( e.getMessage( ) , e );
			return null;
		}finally{			
			con.close( );
		}
		
	}

	@Override
	public int add( ConsumersEntity consumersEntity ) {
		RedisConnection con = this.rely.getConnection( );
		try{
			boolean boo = con.evalSha( evalSHA , ReturnType.BOOLEAN , 2 , new byte[][]{messageHashName , messageQueueName ,ADD_BYTE , JSON.toJSONBytes( consumersEntity )} );
			if( boo ){
				
			}
			return 1;
		}catch(Exception e){
			
			return 0;
		}finally{			
			con.close( );
		}
	}

	@Override
	public boolean deleteConsumption( ConsumersEntity consumersEntity ) {
		return false;
	}

	@Override
	public List< ConsumersEntity > checkConsumption( ) {
		return null;
	}

	@Override
	public boolean isStop( ) {
		RedisConnection con = this.rely.getConnection( );
		boolean boo = con.exists( hostIp );
		con.close( );
		return boo;
	}

	@Override
	public String getName( ) {
		// TODO 自动生成的方法存根
		return null;
	}

	

}
