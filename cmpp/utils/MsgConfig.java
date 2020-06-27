package com.lamp.commons.lang.cmpp.utils;

import java.util.concurrent.atomic.AtomicInteger;

public class MsgConfig {
	/**
	 * IP
	 */
	private String ismgIp;
	/**
	 * 端口
	 */
	private int ismgPort;
	/**
	 * 企业代码
	 */
	private String companyCode;
	/**
	 * 密钥
	 */
    private String secret;
    
    private String serviceId;
    
    private String serviceCode;
    
    private String SpId;
    
    private String SpCode;
    
    private int PoolSize;
    
    private AtomicInteger SendCount;
    
    private String server;

	public String getIsmgIp() {
		return ismgIp;
	}

	public void setIsmgIp(String ismgIp) {
		this.ismgIp = ismgIp;
	}

	public int getIsmgPort() {
		return ismgPort;
	}

	public void setIsmgPort(int ismgPort) {
		this.ismgPort = ismgPort;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getSpId() {
		return SpId;
	}

	public void setSpId(String spId) {
		SpId = spId;
	}

	public String getSpCode() {
		return SpCode;
	}

	public void setSpCode(String spCode) {
		SpCode = spCode;
	}

	public int getPoolSize() {
		return PoolSize;
	}

	public void setPoolSize(int poolSize) {
		PoolSize = poolSize;
	}

	public AtomicInteger getSendCount() {
		return SendCount;
	}

	public void setSendCount(AtomicInteger sendCount) {
		SendCount = sendCount;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}
	
	
}
