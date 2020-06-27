package com.lamp.commons.lang.security.generate ;

import java.security.Signature ;

public interface GenerateSignature {
	
	
	public static final int VERIFY = 0 ;

	public static final int SIGN = 1 ;

	public Signature getVerify ( ) ;

	public Signature getSign ( ) ;
}
