package com.lamp.commons.lang.security.cipherstream;

import java.io.OutputStream;

import javax.crypto.Cipher;

public interface CipherStream {
	
	
	public final static CipherStream BYTE_CIPHER_STREAM = new ByteCipherStream();

	public byte[] getByte(Cipher cipher ,byte[] by , int length  , boolean  subsection) throws Exception ;
	
	public void stream(Cipher cipher ,byte[] by, OutputStream ots , int length , boolean  subsection) throws Exception;
	
	
}
