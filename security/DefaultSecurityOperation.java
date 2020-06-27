package com.lamp.commons.lang.security;

import java.security.InvalidKeyException ;
import java.security.NoSuchAlgorithmException ;
import java.security.Signature ;
import java.security.SignatureException ;
import java.security.spec.InvalidKeySpecException ;

import javax.crypto.NoSuchPaddingException ;

import com.lamp.commons.lang.security.cipherstream.CipherStream ;
import com.lamp.commons.lang.security.generate.AsymmetryGenerateCipher ;
import com.lamp.commons.lang.security.generate.GenerateCipher ;
import com.lamp.commons.lang.security.generate.SymmetricGenerateCipher ;

public class DefaultSecurityOperation implements SecurityOperation {

	private SecurityEntity securityEntity;
	
	private CipherStream cipherStream;
	
	private GenerateCipher generateCipher;
	
	
	public DefaultSecurityOperation(SecurityEntity securityEntity) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException {
		this.securityEntity = securityEntity;
		this.cipherStream   = securityEntity.getCipherStream( );
		if(securityEntity.isSymmetric( )){
			this.generateCipher = new SymmetricGenerateCipher( securityEntity.getAlgorithm( ) , securityEntity.getEncryption( ) );
		}else{
			this.generateCipher = new AsymmetryGenerateCipher( securityEntity.getAlgorithm( ) , securityEntity.getSignName( ) , securityEntity.getDecryption( ) , securityEntity.getEncryption( ) );
		}
	}
	
	@Override
	public byte[] encryption ( byte[] data ) throws Exception {
		return cipherStream.getByte( generateCipher.getPrivateCipher( ) , data , securityEntity.getEncryptionCipherLength( ) , securityEntity.isSymmetric( ) ) ;
	}

	@Override
	public byte[] decryption ( byte[] data ) throws Exception {
		return cipherStream.getByte( generateCipher.getPublicCipher( ) , data , securityEntity.getDecryptionCipherLength( ) , securityEntity.isSymmetric( ) ) ;
	}

	@Override
	public int encryptionAfterLength ( byte[] data ) {
		return 0 ;
	}

	@Override
	public int decryptionAfterLength ( byte[] data ) {
		return 0 ;
	}

	@Override
	public byte[] sign ( byte[] data ) throws SignatureException {
		Signature st = generateCipher.getSign( );
		st.update( data );
		return st.sign( );
	}

	@Override
	public boolean verify ( byte[] data , byte[] verifyByte) throws SignatureException {
		Signature verify = generateCipher.getVerify( );
		verify.update( data );;
		return verify.verify( verifyByte );
	}

	@Override
	public int signAfterLength ( byte[] data ) {
		// TODO 自动生成的方法存根
		return 0 ;
	}

	@Override
	public String getAlgorithm ( ) {
		return securityEntity.getAlgorithm( ) ;
	}

	@Override
	public Integer getKeyLength ( ) {
		return securityEntity.getLength( ) ;
	}

}
