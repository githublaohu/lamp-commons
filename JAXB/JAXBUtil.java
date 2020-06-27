package com.lamp.commons.lang.JAXB;

import java.io.StringWriter;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class JAXBUtil {

	/** 
     * JavaBean转换成xml 
     * 默认编码UTF-8 
     * @param obj 
     * @param writer 
     * @return  
     */  
    public static String convertToXml(Object obj) {  
        return convertToXml(obj, "UTF-8");  
    }  
  
    /** 
     * JavaBean转换成xml 
     * @param obj 
     * @param encoding  
     * @return  
     */  
    public static String convertToXml(Object obj, String encoding) {  
        String result = null;  
        try {  
            JAXBContext context = JAXBContext.newInstance(obj.getClass());  
            Marshaller marshaller = context.createMarshaller();  
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);  
            marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);  
  
            StringWriter writer = new StringWriter();  
            marshaller.marshal(obj, writer);  
            result = writer.toString();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
  
        return result;  
    }  
	
	
	
	/** 
     * xml转换成JavaBean 
     * @param xml 
     * @param c 
     * @return 
     */  
    @SuppressWarnings("unchecked")  
    public static <T> T converyToJavaBean(String path, Class<T> c) {  
        T t = null;  
        try {  
        	URL url = Thread.currentThread( ).getContextClassLoader( ).getResource( path );
            JAXBContext context = JAXBContext.newInstance(c);  
            Unmarshaller unmarshaller = context.createUnmarshaller();  
            t = (T) unmarshaller.unmarshal( url.openStream( ));  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
  
        return t;  
    }
}
