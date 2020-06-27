package com.lamp.commons.lang.email;

import java.util.Properties;

import javax.mail.Session;

import com.lamp.commons.lang.email.gao.entity.SenderParams;

public class MailSeesionFactory {

	private final static Session DEFAULT_SESSION;

	private final static Session DEFAULT_DEBUG_SESSION;

	private final static Session DEFAULT_SSL_SESSION;

	private final static Session DEFAULT_SSL_DEBUG_SESSION;
	
	
	

	static {
		Properties properties = new Properties( );
		properties.setProperty( "mail.transport.protocol" , "smtp" );
		properties.setProperty( "mail.smtp.auth" , "true" );
		DEFAULT_SESSION = Session.getInstance( properties );

		Properties debugProperties = new Properties( properties );
		properties.setProperty( "mail.debug" , "true" );
		DEFAULT_DEBUG_SESSION = Session.getInstance( debugProperties );

		Properties sslProperties = new Properties( properties );
		sslProperties.setProperty( "mail.smtp.socketFactory.class" , "javax.net.ssl.SSLSocketFactory" );
		sslProperties.setProperty( "mail.smtp.socketFactory.fallback" , "false" );

		DEFAULT_SSL_SESSION = Session.getInstance( sslProperties );

		Properties sslDebugProperties = new Properties( sslProperties );
		sslDebugProperties.setProperty( "mail.debug" , "true" );
		DEFAULT_SSL_DEBUG_SESSION = Session.getInstance( sslDebugProperties );
		
		
		
	}

	public static Properties getProperties( SenderParams senderParams ) {
		Properties properties = new Properties( );
		properties.setProperty( "mail.transport.protocol" , "smtp" );
		properties.setProperty( "mail.smtp.auth" , "true" );
		properties.setProperty( "mail.debug" , senderParams.isDebug( ) );
		return properties;
	}

	public static Properties getPropertiesWithSSL( SenderParams senderParams ) {
		Properties props = getProperties( senderParams );
		final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
		props.setProperty( "mail.smtp.socketFactory.class" , SSL_FACTORY );
		props.setProperty( "mail.smtp.socketFactory.fallback" , "false" );
		return props;
	}
}
