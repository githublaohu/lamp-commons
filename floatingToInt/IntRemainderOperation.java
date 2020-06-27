package com.lamp.commons.lang.floatingToInt;

public class IntRemainderOperation extends AbstractRemainderOperation<Integer> {

	public IntRemainderOperation(int multiple) {
		super(multiple);
	}

	@Override
	public double reduction(Integer t) {
		return t/multiple;
	}
	
	public double take(Integer str, int takeNum){
		return reduction(str)*takeNum;
	}
}