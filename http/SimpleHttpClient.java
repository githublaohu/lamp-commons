package com.lamp.commons.lang.http;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;


/**
 * 所有数据默认返回 json
 * 目前不支持文件上传
 * @author vp
 *
 */
public class SimpleHttpClient {
	private Object object;
	
	public SimpleHttpClient(Class clazz){
		object = Proxy.newProxyInstance(clazz.getClassLoader(), 
			     new Class[]{clazz}, 
			     new ProxyHandler( new HttpClient("", PerFormEnum.POSTBODY)));
	}
	
	
	

	static class bulid {
		
	}
}

class HttpClient{
	private static final Map<String,String> defaultRequestProperty = new HashMap<String, String>();
	static{
		defaultRequestProperty.put("User-Agent", "laohu");
		defaultRequestProperty.put("Content-Type", "application/json");
		defaultRequestProperty.put("Accept", "application/json");
	}
	
	
	private Map<String,String> requestProperty = new HashMap<String, String>( defaultRequestProperty );
	
	private HttpRequestSerialization httpRequestSerialization;
	
	private HttpReturnSerialization httpReturnSerialization;
	
	private Perform perform;
	
	private int defaultConnectTimeout = 3000;
	private int defaultReadTimeout    = 3000;
	
	private int connectTimeout = 3000;
	private int readTimeout    = 3000;
	
	private String url;
	
	private PerFormEnum perFormEnum;
	
	private Map<String ,String> parameter;
	
	
	public HttpClient(String url , PerFormEnum perFormEnum){
		this( url , perFormEnum , new HashMap<String, String>());
	}
	
	public HttpClient(String url , PerFormEnum perFormEnum , Map<String ,String> parameter ){
		this( url , perFormEnum , parameter , new DefaultHttpRequestSerialization() , new StringHttpReturnSerialization());
	}
	
	
	public HttpClient(String url , PerFormEnum perFormEnum , Map<String ,String> parameter , HttpRequestSerialization httpRequestSerialization,HttpReturnSerialization httpReturnSerialization){
		if( url == null  || parameter == null){
			throw new NullPointerException();
		}
		this.url = url ;
		this.perFormEnum = perFormEnum;
		this.perform = perFormEnum.getPerform();
		this.perform.setUrl( this.url );
		
		this.parameter = parameter;
		
		if(parameter.containsKey( "connectTimeout")){
			connectTimeout = Integer.getInteger( this.parameter.get("connectTimeout"));
		}else{
			connectTimeout  = defaultConnectTimeout;
		}
		
		if(parameter.containsKey( "readTimeout")){
			readTimeout = Integer.getInteger( this.parameter.get("readTimeout"));
		}else{
			readTimeout = defaultReadTimeout;
		}
		
		
		this.httpRequestSerialization = httpRequestSerialization;
		this.httpReturnSerialization  = httpReturnSerialization;
				
	}
	
	
	public <T>T perform(String uri , Object object) throws Exception{
		String data = null;
		if(object != null){
			data = httpRequestSerialization.serialization( object );
		}
		String url = perform.urlPerform(uri, data);
		HttpURLConnection httpUrlConnection = (HttpURLConnection)new URL(url).openConnection();
		httpUrlConnection.setRequestMethod( perFormEnum.getMethon() );
		setRequestProperty( httpUrlConnection );
		
		perform.perform(httpUrlConnection, data);
		
		
		try{		
			httpUrlConnection.setUseCaches( false );
			
			httpUrlConnection.setReadTimeout( this.readTimeout );
			
			httpUrlConnection.setConnectTimeout( this.connectTimeout );
			
			httpUrlConnection.connect();
		}catch (Exception e) {
			//这里修改代码不
		}
		
		int i = httpUrlConnection.getResponseCode();
		if( i == 200)
			return returnDate( httpUrlConnection );		
		return null;		
	}
	
	
	private void setRequestProperty(HttpURLConnection httpUrlConnection){
		for( Entry<String, String> e : requestProperty.entrySet()){
			httpUrlConnection.setRequestProperty( e.getKey() , e.getValue() );
		}
	}
	
