package com.lamp.commons.lang.cmpp.utils;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lamp.commons.lang.cmpp.domain.CmppCommand;
import com.lamp.commons.lang.cmpp.domain.CmppMsg;
import com.lamp.commons.lang.cmpp.domain.MsgConnect;
import com.lamp.commons.lang.cmpp.domain.MsgDeliver;
import com.lamp.commons.lang.cmpp.domain.MsgDeliverResp;
import com.lamp.commons.lang.cmpp.domain.MsgHead;
import com.lamp.commons.lang.cmpp.domain.MsgResponse;
import com.lamp.commons.lang.cmpp.domain.MsgSubmit;


/**
 * 文档请看
 * https://wenku.baidu.com/view/6461d1f34693daef5ef73dce.html###
 * laohu
 * @author vp
 *
 */
public class CmppProtocol {
	private static final Logger log = LoggerFactory.getLogger( CmppProtocol.class );

	/**
	 * 单条信息最大长度
	 */
	private static final int singleMsgLength = 140;

	/**
	 * 处理byte流
	 *
	 * @param stream
	 * @param length
	 */
	public static List< MsgResponse > deal( ByteBuffer by , MsgConfig msgConfig) {
		List< MsgResponse > list = new ArrayList<>( );
		try {

			MsgResponse response = null;
			while ( null != ( response = readMsgResponse( by ) ) ) {
				response = dealMsgResponse( response );
				list.add( response );
			}
			return list;
		} catch ( Exception e ) {
			log.error( msgConfig.getServiceId() + "[" + msgConfig.getServiceCode() + "]deal()" + e.getMessage( ) , e );
			return list;

		}
	}

	public static MsgResponse readMsgResponse( ByteBuffer buffer ) {
		
		if ( buffer.limit( ) - buffer.position( ) < 12 ) {
			return null;
		}
		int position = buffer.position( ) , limit = buffer.limit( ) , len ,message = 0;
		try{
			len = buffer.getInt( );
			if(limit - position < len){
				buffer.position( position );
				return null;
			}
			message = buffer.getInt( );
			if( message == 5){
				log.info( "message :  {} " , message );
			}
			CmppCommand cc = CmppCommand.getEnum( message );
			
			MsgResponse response = cc.getMsgResponse(  );
			
			//消息总长度
			response.setTotalLength( len );
			// 消息类型
			response.setCommandId( message );
			response.setCmppCommand( cc );
			//如果是 移动主动请求的。就没有序列号
			if( cc != CmppCommand.CMPP_DELIVER){
				// 序列号
				response.setSequenceId( buffer.getInt( ) );
			}
			response.initOperation( buffer );
			if(buffer.position( ) - position != len){
				buffer.position( position+len );
			}
			return response;
		}catch (Exception e) {
			buffer.position( position ).limit( limit );
			log.error( "message is {} " , message );
			log.error( e.getMessage( ) , e );
			return null;
		}
	}
	
	
	/**
	 * 提取处理结果
	 * 这里要修改。
	 * @return
	 */
	public static MsgResponse readMsgResponse( byte[ ] undoStream ) {

		MsgResponse response = null;
		try {
			// 获取长度
			Integer len = getInt( undoStream , 0 );
			// 未获取长度则返回null
			// 流的长度小于报文长度则返回null

			if ( len < 12 || undoStream.length < len ) {
				return null;
			}
			byte[ ] body = new byte[ len - 12 ];
			// 消息
			response = new MsgResponse( );
			// 长度
			response.setTotalLength( len );
			// 消息Id
			response.setCommandId( getInt( undoStream , 4 ) );
			// 序列号
			response.setSequenceId( getInt( undoStream , 8 ) );
			// 复制body内容
			if ( body.length > 0 ) {
				System.arraycopy( undoStream , 12 , body , 0 , body.length );
			}
			// 未处理的流保存起来
			byte[ ] stream = new byte[ undoStream.length - len ];
			if ( stream.length > 0 ) {
				System.arraycopy( undoStream , len , stream , 0 , stream.length );
			}
			undoStream = stream;
			// body
			response.setBody( body );
		} catch ( Exception e ) {
			log.error( "readMsgResponseError" , e );
		}
		return response;
	}

	/**
	 * 获取整数
	 *
	 * @param stream
	 * @param point
	 * @return
	 */
	public static Integer getInt( byte[ ] stream , int point ) {
		if ( stream.length < point + 4 ) {
			return -1;
		}
		int mask = 0xff;
		int temp = 0;
		int n = 0;
		for ( int i = 0 ; i < 4 ; i++ ) {
			n <<= 8;
			temp = stream[ point + i ] & mask;
			n |= temp;
		}
		return n;
	}

