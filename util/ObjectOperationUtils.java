package com.lamp.commons.lang.util;

import java.util.ArrayList ;
import java.util.List ;

public class ObjectOperationUtils {

	public static <T> List<T> isNullCreateArrayList(List<T> list){
		return list == null?new ArrayList<>( ):list;
	}
	
}
