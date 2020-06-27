package com.lamp.commons.lang.time.clock ;

import java.util.concurrent.atomic.AtomicBoolean ;
import java.util.concurrent.locks.LockSupport ;

public class Clock {

	private final static TimeClock timeColck = new TimeClock();
	
	private final static SystemClock systemClock = new SystemClock();
	
	static{
		timeColck.start( );
	}
	
	private final Clockperation clock;
	
	public Clock(){
		this(true);
	}
	
	public Clock(boolean system){
		clock = system?systemClock:timeColck;
	}
	
	public long now(){
		return clock.now( );
	}
	
	interface Clockperation {

		public long now ( ) ;
	}

	static class SystemClock implements Clockperation {

		@Override
		public long now ( ) {
			return System.currentTimeMillis( ) ;
		}

	}

	static class TimeClock extends Thread implements Clockperation {

		private volatile long clock = System.currentTimeMillis( ) ;

		private AtomicBoolean isSuspend = new AtomicBoolean( true ) ;

		
		private long getNano(){
			long namoTime = System.nanoTime( ) ;
			if ( namoTime < 0 ) {
				namoTime = 0 - namoTime ;
			}
			return getMoreDiff(namoTime , 1000000);
		}
		
		private long getMoreDiff(long num , long more){
			return more - num%more;
		}
		
		public void run ( ) {
			LockSupport.park( ) ;
			LockSupport.parkNanos( 1 ) ;
			long namoTime = getNano() ;
			while ( true ) {
				if ( isSuspend.get( ) ) {
					LockSupport.park( ) ;
					clock = System.currentTimeMillis( ) ;
					namoTime = getNano();
				}
				LockSupport.parkNanos( namoTime ) ;
				clock = clock + 1 ;
				namoTime = 1000000 ;
			}
		}

		@Override
		public long now ( ) {
			return clock ;
		}

		public void pause ( ) {
			if ( ! isSuspend.get( ) )
				isSuspend.compareAndSet( false , true ) ;
		}

		public void working ( ) {
			if ( isSuspend.compareAndSet( true , false ) ) {
				LockSupport.unpark( this ) ;
			}
		}

		public boolean isSuspend ( ) {
			return isSuspend.get( ) ;
		}
	}
}