	/**
	 * 把msg内容，生成
	 * @param <MsgConfig>
	 *
	 * @param msg
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static <MsgConfig> CmppMsg getByteMsg( CmppMsg msg , MsgConfig msgConfig ) throws UnsupportedEncodingException {

		if ( msg.getMessage( ).getBytes( "utf-8" ).length < singleMsgLength ) {
			msg = getShortByteMsg( msg , msgConfig );
		} else {
			msg = getLongByteMsg(  msg , msgConfig );
		}
		return msg;
	}

	/**
	 * 组装短短信byte流
	 * @param <MsgConfig>
	 *
	 * @param msg
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private static  CmppMsg getShortByteMsg( CmppMsg msg , MsgConfig msgConfig ) throws UnsupportedEncodingException {
		byte[ ] msgByte = msg.getMessage( ).getBytes( "iso-10646-ucs-2" );
		MsgSubmit submit = new MsgSubmit( );
		submit.setTotalLength( 159 + msgByte.length );
		submit.setCommandId( CmppCommand.CMPP_SUBMIT.getValue( ) );
		submit.setSequenceId( msg.getSequenceId( ) );
		submit.setPkTotal( ( byte ) 0x01 );
		submit.setPkNumber( ( byte ) 0x01 );
		submit.setRegisteredDelivery( ( byte ) 0x00 );
		submit.setMsgLevel( ( byte ) 0x01 );
		submit.setFeeUserType( ( byte ) 0x02 );
		submit.setFeeTerminalId( "" );
		submit.setTpPId( ( byte ) 0x00 );
		submit.setTpUdhi( ( byte ) 0x00 );
		submit.setMsgFmt( ( byte ) 0x08 );
		submit.setMsgSrc( msgConfig.getSpId() );
		submit.setSrcId( msgConfig.getSpCode() );
		submit.setDestTerminalId( msg.getPhone( ) );
		submit.setMsgLength( ( byte ) msgByte.length );
		submit.setMsgContent( msgByte );

		byte[ ] data = submit.toByteArray( );
		msg.setDataArray( new byte[ ][ ] { data } );
		return msg;
	}

	/**
	 * 组装长短信byte流
	 * @param <MsgConfig>
	 *
	 * @param msg
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private static <MsgConfig> CmppMsg getLongByteMsg( CmppMsg msg , MsgConfig msgConfig ) throws UnsupportedEncodingException {
		byte[ ] allByte = msg.getMessage( ).getBytes( "iso-10646-ucs-2" );
		int msgLength = allByte.length;
		int maxLength = singleMsgLength - 6;
		int msgSendCount = msgLength % ( maxLength ) == 0 ? msgLength / ( maxLength ) : msgLength / ( maxLength ) + 1;

		byte[ ][ ] dataArray = new byte[ msgSendCount ][ ];
		// 短信息内容头拼接
		byte[ ] msgHead = new byte[ 6 ];
		Random random = new Random( );
		random.nextBytes( msgHead ); // 为了随机填充msgHead[3]
		msgHead[ 0 ] = 0x05;
		msgHead[ 1 ] = 0x00;
		msgHead[ 2 ] = 0x03;
		msgHead[ 4 ] = ( byte ) msgSendCount;
		for ( int i = 0 ; i < msgSendCount ; i++ ) {
			msgHead[ 5 ] = ( byte ) ( i + 1 );
			byte[ ] needMsg = null;
			// 消息头+消息内容拆分
			if ( i != msgSendCount - 1 ) {
				int start = ( maxLength ) * i;
				int end = ( maxLength ) * ( i + 1 );
				needMsg = CmppUtils.getMsgBytes( allByte , start , end );
			} else {
				int start = ( maxLength ) * i;
				int end = allByte.length;
				needMsg = CmppUtils.getMsgBytes( allByte , start , end );
			}
			int subLength = needMsg.length + msgHead.length;
			byte[ ] sendMsg = new byte[ subLength ];
			System.arraycopy( msgHead , 0 , sendMsg , 0 , 6 );
			System.arraycopy( needMsg , 0 , sendMsg , 6 , needMsg.length );
			MsgSubmit submit = new MsgSubmit( );
			// 12+8+1+1+1+1+10+1+21+1+1+1+6+2+6+17+17+21+1+21+1+8
			submit.setTotalLength( 159 + subLength );
			submit.setCommandId( CmppCommand.CMPP_SUBMIT.getValue( ) );
			submit.setSequenceId( msg.getSequenceId( ) );
			submit.setPkTotal( ( byte ) msgSendCount );
			submit.setPkNumber( ( byte ) ( i + 1 ) );
			submit.setRegisteredDelivery( ( byte ) 0x00 );
			submit.setMsgLevel( ( byte ) 0x01 );
			submit.setFeeUserType( ( byte ) 0x02 );
			submit.setFeeTerminalId( "" );
			submit.setTpPId( ( byte ) 0x00 );
			submit.setTpUdhi( ( byte ) 0x01 );
			submit.setMsgFmt( ( byte ) 0x08 );
			submit.setMsgSrc( cc.getSpId( ) );
			submit.setSrcId( cc.getSpCode( ) );
			submit.setDestTerminalId( msg.getPhone( ) );
			submit.setMsgLength( ( byte ) ( subLength ) );
			submit.setMsgContent( sendMsg );
			dataArray[ i ] = submit.toByteArray( );
		}
		msg.setDataArray( dataArray );
		return msg;
	}

	/**
	 * 连接到cmpp服务器
	 *
	 * @return
	 */
	public static byte[ ] connectISMG( MsgConfig msgConfig ) {
		log.info( "请求连接到ISMG..." );
		MsgConnect connect = new MsgConnect( );
		// 消息总长度，级总字节数:4+4+4(消息头)+6+16+1+4(消息主体)
		connect.setTotalLength( 12 + 6 + 16 + 1 + 4 );
		// 标识创建连接
		connect.setCommandId( CmppCommand.CMPP_CONNECT.getValue( ) );
		// 序列号
		connect.setSequenceId( CmppUtils.getSequence( ) );
		// 设置企业代码
		connect.setSourceAddr( msgConfig.getCompanyCode());
		String timestamp = CmppUtils.getTimestamp( );
		// md5(企业代码+密匙+时间戳)
		connect.setAuthenticatorSource(
				CmppUtils.getAuthenticatorSource( msgConfig.getCompanyCode(), msgConfig.getSecret(), timestamp ) );
		// 时间戳(MMDDHHMMSS)
		connect.setTimestamp( Integer.parseInt( timestamp ) );
		// 版本
		connect.setVersion( ( byte ) 0x20 );

		return connect.toByteArray( );
	}

