package com.lamp.commons.lang.templet;

import java.util.Map;

import com.lamp.commons.lang.templet.velocity.TempletVeocityStringSource ;

public class TempletConfigure {

	private String filePath;
	
	private String encoding;
	
	private String templetRealization;
	
	private TempletSource templetSource;
	
	public static TempletConfigure create(){
		return new TempletConfigure();
	}
	
	public final TempletConfigure setFilePath(String filePath){
		this.filePath = filePath;
		return this;
	}
	
	public final TempletConfigure setEncoding(String encoding){
		this.encoding = encoding;
		return this;
	}
	
	public final TempletConfigure setTempletRealization(String templetRealization){
		this.templetRealization = templetRealization;
		return this;
	}
	
	public final TempletSource build(){
		
		Map<String,String> map = null;
		
		if(this.filePath != null){
			
		}
		if( this.encoding == null){
			this.encoding = "utf-8";
		}
		this.templetRealization = "velocity-string";
		if(this.templetRealization == null){
			this.templetRealization = "independent-string";
		}
		
		
		if( "independent-string".equals( this.templetRealization)){
			
		}else if( "velocity-string".equals(this.templetRealization)){
			templetSource = new TempletVeocityStringSource();
		}
		
		if(this.filePath == null){
			this.templetSource.init();
		}else{
			this.templetSource.init( map );
		}
		this.templetSource.setEncoding( this.encoding );
		return templetSource;
	}
}
