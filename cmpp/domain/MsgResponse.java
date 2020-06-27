package com.lamp.commons.lang.cmpp.domain;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * 发送请求的返回结果 Created by Fred on 2016/8/6.
 */
public class MsgResponse extends MsgHead {

	static final String getString( ByteBuffer buffer , byte[ ] by , int index , int length , String format ) {
		buffer.get( by , index , length );
		try {
			return new String( by , index , length , format );
		} catch ( UnsupportedEncodingException e ) {

			e.printStackTrace( );
			return "";
		}
	}

	private CmppCommand cmppCommand;

	/**
	 * 信息提交结果
	 */
	private boolean success;
	/**
	 * 消息体
	 */
	private byte[ ] body;

	private String name = this.getClass( ).getName( )+"   ";
	
	public MsgResponse( ) {
	}

	public MsgResponse( MsgResponse response ) {
		this.setSequenceId( response.getSequenceId( ) );
		this.setCommandId( response.getCommandId( ) );
		this.setTotalLength( response.getTotalLength( ) );
		this.setBody( response.getBody( ) );
	}

	public void initOperation(ByteBuffer buffer){
		this.init( buffer );
	}
	
	public void init( ByteBuffer buffer ) {
		
	}

	public void setCmppCommand( CmppCommand cmppCommand ) {
		this.cmppCommand = cmppCommand;
	}

	/**
	 * 获取消息体
	 *
	 * @return
	 */
	public byte[ ] getBody( ) {
		return body;
	}

	/**
	 * 设置消息体
	 *
	 * @param body
	 */
	public void setBody( byte[ ] body ) {
		this.body = body;
	}

	/**
	 * 获取信息提交结果
	 * 
	 * @return
	 */
	public boolean isSuccess( ) {
		return success;
	}

	/**
	 * 设置信息提交结果
	 * 
	 * @param success
	 */
	public void setSuccess( boolean success ) {
		this.success = success;
	}

	protected StringBuffer content( ) {
		StringBuffer sb = new StringBuffer( );
		if ( cmppCommand == null ) {
			sb.append( "无法解析IMSP返回的包结构：包长度为:" ).append( this.getTotalLength( ) );
		} else {
			sb.append( name )
			.append( cmppCommand.getName( ) )
			.append( "  " )
			.append( cmppCommand.getValue( ) )
			.append( "  success is : " )
			.append( this.success )
			.append( "  sequenceId is : " )
			.append( this.getSequenceId( ) );
		}
		return sb;
	}

	@Override
	public String toString( ) {
		return content( ).toString( );
	}

}
