package com.lamp.commons.lang.templet;


public abstract class AbstractTempleSource implements TempletSource {

	private String encoding;
	
	public void setEncoding(String encoding){
		this.encoding = encoding;
	}
	
	protected String getEncoding(){
		return this.encoding;
	}
}

