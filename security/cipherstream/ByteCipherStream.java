package com.lamp.commons.lang.security.cipherstream;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import javax.crypto.Cipher;

public class ByteCipherStream implements CipherStream {

	@Override
	public byte[ ] getByte( Cipher cipher , byte[ ] by , int length , boolean  subsection ) throws Exception {
		int bylength;
		if( !subsection && ( bylength = by.length) > length){
			int  more = bylength%length , num = bylength / length,offset = 0 , i=0 ;
			ByteArrayOutputStream  bos = new ByteArrayOutputStream( length *( num+1));
			for( ; ; ){				
				bos.write( cipher.doFinal( by , offset , length ) );
				offset+=length;
				if( ++i == num){
					break;
				}
			}
			if(more > 0){
				bos.write( cipher.doFinal( by , offset , more ));
			}
			return bos.toByteArray( );
		}else{
			return cipher.doFinal( by );
		}
	}

	@Override
	public void stream( Cipher cipher , byte[ ] by , OutputStream ots , int length , boolean  subsection ) throws Exception {
		

	}

}
