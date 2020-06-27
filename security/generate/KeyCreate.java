package com.lamp.commons.lang.security.generate;

import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException ;
import java.util.concurrent.ConcurrentHashMap ;

import javax.crypto.KeyGenerator ;
import javax.crypto.SecretKey ;
/**
 * KeyGenerator
 * 	<li>AES 	Key generator for use with the AES algorithm.</li>
 * 	<li>ARCFOUR 	Key generator for use with the ARCFOUR (RC4) algorithm.</li>
 * 	<li>Blowfish 	Key generator for use with the Blowfish algorithm.</li>
 * 	<li>DES 	Key generator for use with the DES algorithm.</li>
 * 	<li>DESede 	Key generator for use with the DESede (triple-DES) algorithm.</li>
 * 	<li>HmacMD5 	Key generator for use with the HmacMD5 algorithm.</li>
 * 	<li>HmacSHA1</li>
 * 	<li>HmacSHA224</li>
 * 	<li>HmacSHA256</li>
 * 	<li>HmacSHA384</li>
 * 	<li>HmacSHA512 	Keys generator for use with the various flavors of the HmacSHA algorithms.</li>
 * 	<li>RC2 	        Key generator for use with the RC2 algorithm.</li>
 * 
 * SecretKeyFactory
 * 	<li>AES 	Constructs secret keys for use with the AES algorithm.</li>
 *  <li>ARCFOUR 	Constructs secret keys for use with the ARCFOUR algorithm.</li>
 *  <li>DES 	Constructs secrets keys for use with the DES algorithm.</li>
 *  <li>DESede 	Constructs secrets keys for use with the DESede (Triple-DES) algorithm.</li>
 *  <li>PBEWith<digest>And<encryption>
 *  <li>PBEWith<prf>And<encryption> 	Secret-key factory for use with PKCS5 password-based encryption, where <digest> is a message digest, <prf> is a pseudo-random function, and <encryption> is an encryption algorithm.
 *							Examples:
 *							PBEWithMD5AndDES (PKCS #5, 1.5),
 *								PBEWithHmacSHA256AndAES_128 (PKCS #5, 2.0)
 *               Note: These all use only the low order 8 bits of each password character.</li>
 *  <li>PBKDF2With<prf> 	PBKDF2With<prf> 	Password-based key-derivation algorithm found in PKCS #5 2.0 using the specified pseudo-random function (<prf>). Example: PBKDF2WithHmacSHA256.</li>
 * @author muqi
 *
 */
public class KeyCreate<T extends SecretKey> {

	
	
	private static final ConcurrentHashMap<String , ConcurrentHashMap<Integer , KeyGenerator> >  KEY_GENERATOR = new ConcurrentHashMap<String, ConcurrentHashMap <Integer,KeyGenerator>>();
	
	private SecretKey  key ;

	private String algorithm;
	
	private int length;
	
	
	public KeyCreate(String algorithm , int length) throws NoSuchAlgorithmException,InvalidParameterException{
		this.algorithm = algorithm;
		this.length    = length;
		createKey( );
	}
	
	public SecretKey getKey() throws NoSuchAlgorithmException{
		return key;
	}
	
	private void createKey() throws NoSuchAlgorithmException , InvalidParameterException{
		if(key == null ){
			 ConcurrentHashMap<Integer , KeyGenerator> keyMap = KEY_GENERATOR.get( algorithm );
			 if( keyMap == null){
				 KEY_GENERATOR.put( algorithm , keyMap = new ConcurrentHashMap<Integer , KeyGenerator>());
			 }
			KeyGenerator keyGenerator = keyMap.get( algorithm );
			if( keyGenerator == null){
					keyGenerator = KeyGenerator.getInstance( algorithm  );
					keyGenerator.init( length );
			}
			key = keyGenerator.generateKey( );
		}
	}
	
}
