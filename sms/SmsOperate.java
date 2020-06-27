package com.lamp.commons.lang.sms;

import java.util.List;

import com.lamp.commons.lang.sms.entity.MessageEneity;
/**
 * 短信操作接口
 * @author yuki
 *
 */
public interface SmsOperate {
	
	/**
	 * 链接
	 */
	public boolean link();
	
	
	/**
	 * 心跳
	 */
	public void deliver();
	
	
	/**
	 * 发送
	 */
	public boolean send(MessageEneity messageEneity);
	
	
	public boolean sendList(List<MessageEneity> messageEneity);
	
	
	public void accept();

	
	/**
	 *查询 
	 */
	public void query();
	/**
	 * 查询结果又多条
	 * @return
	 */
	public List queryList();
	
	/**
	 * 费用查询
	 */
	public void queryFee();
	
	/**
	 * 费用查询结果为多条
	 */
	public void queryFeeList();
	
	
	public void startTime();
}
