package com.lamp.commons.lang.cmpp.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Date;

/**
 * laohu 
 * @author vp
 *
 */
public class SocketConnect {

	
	private String ip;
	
	private int post;
	
	private Socket socket;
	/**
	 * socket调用次数
	 */
	private long useCount;

	/**
	 * 连接时间
	 */
	private long connectTime;

	// socket状态
	// 0:未连接
	// 1：连接中
	// 2：已连接
	private int socketFlag = 0;

	/**
	 * 
	 */
	DataOutputStream out = null;
	/**
	 * 
	 */
	DataInputStream in = null;

	public SocketConnect( String ip , int post ) {
		try {
			this.ip = ip;
			this.post = post;
			info( );
		} catch ( IOException e ) {
			// TODO 自动生成的 catch 块
			e.printStackTrace( );
		}
	}

	public void info( ) throws IOException {
		connectTime = new Date( ).getTime( );
		socket = new Socket( );
		socket.connect( new InetSocketAddress( ip , post ) );
		getDataStream( );
		socketFlag = 1;
	}

	private void getDataStream( ) throws IOException {
		try {
			out = new DataOutputStream( this.socket.getOutputStream( ) );
			in = new DataInputStream( this.socket.getInputStream( ) );
		} catch ( IOException e ) {
			interrupt();
			throw e;
		}
	}

	public boolean write( byte[ ] by ) throws Exception {
		try {
			reconnection();			
			out.write( by );
			return true;
		} catch ( SocketException e ) {
			interrupt();
			throw e;
		}catch (Exception e){
			interrupt();
			throw e;
		}
	}

	public void flush( ) throws IOException {
		out.flush( );
	}

	public int read( byte[ ] by , int index , int length ) throws Exception {
		try {
			return in.read( by ,index ,length);
		} catch ( SocketException e ) {
			interrupt();
			throw e;
		}catch (Exception e){
			interrupt();
			throw e;
		}
	}

	public boolean reconnection(){		
		if( socketFlag == 0){
			IOException ioe = null;
			for(int i = 0 ; i < 10 ; i++){
				try {
					info();
					if( socketFlag == 1){
						return true;
					}
				} catch ( IOException e ) {
					ioe = e;
				}
			}
			if( socketFlag == 0){
				throw new RuntimeException("reconnection ten  " , ioe);
			}
			return false;
		}else{
			return false;
		}
		
	}
	
	private void interrupt( ) {
		socketFlag = 0;
	}

	public String getIp( ) {
		return ip;
	}

	public int getPost( ) {
		return post;
	}

	public Socket getSocket( ) {
		return socket;
	}

	public long getUseCount( ) {
		return useCount;
	}

	public long getConnectTime( ) {
		return connectTime;
	}

	public int getSocketFlag( ) {
		return socketFlag;
	}

	public boolean isSocketFlag(){
		return socketFlag == 1;
	}
	
	@Override
	public String toString( ) {
		return "SocketConnect [ip=" + ip + ", post=" + post + ", useCount=" + useCount + ", connectTime=" + connectTime
				+ ", socketFlag=" + socketFlag + "]";
	}

	
}
