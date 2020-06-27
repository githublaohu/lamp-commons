package com.lamp.commons.lang.templet;

import java.util.Map;

public interface TempletSource {
	public void init();
	
	public void init(Map<String,String> map);
	
	public int addTemplet(String templetId ,  String templetString);
	
	public int upTemplet(String templetId ,  String templetString);
	
	public int deleltTemplet( String tempetId);
	
	public int disableTemplet( String tempetId);
	
	public TempletStringResource queryTemplet(String temetId);
	
	public void setEncoding(String encoding);
}
