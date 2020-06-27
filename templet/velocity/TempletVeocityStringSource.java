package com.lamp.commons.lang.templet.velocity;

import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.RuntimeSingleton;
import org.apache.velocity.runtime.resource.loader.StringResourceLoader;
import org.apache.velocity.runtime.resource.util.StringResource;
import org.apache.velocity.runtime.resource.util.StringResourceRepository;

import com.lamp.commons.lang.templet.AbstractTempleSource ;
import com.lamp.commons.lang.templet.TempletDispose ;
import com.lamp.commons.lang.templet.TempletStringResource ;
/**
 *  resource.loader = file
	file.resource.loader.description = Velocity File Resource Loader
	file.resource.loader.class = org.apache.velocity.runtime.resource.loader.StringResourceLoader
	file.resource.loader.cache = true 
	file.resource.loader.modificationCheckInterval = 2 
	
	
	#默认 是 UTF-8，可以不用写了。
	repository.encoding= "UTF-8"
	
	#默认是  StringResourceRepositoryImpl 对象  如果要自己实现，那么重写 StringResourceRepository
	#repository.class = “”
	
	
	#使用使用 StringResourceLoader 静态仓库 或者 TempletStringResource使用StringResourceRepository 默认使用
	#默认是 true 使用 StringResourceLoader 静态仓库 如果false 使用 repository.class  的对象
	repository.static= false
	
	#从  RuntimeServices 得到  StringResourceRepository 对象
	#repository.name
 * @author muqi
 *
 */
public class TempletVeocityStringSource extends AbstractTempleSource implements TempletDispose {
	
	private static final Map<String, String> property = new HashMap<String, String>();
	static{
		property.put("resource.loader", "string");
		property.put("string.resource.loader.description", "Velocity File Resource Loader");
		property.put("string.resource.loader.class", "org.apache.velocity.runtime.resource.loader.StringResourceLoader");
		property.put("string.resource.loader.cache", "true");
		property.put("string.resource.loader.modificationCheckInterval", "2");
	}
	private StringResourceRepository srr;
	
	@Override
	public void init() {
		init( property );
	}

	
	@Override
	public void init(Map<String, String> map) {
		for(Map.Entry<String , String > entry : property.entrySet()){
			Velocity.setProperty( entry.getKey(), entry.getValue());
		}
		srr = (StringResourceRepository)RuntimeSingleton.getApplicationAttribute( StringResourceLoader.REPOSITORY_NAME_DEFAULT);
		
	}
	
	@Override
	public int addTemplet(String templetId, String templetString) {
		srr.putStringResource( templetId , templetString);
		return 0;
	}

	@Override
	public int upTemplet(String templetId, String templetString) {
		srr.putStringResource( templetId , templetString);
		return 0;
	}

	@Override
	public int deleltTemplet(String tempetId) {
		srr.removeStringResource( tempetId );
		return 0;
	}

	@Override
	public int disableTemplet(String tempetId) {
		// TODO 自动生成的方法存根
		return 0;
	}

	@Override
	public TempletStringResource queryTemplet(String temetId) {
		StringResource sr = srr.getStringResource(temetId);
		TempletStringResource tsr = new TempletStringResource( sr.getBody() , sr.getEncoding());
		return tsr;
	}


	@Override
	public String mergeTemplateString(String id, Map<String, Object> context) {
		
		return mergeTemplateWriterString(id, context).toString();
	}


	@Override
	public Writer mergeTemplateWriterString(String id, Map<String, Object> context) {
		Writer writer = new StringWriter();
		mergeTemplateWriter(id, context, writer);
		return writer;
	}


	@Override
	public void mergeTemplateWriter(String id, Map<String, Object> context, Writer writer) {
		// TODO 自动生成的方法存根
		VelocityContext vc = new VelocityContext(context);
		Velocity.mergeTemplate( id , getEncoding(), vc, writer);
	}

	

}