	/**
	 * 链路检查
	 *
	 * @return
	 */
	public static byte[ ] activityTestISMG( ) {

		MsgHead head = new MsgHead( );
		// 消息总长度，级总字节数:4+4+4(消息头)+6+16+1+4(消息主体)
		head.setTotalLength( 12 );
		// 标识创建连接
		head.setCommandId( CmppCommand.CMPP_ACTIVE_TEST.getValue( ) );
		// 序列，由我们指定
		head.setSequenceId( CmppUtils.getSequence( ) );
		// 发送消息
		return head.toByteArray( );
	}

	/**
	 * 拆除与ISMG的链接
	 *
	 * @return
	 */
	public static byte[ ] cancelISMG( ) {
		MsgHead head = new MsgHead( );
		// 消息总长度，级总字节数:4+4+4(消息头)+6+16+1+4(消息主体)
		head.setTotalLength( 12 );
		// 标识创建连接
		head.setCommandId( CmppCommand.CMPP_TERMINATE.getValue( ) );
		// 序列，由我们指定
		head.setSequenceId( CmppUtils.getSequence( ) );
		// 向socket通道写入数据流
		return head.toByteArray( );
	}

	/**
     * 处理返回结果
     *
     * @param response
     * @return
     */
    private static MsgResponse dealMsgResponse(MsgResponse response) throws IOException {
//        boolean success = false;

        if (null == response) {
            return response;
        }
        CmppCommand cc = response.getCommand();
        if( cc != null){ 
        		try {
					response = cc.getMsgResponse(  );
				} catch ( Exception e ) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
        		if(response instanceof MsgDeliver){
        			MsgDeliver msgDeliver = (MsgDeliver)response;
        			MsgDeliverResp msgDeliverResp = new MsgDeliverResp();
                    msgDeliverResp.setTotalLength(12 + 8 + 4);
                    msgDeliverResp.setCommandId(CmppCommand.CMPP_DELIVER_RESP.getValue());
                    msgDeliverResp.setSequenceId(CmppUtils.getSequence());
                    msgDeliverResp.setMsg_Id(msgDeliver.getMsg_Id());
                    msgDeliverResp.setResult(msgDeliver.getResult());
                    //write(msgDeliverResp.toByteArray());//进行回复
                    msgDeliver.setSuccess(true);
                    response = msgDeliver;
        		}
        	log.info( cc.toString() + ": " +  response.getSequenceId() );	
        }else{
        	
        }
        return response;
    }
    
    static MsgDeliverResp getMsgDeliverResp(MsgDeliver msgDeliver){
    	MsgDeliverResp msgDeliverResp = new MsgDeliverResp();
        msgDeliverResp.setTotalLength(12 + 8 + 4);
        msgDeliverResp.setCommandId(CmppCommand.CMPP_DELIVER_RESP.getValue());
        msgDeliverResp.setSequenceId(CmppUtils.getSequence());
        msgDeliverResp.setMsg_Id(msgDeliver.getMsg_Id());
        msgDeliverResp.setResult(msgDeliver.getResult());
        return msgDeliverResp;
    }

    
}
