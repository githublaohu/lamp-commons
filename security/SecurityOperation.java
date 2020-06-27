package com.lamp.commons.lang.security;

import java.security.SignatureException ;

public interface SecurityOperation {

	public byte[] encryption( byte[] data ) throws Exception;
	
	public byte[] decryption(byte[] data) throws Exception;
	
	public int encryptionAfterLength( byte[] data );
	
	public int decryptionAfterLength( byte[] data );
	
	public byte[] sign(byte[] data) throws SignatureException;
	
	boolean verify ( byte[] data , byte[] verifyByte ) throws SignatureException ;

	
	public int signAfterLength( byte[] data );
	
	
	public String getAlgorithm();
	
	public Integer getKeyLength();

}
