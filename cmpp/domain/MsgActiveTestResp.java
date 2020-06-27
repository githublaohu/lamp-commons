package com.lamp.commons.lang.cmpp.domain;

import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 *
 * 链路检查消息结构定义
 * Created by Fred on 2016/8/10.
 */
public class MsgActiveTestResp extends MsgResponse {
	
	private static Logger logger= Logger.getLogger(MsgActiveTestResp.class);
	
	private byte reserved;
	
	
	
	public MsgActiveTestResp( ) {
		super( );
	}

	public MsgActiveTestResp(MsgResponse response){
	    super(response);
		byte[] data=response.getBody();

		if(data.length==1){
			ByteArrayInputStream bins=new ByteArrayInputStream(data);
			DataInputStream dins=new DataInputStream(bins);
			try {
				this.reserved=dins.readByte();
				dins.close();
				bins.close();
			} catch (IOException e){}
		}else{
			logger.info("链路检查,解析数据包出错，包长度不一致。长度为:"+data.length);
		}
	}
	
	public void init(ByteBuffer buffer){
		this.reserved= buffer.get( );
	}
	
	public byte getReserved() {
		return reserved;
	}

	public void setReserved(byte reserved) {
		this.reserved = reserved;
	}
}
