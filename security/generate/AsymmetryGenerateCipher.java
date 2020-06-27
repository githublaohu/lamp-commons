package com.lamp.commons.lang.security.generate;

import java.security.InvalidKeyException ;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

/**
 * 
 * 
 * 执行类
 * 操作类
 * 
 * 把所有的 非对称加解密的 算法测试完成
 * @author yuki
 *
 */
public class AsymmetryGenerateCipher extends AbstractGenerateCipher implements GenerateSignature {

	
	public AsymmetryGenerateCipher(String algorithm , byte[] publicKeyByte , byte[] privateKeyByte) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException{
		super( algorithm );
		KeyFactory keyFactory = getKeyFactory();
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKeyByte);
		Key publicKey = keyFactory.generatePublic(x509KeySpec);
		setPublicCipher(  getCipher(Cipher.DECRYPT_MODE , publicKey));

		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKeyByte);  
	    Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec); 
	    setPrivateCipher(  getCipher(Cipher.ENCRYPT_MODE , privateKey) );

	    
	}
	
	public AsymmetryGenerateCipher(String algorithm ,String sign, byte[] publicKeyByte , byte[] privateKeyByte) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException{
		super( algorithm , sign );
		KeyFactory keyFactory = getKeyFactory();
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKeyByte);
		Key publicKey = keyFactory.generatePublic(x509KeySpec);
		setPublicCipher(  getCipher(Cipher.DECRYPT_MODE , publicKey));
		
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKeyByte);  
	    Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec); 
	    setPrivateCipher(  getCipher(Cipher.ENCRYPT_MODE , privateKey) );
	    
	    
	    setSignSignature( getSignature( GenerateSignature.SIGN ,  privateKey ) );
	    setVerifySignature( getSignature( GenerateSignature.VERIFY , publicKey ) );
	}
 
	
	
}
