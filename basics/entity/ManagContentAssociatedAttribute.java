package com.lamp.commons.lang.basics.entity;

import lombok.Data ;
import lombok.EqualsAndHashCode ;

@Data
@EqualsAndHashCode(callSuper=false)
public class ManagContentAssociatedAttribute extends ContentAssociatedAttribute {
	
	private Long   id;
	
	private Long   groupId;
	
	private String instructions;
	/*
	 * 模块
	 * 内容数据
	 * 映射类型：短信，邮件，exel，html，doc等等
	 */
	private Integer type;
	
	private Integer delFlag;
}
