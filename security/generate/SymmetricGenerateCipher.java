package com.lamp.commons.lang.security.generate;

import java.security.InvalidKeyException ;
import java.security.NoSuchAlgorithmException ;

import javax.crypto.Cipher ;
import javax.crypto.NoSuchPaddingException ;
import javax.crypto.spec.SecretKeySpec ;

public class SymmetricGenerateCipher extends AbstractGenerateCipher{

	public SymmetricGenerateCipher(String algorithm , byte[] keyByte) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
		super( algorithm ) ;
		SecretKeySpec secretKeySpec = new SecretKeySpec(keyByte ,  algorithm  );
		setPublicCipher(   getCipher(Cipher.DECRYPT_MODE , secretKeySpec));		
		setPrivateCipher(  getCipher(Cipher.ENCRYPT_MODE , secretKeySpec));
	}

}
