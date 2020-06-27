package com.lamp.commons.lang.security.generate ;

import java.security.InvalidKeyException ;
import java.security.Key ;
import java.security.KeyFactory ;
import java.security.NoSuchAlgorithmException ;
import java.security.PrivateKey ;
import java.security.PublicKey ;
import java.security.Signature ;
import java.util.Map ;
import java.util.concurrent.ConcurrentHashMap ;

import javax.crypto.Cipher ;
import javax.crypto.NoSuchPaddingException ;
import javax.crypto.spec.SecretKeySpec ;

public abstract class AbstractGenerateCipher implements GenerateCipher {

	private static final Map< String , KeyFactory > KEY_GENERATOR   = new ConcurrentHashMap< String , KeyFactory >( ) ;

	private static final Map< Key , Cipher >    DECRYPT_CIPHER_MAP   = new ConcurrentHashMap<>( ) ;

	private static final Map< Key , Cipher >    ENCRYPT_CIPHER_MAP   = new ConcurrentHashMap<>( ) ;

	private static final Map< Key , Signature > VERIFY_SIGNATURE_MAP = new ConcurrentHashMap<>( ) ;

	private static final Map< Key , Signature > SIGN_SIGNATURE_MAP   = new ConcurrentHashMap<>( ) ;

	private String algorithm ;

	private String sign;
	
	private Cipher pulicCipher ;

	private Cipher privateCipher ;
	
	private Signature signSignature;
	
	private Signature verifySignature;

	public AbstractGenerateCipher(String algorithm) {
		this( algorithm , null );
	}
	
	public AbstractGenerateCipher(String algorithm ,String sign) {
		this.algorithm = algorithm ;
		this.sign      = sign;
	}

	void setPublicCipher ( Cipher pulicCipher ) {
		this.pulicCipher = pulicCipher ;
	}

	void setPrivateCipher ( Cipher privateCipher ) {
		this.privateCipher = privateCipher ;
	}

	public Cipher getPublicCipher ( ) {
		return this.pulicCipher ;
	}

	public Cipher getPrivateCipher ( ) {
		return this.privateCipher ;
	}
	
	
	
	
	
	
	public void setSignSignature ( Signature signSignature ) {
		this.signSignature = signSignature ;
	}

	public void setVerifySignature ( Signature verifySignature ) {
		this.verifySignature = verifySignature ;
	}

	public Signature getVerify(){
		return this.verifySignature;
	}
	
	public Signature getSign(){
		return this.signSignature;
	}
	
	
	final Cipher getCipher ( int opmode , Key key )throws NoSuchAlgorithmException , NoSuchPaddingException , InvalidKeyException {
		Map< Key , Cipher > map = opmode == Cipher.DECRYPT_MODE ? DECRYPT_CIPHER_MAP : ENCRYPT_CIPHER_MAP ;
		Cipher cipher = map.get( key ) ;
		if ( cipher == null ) {
			cipher = Cipher.getInstance( algorithm ) ;
			cipher.init( opmode , key ) ;
			map.put( key , cipher ) ;
		}
		return cipher ;
	}
	
	
	final Cipher getCipher ( int opmode , SecretKeySpec keySpec )throws NoSuchAlgorithmException , NoSuchPaddingException , InvalidKeyException {
		Map< Key , Cipher > map = opmode == Cipher.DECRYPT_MODE ? DECRYPT_CIPHER_MAP : ENCRYPT_CIPHER_MAP ;
		Cipher cipher = map.get( keySpec ) ;
		if ( cipher == null ) {
			cipher = Cipher.getInstance( algorithm ) ;
			cipher.init( opmode , keySpec ) ;
			map.put( keySpec , cipher ) ;
		}
		return cipher ;
	}
	

	final Signature getSignature ( int opmode , Key key ) throws NoSuchAlgorithmException , InvalidKeyException {
		Map< Key , Signature > map = opmode == GenerateSignature.VERIFY ? VERIFY_SIGNATURE_MAP : SIGN_SIGNATURE_MAP ;
		Signature signature = map.get( key ) ;
		if ( signature == null ) {
			signature = Signature.getInstance( this.sign ) ;
			if ( opmode == GenerateSignature.VERIFY ) {
				signature.initVerify( ( PublicKey ) key ) ;
			} else {
				signature.initSign( ( PrivateKey ) key ) ;
			}
		}
		return signature ;
	}

	final KeyFactory getKeyFactory ( ) throws NoSuchAlgorithmException {
		KeyFactory keyFactory = KEY_GENERATOR.get( this.algorithm ) ;
		if ( keyFactory == null ) {
			keyFactory = KeyFactory.getInstance( algorithm ) ;
			KEY_GENERATOR.put( algorithm , keyFactory ) ;
		}
		return keyFactory ;
	}
}
