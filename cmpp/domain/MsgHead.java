package com.lamp.commons.lang.cmpp.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * 所有请求的消息头<br/>
 * totalLength 消息总长度<br/>
 * commandId 命令类型<br/>
 * sequenceId 消息流水号,顺序累加,步长为1,循环使用（一对请求和应答消息的流水号必须相同）<br/>
 * Unsigned Integer  	无符号整数<br/>
 * Integer	整数，可为正整数、负整数或零<br/>
 * Octet String	定长字符串，位数不足时，如果左补0则补ASCII表示的零以填充，如果右补0则补二进制的零以表示字符串的结束符
 * Created by Fred on 2016/8/10.
 */
public class MsgHead {
	private static Logger log= LoggerFactory.getLogger(MsgHead.class);
	private int totalLength;//Unsigned Integer 消息总长度
	private int commandId;//Unsigned Integer 命令类型
	private int sequenceId;//Unsigned Integer 消息流水号,顺序累加,步长为1,循环使用（一对请求和应答消息的流水号必须相同）
	private CmppCommand command;
			/**
	 * 命令编码
	 */
	private String commandCode;
	/**
	 * 命令名称
	 */
	private String commandName;
	/**
	 * 命令描述
	 */
	private String commandDesc;

	public byte[] toByteArray(){
		ByteArrayOutputStream bous=new ByteArrayOutputStream();
		DataOutputStream dous=new DataOutputStream(bous);
		try {
			dous.writeInt(this.getTotalLength());
			dous.writeInt(this.getCommandId());
			dous.writeInt(this.getSequenceId());
			dous.close();
		} catch (IOException e) {
			log.error("封装CMPP消息头二进制数组失败。");
		}
		return bous.toByteArray();
	}
	public MsgHead(){
		super();
	}
	public int getTotalLength() {
		return totalLength;
	}
	public void setTotalLength(int totalLength) {
		this.totalLength = totalLength;
	}
	public int getCommandId() {
		return commandId;
	}
	public void setCommandId(int commandId) {
		this.commandId = commandId;
		this.command= CmppCommand.getEnum(commandId);
		this.commandCode=command.getCode();
		this.commandName=command.getName();
		this.commandDesc= command.getDescription();
	}
	public int getSequenceId() {
		return sequenceId;
	}
	public void setSequenceId(int sequenceId) {
		this.sequenceId = sequenceId;
	}

	public CmppCommand getCommand() {
		return command;
	}

	public String getCommandCode() {
		return commandCode;
	}

	public String getCommandName() {
		return commandName;
	}

	public String getCommandDesc() {
		return commandDesc;
	}
}
