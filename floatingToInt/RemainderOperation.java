package com.lamp.commons.lang.floatingToInt;

public interface RemainderOperation<T extends Number> {

	double reduction(T t);
	
	double take(T num , int takeNum);
	
	
	T truncate(String str);
	T truncate(Number num);
	T truncate(Double num);
	
	T round(String str);
	T round(Number num);
	T round(Double num);

	T ceiling(String str);
	T ceiling(Number num);
	T ceiling(Double num);
}
