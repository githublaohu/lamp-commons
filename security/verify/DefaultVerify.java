package com.lamp.commons.lang.security.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lamp.commons.lang.security.generate.GenerateCipher ;

public  class DefaultVerify implements Verify{

	public final static Map<String , Verify> ALGORITHM_VERIFY = new ConcurrentHashMap<>();
	
	private final static String ALGORITHMNAE = "aogorith error ,aogorith in ";
	private final static String WORKINGMODE  = "working mode error , workingmode in ";
	private final static String PADDING = "padding error ,  padding in ";
	private final static String KEYLENGTH = "key length error , keyleng ";
	
	static{
		List<Integer> list = new ArrayList<Integer>();
		list.add(128);
		list.add(192);
		list.add(256);
		new DefaultVerify("AES", list);
		
		new DefaultVerify("ARCFOUR", 40, 1024);
		
		new DefaultVerify("BLOWFISH",32, 448);
		
		List<Integer> list1 = new ArrayList<>();
		list1.add(56);
		new DefaultVerify("DES",list1);
		
		List<Integer> list2 = new ArrayList<>();
		list2.add(112);
		list2.add(168);
		new DefaultVerify("DESede", list2);
		
		new DefaultVerify("RC2", 40, 1024);
	}
	
	private static void setAlgorithmVerify(String algorithm , Verify verify){
		ALGORITHM_VERIFY.put(algorithm, verify);
	}
	
	public static final boolean verifys( String algorithm , int length ){
		Verify verify = ALGORITHM_VERIFY.get(algorithm.split("/")[0]);
		if( verify!= null){
			return verify.verify(algorithm, length);
		}
		throw new RuntimeException();
	}
	
	
    private List<Integer> size ;

    private int between;

    private int and;
    
    private String algorithm;
    
    public DefaultVerify(){}
	  
    public DefaultVerify(String algorithm , List<Integer> size){
    	this(algorithm, size, -1, -1);
    }
    
    public DefaultVerify(String algorithm ,int between, int and){
    	this(algorithm, null, between, and);
    }
    
	public DefaultVerify(String algorithm , List<Integer> size, int between, int and){
		this.size = size;
		this.between = between;
		this.and = and;
		this.algorithm = algorithm;
		setAlgorithmVerify(algorithm, this);
	}
	
	public boolean verify( String algorithm , int length){
		if(algorithm != null){
			String[] strArray = algorithm.split("/");				
		    if( GenerateCipher.SYMMETRIC.contains(strArray[0].toUpperCase())){// || GenerateCipher.ASYMMETRIC.contains(strArray[0].toUpperCase())){
		    	if(strArray.length == 3 ){
		    		if( !GenerateCipher.WORKING_MODE.contains( strArray[1]) ){
		    			//throw new RuntimeException(WORKINGMODE+GenerateCipher.WORKING_MODE.toString());
		    			System.out.println(WORKINGMODE+GenerateCipher.WORKING_MODE.toString());
		    		}
		    		if(!GenerateCipher.PADDING.contains(strArray[2]) ){
		    			//throw new RuntimeException(PADDING+GenerateCipher.PADDING.toString());
		    			System.out.println(PADDING+GenerateCipher.PADDING.toString());
		    		}
		    		
		    	}
		    	if( size !=null){
					if( size.contains(length)){
						return true;
					}
					//throw new RuntimeException(KEYLENGTH + size.toString());
					System.out.println(KEYLENGTH + size.toString());
		    	}else{
					if(length >= between && length <= and ){
						return true;
					}
					//throw new RuntimeException(KEYLENGTH + "length between "+ between +"and "+and);
					System.out.println(KEYLENGTH + "length between "+ between +"and "+and);
		    	}
		    }
		}
	 // throw new RuntimeException(ALGORITHMNAE+GenerateCipher.SYMMETRIC.toString()+","+GenerateCipher.ASYMMETRIC.toString());
		System.out.println(ALGORITHMNAE+GenerateCipher.SYMMETRIC.toString()+","+GenerateCipher.ASYMMETRIC.toString());
		return false;
	}

	public List< Integer > getSize( ) {
		return size;
	}

	public int getBetween( ) {
		return between;
	}

	public int getAnd( ) {
		return and;
	}

	public String getAlgorithm( ) {
		return algorithm;
	}
	
	
}
