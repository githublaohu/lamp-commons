package com.lamp.commons.lang.security;

import com.lamp.commons.lang.security.cipherstream.CipherStream ;
import com.lamp.commons.lang.security.generate.GenerateCipher ;
import com.lamp.commons.lang.security.verify.DefaultVerify ;

public class SecurityEntity {

	
	public static final SecurityEntityBuilder create(){
		return new SecurityEntityBuilder();
	}
	
	private String algorithm;
	
	private String signName;
	
	private boolean isSymmetric;

	private SecurityEnum securityEnum;

	private byte[] encryption;
	
	private byte[] decryption;	
	
	private int length;
	
	private int keyLength;
	
	private int encryptionCipherLength;
	
	private int decryptionCipherLength;
	
	private CipherStream cipherStream;
	
	
	public String getAlgorithm ( ) {
		return algorithm ;
	}




	public void setAlgorithm ( String algorithm ) {
		this.algorithm = algorithm ;
	}



	
	

	public String getSignName ( ) {
		return signName ;
	}




	public void setSignName ( String signName ) {
		this.signName = signName ;
	}




	public boolean isSymmetric ( ) {
		return isSymmetric ;
	}




	public void setSymmetric ( boolean isSymmetric ) {
		this.isSymmetric = isSymmetric ;
	}




	public SecurityEnum getSecurityEnum ( ) {
		return securityEnum ;
	}




	public void setSecurityEnum ( SecurityEnum securityEnum ) {
		this.securityEnum = securityEnum ;
	}




	public byte[] getEncryption ( ) {
		return encryption ;
	}




	public void setEncryption ( byte[] encryption ) {
		this.encryption = encryption ;
	}




	public byte[] getDecryption ( ) {
		return decryption ;
	}




	public void setDecryption ( byte[] decryption ) {
		this.decryption = decryption ;
	}


	public int getLength ( ) {
		return length ;
	}




	public void setLength ( int length ) {
		this.length = length ;
	}




	public int getKeyLength ( ) {
		return keyLength ;
	}




	public void setKeyLength ( int keyLength ) {
		this.keyLength = keyLength ;
	}




	public int getEncryptionCipherLength ( ) {
		return encryptionCipherLength ;
	}




	public void setEncryptionCipherLength ( int encryptionCipherLength ) {
		this.encryptionCipherLength = encryptionCipherLength ;
	}

	
	

	public int getDecryptionCipherLength ( ) {
		return decryptionCipherLength ;
	}




	public void setDecryptionCipherLength ( int decryptionCipherLength ) {
		this.decryptionCipherLength = decryptionCipherLength ;
	}




	public CipherStream getCipherStream ( ) {
		return cipherStream ;
	}

	public void setCipherStream ( CipherStream cipherStream ) {
		this.cipherStream = cipherStream ;
	}





	public static class SecurityEntityBuilder{
		
		private String algorithm;	
		
		private String signName;

		private SecurityEnum securityEnum;

		private byte[] privateKey;
		
		private byte[] publicKey;
		
		private byte[] key;

		private CipherStream cipherStream; 
		
		
		
		public SecurityEntityBuilder setAlgorithm ( String algorithm ) {
			this.algorithm = algorithm ;
			return this;
		}


		public SecurityEntityBuilder setSignName(String sign){
			this.signName = sign;
			return this;
		}
		
		public SecurityEntityBuilder setSecurityEnum ( SecurityEnum securityEnum ) {
			this.securityEnum = securityEnum ;
			return this;
		}

		public SecurityEntityBuilder setKey ( byte[] key ) {
			this.key = key ;
			return this;
		}
		
		public SecurityEntityBuilder setCipherStream ( CipherStream cipherStream) {
			this.cipherStream = cipherStream ;
			return this;
		}
		
		
		public SecurityEntityBuilder setPublicKey ( byte[] publicKey ) {
			this.publicKey = publicKey ;
			return this;
		}

		public SecurityEntityBuilder setPrivateKey ( byte[] privateKey ) {
			this.privateKey = privateKey ;
			return this;
		}	
		
		public SecurityEntity builder(){
			SecurityEntity securityEntity = new SecurityEntity( );
			securityEntity.setAlgorithm( algorithm );
			String alg = GenerateCipher.getAlgorithm( algorithm );
			securityEntity.setSymmetric( GenerateCipher.isSymmetric( alg ));
			
			
			int length = 0;
			if( securityEntity.isSymmetric( ) ){
				if(this.key != null){
					securityEntity.setEncryption( key );
					securityEntity.setDecryption( key );
					length = key.length << 3;
					if("AES".equals( alg )){
						securityEntity.setEncryptionCipherLength( 0 );
					}else{
						securityEntity.setEncryptionCipherLength( 16 );
					}
					securityEntity.setLength( key.length );
				}else{
					throw new RuntimeException( );
				}
			}else{
				if(this.privateKey != null && this.publicKey!= null){
					int i = 11;
					if(algorithm == "RSA/ECB/PKCS1Padding"){
						i =0;
					}
					// int capacity = (privateKey.length/256)*2*256
					length = (privateKey.length/256)<<9;
			        securityEntity.setEncryptionCipherLength( (length >> 3) - i);
			        securityEntity.setDecryptionCipherLength( (length >> 3));
			        securityEntity.setEncryption( privateKey );
			        securityEntity.setDecryption( publicKey );
				}else{
					throw new RuntimeException( );
				}
			}
			
			securityEntity.setSignName( this.signName );
			
			/*if ( DefaultVerify.verifys( alg , securityEntity.getLength( ) ) ){
				
			}*/
			if( this.securityEnum == null){
				this.securityEnum = SecurityEnum.defaults;
			}
			if(this.cipherStream == null){
				this.cipherStream = CipherStream.BYTE_CIPHER_STREAM;
			}
			securityEntity.setCipherStream( cipherStream );
			return securityEntity;
		}	
	}
	
}
