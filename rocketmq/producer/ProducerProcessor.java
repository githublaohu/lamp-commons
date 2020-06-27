package com.lamp.commons.lang.rocketmq.producer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

public class ProducerProcessor implements InvocationHandler{
	
	private static final Logger logger = LoggerFactory.getLogger(ProducerProcessor.class );
	
	private static final  Class<?>[] clazzArray = new Class[0];

	private static final char[] GET_CHAR_ARRAY = "get".toCharArray( );
	
	public static Method getMethod(Class< ? > clazz , String keys) throws NoSuchMethodException, SecurityException{
		return clazz.getMethod( getMethodName( keys ) , clazzArray );
	}
	
	public static final String getMethodName( String key){
		char[] charr = key.toCharArray( );		
		charr[0] = Character.toUpperCase( charr[0]);
		StringBuffer sb = new StringBuffer( 3+ charr.length );
		sb.append( GET_CHAR_ARRAY ).append(  charr );		
		return sb.toString( );		
	}
	
	public static final Object invokeMethod(Object object , Method keysMethod) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		return keysMethod.invoke( object , clazzArray );
	}
	
	private ProducerEntity producerEntity;
	
	private Object object;


	private Class<?> clazz;
	
	private Method keysMethod;

	private Method tagsMethod;
	
	DefaultMQProducer producer;

	public ProducerProcessor(ProducerEntity producerEntity) throws ClassNotFoundException, NoSuchMethodException, SecurityException, MQClientException{
		this.producerEntity = producerEntity;
		init();
	}
	
	
	public void init( ) throws ClassNotFoundException, NoSuchMethodException, SecurityException, MQClientException {
		List<Class<?>> list = new ArrayList<>( );
		if( producerEntity.getClazzName( ) != null && !"".equals( producerEntity.getClazzName( ) )){
			Class< ? > clazz = Class.forName( producerEntity.getClazzName( ) );
			list.add( clazz );			
		}
		List< String > clazzNameList = producerEntity.getClazzNameList( );
		if( clazzNameList != null && !clazzNameList.isEmpty( )){
			for(String clazzName : clazzNameList){
				Class< ? > clazz = Class.forName( clazzName );
				list.add( clazz );
			}
		}
		Class[] clazzs = new Class[list.size( )];
		list.toArray( clazzs );
		object = Proxy.newProxyInstance( ProducerProcessor.class.getClassLoader( ) , clazzs,this);
		getKeysAndTags();
		//initProducer();
		
	}

	public void initProducer() throws MQClientException{
		producer = new DefaultMQProducer( producerEntity.getGroup( ) );
		producer.setNamesrvAddr( producerEntity.getNameServer( ) );
		producer.start( );
	}
	
	public void getKeysAndTags( ) throws ClassNotFoundException, NoSuchMethodException, SecurityException {
		clazz = Class.forName( producerEntity.getEntityClazzName( ) );
		keysMethod = getMethod(clazz , producerEntity.getKeys( ));
		tagsMethod = getMethod(clazz , producerEntity.getTags( ));
	}

	private String getTags(Object object) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		Object o = invokeMethod( object , tagsMethod );
		if( o instanceof String){
			return (String)o;
		}
		return o.toString( );
	};
	
	private String getKeys(Object object) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		Object o = invokeMethod( object , keysMethod );
		if( o instanceof String){
			return (String)o;
		}
		return o.toString( );
	};
	
	@Override
	public Object invoke( Object proxy , Method method , Object[ ] args ) throws Throwable {
		Object object = null;
		if(args.length == 0){
			
		}else if(args.length == 1){
			object = args[ 0 ];
		}else{
			for(Object o : args){
				if ( clazz.equals( o.getClass( ) ) ){
					object = o;
					break;
				}
			}
		}
		if(object != null){
			Message message = new Message(producerEntity.getTopic( ), getTags( object ) , getKeys( object ) , JSON.toJSONString( object ).getBytes( ));
			SendResult sendResult = producer.send( message );
			logger.info("msgId : {} , keys:{}" , sendResult.getMsgId( ) , message.getKeys( ) );
		}
		return null;
	}

	@SuppressWarnings ( "unchecked" )
	public <T>T getObject( ) {
		return (T)object;
	}

	
}
