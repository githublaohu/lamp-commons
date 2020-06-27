package com.lamp.commons.lang.cmpp.netty;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.rocketmq.remoting.netty.NettyClientConfig;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultEventExecutorGroup;

public class NettyNotificationClient {
	private final Bootstrap bootstrap = new Bootstrap( );
	private final EventLoopGroup eventLoopGroupWorker;
	private DefaultEventExecutorGroup defaultEventExecutorGroup;
	private final NettyClientConfig nettyClientConfig;

	public NettyNotificationClient( ) {
		nettyClientConfig = new NettyClientConfig();
		this.eventLoopGroupWorker = new NioEventLoopGroup( 1 , new ThreadFactory( ) {
			private AtomicInteger threadIndex = new AtomicInteger( 0 );

			@Override
			public Thread newThread( Runnable r ) {
				return new Thread( r ,
						String.format( "NettyClientSelector_%d" , this.threadIndex.incrementAndGet( ) ) );
			}
		} );

		this.defaultEventExecutorGroup = new DefaultEventExecutorGroup(//
	            nettyClientConfig.getClientWorkerThreads(), //
	            new ThreadFactory() {

	                private AtomicInteger threadIndex = new AtomicInteger(0);

	                @Override
	                public Thread newThread(Runnable r) {
	                    return new Thread(r, "NettyClientWorkerThread_" + this.threadIndex.incrementAndGet());
	                }
	            });
		
		
	/*	Bootstrap handler = this.bootstrap.group( this.eventLoopGroupWorker ).channel( NioSocketChannel.class )
				.option( ChannelOption.TCP_NODELAY , true )
				.option( ChannelOption.SO_KEEPALIVE , false )
				.option( ChannelOption.CONNECT_TIMEOUT_MILLIS , nettyClientConfig.getConnectTimeoutMillis( ) )
				.option( ChannelOption.SO_SNDBUF , nettyClientConfig.getClientSocketSndBufSize( ) )
				.option( ChannelOption.SO_RCVBUF , nettyClientConfig.getClientSocketRcvBufSize( ) )
				.handler( new ChannelInitializer< SocketChannel >( ) {
					@Override
					public void initChannel( SocketChannel ch ) throws Exception {
						ch.pipeline( )
						  .addLast( defaultEventExecutorGroup , 
									new NettyEncoder( ) , 
									new NettyDecoder( ) ,
									new IdleStateHandler( 0 , 0 ,nettyClientConfig.getClientChannelMaxIdleTimeSeconds( ) ) ,
								    new NettyConnectManageHandler( ) , 
								    new NettyClientHandler( ) );
					}
				} );
		ChannelFuture cf = this.bootstrap.connect( null );
		cf.channel( ).writeAndFlush( "" );*/

	}
	

	
}
