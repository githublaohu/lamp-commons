package com.lamp.commons.lang.floatingToInt;

public final class RemainderOperationUtil {

	public static final RemainderOperation<Integer> IntATimes          = new IntRemainderOperation(1);
	
	public static final RemainderOperation<Integer> IntBestTimes       = new IntRemainderOperation(100);
	
	public static final RemainderOperation<Integer> IntThousandTimes   = new IntRemainderOperation(10000);
	
	public static final RemainderOperation<Integer> IntWanTimes        = new IntRemainderOperation(1000000);
	
	public static final RemainderOperation<Long>    LongATimes         = new LongRemainderOperation(1);
	
	public static final RemainderOperation<Long>    LongBestTimes      = new LongRemainderOperation(100);
	
	public static final RemainderOperation<Long>    LongThousandTimes  = new LongRemainderOperation(10000);
	
	public static final RemainderOperation<Long>    LongWanTimes       = new LongRemainderOperation(1000000);
	
}
