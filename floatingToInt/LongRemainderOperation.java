package com.lamp.commons.lang.floatingToInt;

public class LongRemainderOperation extends AbstractRemainderOperation<Long>{

	public LongRemainderOperation(int multiple) {
		super(multiple);
	}

	public double reduction(Long t) {
		return (long)t/multiple;
	}
	public double take(Long str, int takeNum){
		return reduction(str)*takeNum;
	}
}
