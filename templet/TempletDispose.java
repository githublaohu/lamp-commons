package com.lamp.commons.lang.templet;

import java.io.Writer;
import java.util.Map;

public interface TempletDispose {

	
	public String mergeTemplateString(String id , Map<String,Object> context);
	
	public Writer mergeTemplateWriterString(String id , Map<String,Object> context);
	
	public void mergeTemplateWriter( String id , Map<String,Object> context , Writer writer);
}
