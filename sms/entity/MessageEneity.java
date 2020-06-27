package com.lamp.commons.lang.sms.entity;

import java.util.List;

public class MessageEneity {
	
	/**
	 *短信ID 
	 */
	private Long smsId;
	
	/**
	 * 
	 */
	private Long institutions;
	
	/**
	 * 电话号码
	 */
	private String smsPhone;
	
	/**
	 * 批量电话号码
	 */
	private List<String> phoneList;
	
	/**
	 * 
	 */
	private Integer smsContentId;
	
	/**
	 * 短信内容
	 */
	private String smsContent;
	
	/**
	 * 失败次数
	 */
	private Integer smsFailureCount;
	
	/**
	 * 平台名
	 */
	private String smsPlatformId;
	
	
	
	private Integer smsStatus;
	
	private Integer smsPriority;
	
	private long smsSendTime;
	
	private long smsAcceptTime;
	
	/**
	 * 
	 */
	private long smsFeedback;
	
}