	/**
	 * 可以从请求头里面获得 数据长度，就不用循序了
	 * @param httpUrlConnection
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	private <T>T returnDate(HttpURLConnection httpUrlConnection ) throws Exception{
		try(BufferedInputStream bais = new BufferedInputStream(httpUrlConnection.getInputStream())){
			ByteArrayOutputStream baos  = new ByteArrayOutputStream();
			byte[] contents = new byte[2024];  
	        int byteRead = 0;  
	        while((byteRead = bais.read(contents)) != -1){  
	        	baos.write( contents );
            }  	        
			return (T)httpReturnSerialization.serialization( baos.toByteArray() );
		}
	}	
}


/**
 * 
 * @author vp
 *
 */




class ProxyHandler implements InvocationHandler {

	private HttpClient httpClient;
	
	public ProxyHandler( HttpClient httpClient ){
		this.httpClient = httpClient;
	}
	
	
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		
		
		return httpClient.perform( method.getName() , args[0] );
	}
	
}


/**
 * 
 * @author vp
 *
 */

interface HttpRequestSerialization {
	
	public String serialization(Object o);
}

class DefaultHttpRequestSerialization implements HttpRequestSerialization{

	public String serialization(Object o) {
		
		return null;
	}

	private String mapSerialization(){
		
		return null;
	}
	
	private String entitySerialization(){
		
		return null;
	}
}

/**
 * 
 * @author vp
 *
 */
interface HttpReturnSerialization {

	
	public Object serialization(byte[] byteArray);
}

class StringHttpReturnSerialization implements HttpReturnSerialization {

	public Object serialization(byte[] byteArray) {
	
		return new String( byteArray );
	}

}


/**
 * 
 * @author vp
 *
 */


interface Perform {
	
	static final byte[] separator = new byte[]{13 , 10};
	
	static final byte[] piecewise = new byte[]{13 , 10 , 13 ,10};
	
	
	public void setUrl(String url );

	public String urlPerform(String url, String data);

	public void perform(HttpURLConnection httpUrlConnection, String data) throws Exception;
}


 enum PerFormEnum{
	
	GET("GET" , new GetPerform()),
	POST("POST" , new PostPerform()),
	POSTBODY("POST" , new PostBodyPerform());
	
	private String methon;
	
	private Perform perform;
	
	
	
	PerFormEnum(String methon , Perform perform ){
		this.methon = methon;
		this.perform = perform;
	}

	
	public String getMethon(){
		return methon;
	}
	
	public Perform getPerform(){
		return perform;
	}
}


abstract class PequestPerForm implements Perform {

	String url;
	
	public void setUrl(String url){
		this.url = url;
	}
	
	public String urlPerform(String url, String data) {
		return this.url + url;
	}

	public void perform(HttpURLConnection httpUrlConnection, String data) throws Exception {
		
	}
}

abstract class PostPequestPerForm extends PequestPerForm{
	
	private OutputStream outputStream;
	
	private HttpURLConnection httpUrlConnection;
	
	private int bodyLenth = 0;
	
	void setHttpURLConnection( HttpURLConnection httpUrlConnection ) throws IOException{
		this.httpUrlConnection = httpUrlConnection;
		httpUrlConnection.setDoOutput(true);
		httpUrlConnection.setDoInput(true);
		httpUrlConnection.setUseCaches(false);	
				
		this.outputStream = httpUrlConnection.getOutputStream();
	}
	
	
	void write(byte[] by , int length ) throws IOException{
		bodyLenth = bodyLenth + by.length;
		outputStream.write( by );
	}
	
	void write(byte[] by) throws IOException{
		write( by , by.length);
	}
	
	
	void length(){
		httpUrlConnection.setRequestProperty("Content-length" , ""+bodyLenth);
	}
}



class GetPerform extends PequestPerForm {
	public String urlPerform(String url, String data) {
		return super.urlPerform(url,data)+"?"+data;
	}
}

class PostPerform  extends PostPequestPerForm {

	public void perform(HttpURLConnection httpUrlConnection, String data) throws Exception{
		setHttpURLConnection( httpUrlConnection );
		write( data.getBytes() );

	}
}


class PostBodyPerform extends PostPequestPerForm {
	
	public void perform(HttpURLConnection httpUrlConnection, String data) throws Exception {
		setHttpURLConnection( httpUrlConnection );
		
		write( piecewise , 0 );
		write(data.getBytes());	
	}
}


