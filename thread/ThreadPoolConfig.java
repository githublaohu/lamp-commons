package com.lamp.commons.lang.thread;

import java.lang.reflect.Method;

import lombok.Data;

@Data
public class ThreadPoolConfig {

	private Class<?> clazz;
	
	private Method method;
	
	private Class<?>  parameterClazz;
	
	private String threadPoolName;
	
	private int poolSun;
	
	private String threadName;
	
	private int traffic;
	
	private int timeOut;
	
	/**
	 * 抛弃
	 * 异常
	 * 等待
	 * run
	 */
	private int dealTrafficOut;
	
	
}
