package com.lamp.commons.lang.security.generate;

import java.security.Signature ;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher ;

/*
 * http://docs.oracle.com/javase/8/docs/api/javax/crypto/Cipher.html
 * http://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#Signature
 * 加密模式有很多中。java文档中有多种，
 * 	每个平台至少支持 ecb，cbc
 * 	加密模式很好博客 ：http://blog.csdn.net/fw0124/article/details/8472560
 * 
 * 
 * AlgorithmParameters
 *   请看中文api文档
 */
public interface GenerateCipher {
	
	public static final List<Integer> CIPHER_LENGTH = getCipherLength();
	
	public static final List<String>  WORKING_MODE  = getWorkingMode();
	
	public static final List<String>  PADDING       = getPadding();
	
	public static final List<String>  SYMMETRIC     = getSymmetric();
	
	public static final List<String>  ASYMMETRIC    = getAsymmetry();
	
	
	public static List<Integer> getCipherLength(){
		List<Integer> cipherLength = new ArrayList<>( );
		int base = 32;
		for(int i = 1 ; i< 8 ; i++){
			cipherLength.add( base << i );
		}
		return cipherLength;
	}
	
	public static List<String> getWorkingMode(){
		List<String>  workingMode  = new ArrayList<>( );
		workingMode.add( "NONE" );
		workingMode.add( "CBC" );
		workingMode.add( "CCM" );
		workingMode.add( "CFB" );
		workingMode.add( "CFBx" );
		workingMode.add( "CTR" );
		workingMode.add( "CTS" );
		workingMode.add( "ECB" );
		workingMode.add( "GCM" );
		workingMode.add( "OFB" );
		workingMode.add( "OFBx" );
		workingMode.add( "PCBC" );
		return workingMode;
	}
	
	public static List<String> getPadding(){
		List<String>  padding  = new ArrayList<>( );
		padding.add( "NoPadding" );
		padding.add( "PKCS1Padding" );
		padding.add( "PKCS5Padding" );
		padding.add( "ISO10126Padding" );
		padding.add( "SSL3Padding" );
		padding.add( "OAEPPadding" );
		return padding;
	}
	
	public static List<String> getSymmetric(){
		List<String>  symmetric  = new ArrayList<>( );
		symmetric.add( "AES" );
		symmetric.add( "ARCFOUR" );
		symmetric.add( "BLOWFISH" );
		symmetric.add( "DES" );
		symmetric.add( "DESEDE" );
		//symmetric.add( "HmacMD5" );
		//symmetric.add( "HmacSHA1" );
		//symmetric.add( "HmacSHA224" );
		//symmetric.add( "HmacSHA256" );
		//symmetric.add( "HmacSHA384" );
		//symmetric.add( "HmacSHA512" );
		symmetric.add( "RC2" );
		return symmetric;
	}
	
	
	public static List<String> getAsymmetry(){
		List<String>  asymmetric  = new ArrayList<>( );
		asymmetric.add( "DIFFIEHELLMAN" );
		asymmetric.add( "DSA" );
		asymmetric.add( "RSA" );
		asymmetric.add( "EC" );
		return asymmetric;
	}
	
	public static String getAlgorithm(String algorithm){
		return algorithm.indexOf( '/' ) == -1? algorithm : algorithm.substring( 0 ,  algorithm.indexOf( '/' ) -1 );
	}
	
	public static boolean isSymmetric(String algorithm ){
		if(  SYMMETRIC.contains( algorithm ) ){
			return true;
		}else if( ASYMMETRIC.contains( algorithm )){
			return false;
		}
		throw new RuntimeException( );
	}
	
	
	public Cipher getPublicCipher();
	
	public Cipher getPrivateCipher();
	
	public Signature getVerify();
	
	public Signature getSign();
}
