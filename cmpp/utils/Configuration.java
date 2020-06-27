package com.lamp.commons.lang.cmpp.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Configuration {

	private final Map< String , MsgConfig > msgConfigMap = new ConcurrentHashMap<>( );

	private final Map< String , List<MsgConfig> > msgConfigListMap = new ConcurrentHashMap<>( );
	
	private final MsgConfig msgConfig = new MsgConfig();

	private final List<MsgConfig> msgConfigList = new ArrayList<>( );
	
	private static final Configuration configuration = new Configuration( );

	private Configuration( ) {
		initConfig( );
	}

	public static final Configuration getInstance( ) {
		return configuration;
	}

	private void initConfig( ) {
		// 初始化应用信息
		/*NotifyAppInfo appInfo1 = new NotifyAppInfo( );
		appInfo1.setAppId( 100000058L );
		appInfo1.setSubAppId( 0 );
		appInfo1.setAppName( "岭南优品" );
		appInfo1.setKey( "notify_lnyp" );
		appInfo1.setRemarks( "岭南优品通知服务" );
		appInfo1.setStatus( true );

		notifyAppInfoMap.put( appInfo1.getAppId( ) , appInfo1 );

		// 初始化应用信息
		NotifyAppInfo appInfo2 = new NotifyAppInfo( );
		appInfo2.setAppId( 100000015L );
		appInfo2.setSubAppId( 0 );
		appInfo2.setAppName( "云南移动销售宝" );
		appInfo2.setKey( "notify_ydxsb" );
		appInfo2.setRemarks( "云南移动销售宝通知服务" );
		appInfo2.setStatus( true );
		notifyAppInfoMap.put( appInfo2.getAppId( ) , appInfo2 );

		// 初始化应用信息
		NotifyAppInfo appInfo3 = new NotifyAppInfo( );
		appInfo3.setAppId( 100000015L );
		appInfo3.setSubAppId( 1 );
		appInfo3.setAppName( "比价神器" );
		appInfo3.setKey( "notify_bjsq" );
		appInfo3.setRemarks( "比价神器通知服务" );
		appInfo3.setStatus( true );
		notifyAppInfoMap.put( appInfo3.getAppId( ) , appInfo3 );

		// 初始化Cmpp通道信息
		CmppConfig config = new CmppConfig( );
		Date now = new Date( );
		config.setServer( "120.25.217.128" );
		config.setServiceCode( "cmpp_0" );
		config.setServiceId( "cmpp_gdyd" );
		config.setCnnectCount( 5 );
		config.setCreateTime( now );
		config.setIsmgIp( "221.179.9.61" );
		config.setIsmgPort( 7890 );
		config.setSpCode( "106582676" );
		config.setSpId( "950381" );
		config.setSpSharedSecret( "zjX3-6@R" );
		config.setStatus( true );
		config.setTimeOut( 60000 );
		config.setUpdateTime( now );
		config.setPoolSize( 30 );
		dispose( config );
		
		
		config = new CmppConfig( );
		config.setServer( "120.25.217.128" );
		config.setServiceCode( "cmpp_0" );
		config.setServiceId( "cmpp_ynyd" );
		config.setIsmgIp( "221.179.9.61" );
		config.setSpCode( "106586853" );
		config.setSpId( "950424" );
		config.setSpSharedSecret( "G)N!37eb" );
		config.setIsmgPort( 7890 );
		config.setPoolSize( 15 );
		config.setCnnectCount( 2 );		
		config.setStatus( true );
		config.setTimeOut( 60000 );
		config.setUpdateTime( now );
		config.setCreateTime( now );
		dispose( config );

		config = new CmppConfig( );
		config.setServer( "120.76.235.50" );		
		config.setIsmgIp( "221.179.9.61" );
		config.setServiceCode( "cmpp_1" );
		config.setServiceId( "cmpp_gdyd" );
		config.setIsmgPort( 7890 );
		config.setSpCode( "106582675" );
		config.setSpId( "950378" );
		config.setSpSharedSecret( "Id-@H89s" );		
		config.setPoolSize( 30 );
		config.setCnnectCount( 5 );
		config.setCreateTime( now );
		config.setStatus( true );
		config.setTimeOut( 60000 );
		config.setUpdateTime( now );
		dispose( config );
		
		
		
		config = new CmppConfig( );
		config.setServer( "120.76.235.50" );
		config.setServiceCode( "cmpp_0" );
		config.setServiceId( "cmpp_ynyd" );
		config.setIsmgIp( "221.179.9.61" );
		config.setSpCode( "106586853" );
		config.setSpId( "950424" );
		config.setSpSharedSecret( "G)N!37eb" );
		config.setIsmgPort( 7890 );
		config.setPoolSize( 30 );
		config.setCnnectCount( 2 );		
		config.setStatus( true );
		config.setTimeOut( 60000 );
		config.setUpdateTime( now );
		config.setCreateTime( now );
		dispose( config );
		
		

		config = new CmppConfig( );	
		config.setCnnectCount( 2 );
		config.setCreateTime( now );
		config.setIsmgIp( "120.76.235.50" );
		config.setServiceCode( "cmpp_0" );
		config.setServiceId( "cmpp_ynyd" );
		config.setIsmgPort( 7890 );
		config.setSpCode( "106586853" );
		config.setSpId( "950424" );
		config.setSpSharedSecret( "G)N!37eb" );
		config.setStatus( true );
		config.setTimeOut( 60000 );
		config.setUpdateTime( now );
		config.setPoolSize( 30 );
		dispose( config );
		
	
		cmppConfig.setIsmgIp( "120.76.235.50" );
		cmppConfig.setServiceCode( "cmpp_1" );
		cmppConfig.setServiceId( "cmpp_gdyd" );
		cmppConfig.setIsmgPort( 7890 );
		cmppConfig.setSpCode( "106582675" );
		cmppConfig.setSpId( "950378" );
		cmppConfig.setSpSharedSecret( "Id-@H89s" );		
		cmppConfig.setPoolSize( 15 );
		cmppConfig.setCnnectCount( 5 );
		cmppConfig.setCreateTime( now );
		cmppConfig.setStatus( true );
		cmppConfig.setTimeOut( 60000 );
		cmppConfig.setUpdateTime( now );		
		dispose( cmppConfig );*/
	}

	/**
	 * 把配置分类管理
	 * 一个是 spCode，就是cmpp的服务号
	 * 一个是 ip地址
	 * @param config
	 */
	@SuppressWarnings("unused")
	private void dispose( MsgConfig msgConfig ){
		if( msgConfig.getServer( ) == null){
			msgConfigList.add( msgConfig );
		}else{
			msgConfigMap.put( msgConfig.getSpCode( ) , msgConfig );
	    	List< MsgConfig > list = msgConfigListMap.get( msgConfig.getServer( ) );
	    	if(list == null){
	    		list = new ArrayList<>( ); 
	    		msgConfigListMap.put( msgConfig.getServer( ) , list );
	    	}
	    	list.add( msgConfig );
		}
	}
	
	public MsgConfig getMsgConfig( ) {
		MsgConfig msgConfig = msgConfigMap.get(null );
		if(msgConfig == null){
			msgConfig = this.msgConfig;
		}
		return msgConfig;

	}
	
	public List<MsgConfig>  getCmppConfigToIP(){
		List<MsgConfig>  msgConfig = msgConfigListMap.get(null);
		if(msgConfig == null){
			msgConfig = this.msgConfigList;
		}
		return msgConfig;
	}
	
	public void configurationRefresh(){
		Collection< MsgConfig > ccc = msgConfigMap.values( );
		for(MsgConfig cmppConfig : ccc){
			cmppConfig.getSendCount( ).getAndSet( cmppConfig.getPoolSize( ) );
		}
	}
	
	public String toCmppConfigMapString(){
		return msgConfigMap.toString( );
	}
	
	
	@Override
	public String toString( ) {
		return "Configuration [notifyAppInfoMap=" + "" + ", cmppConfigMap=" + msgConfigMap + "]";
	}
	
	
	
}
