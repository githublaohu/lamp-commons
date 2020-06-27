package com.lamp.commons.lang.cmpp.utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class PriorityQueue< T >{

	/** Main lock guarding all access */
	final ReentrantLock lock = new ReentrantLock( );

	/** Condition for waiting takes */
	private final Condition notEmpty = lock.newCondition( );

	/** Condition for waiting puts */
	@SuppressWarnings ( "unused" )
	private final Condition notFull = lock.newCondition( );

	private final AtomicInteger count = new AtomicInteger( );

	private List< Queue< T > > lbqList;

	private int capacity;

	private Priority< T > priority;

	private boolean queue;

	public PriorityQueue( int capacity , Priority< T > priority , boolean queue ) {
		this.capacity = capacity;
		this.priority = priority;
		this.queue = queue;
		init( );
	}

	private void init( ) {
		lbqList = new ArrayList<>( this.capacity );
		for ( int i = 0 ; i < capacity ; i++ ) {
			if ( queue ) {
				lbqList.add( new LinkedList<>( ) );
			} else {
				lbqList.add( new LinkedBlockingQueue<>( ) );
			}
		}
	}

	public int add( T t ) throws Exception {
		int pr = priority.getPriority( t );
		Queue< T > queue = lbqList.get( pr );
		if ( count.get( ) > 0 ) {
			count.getAndIncrement( );
			queue.add( t );
			if ( count.get( ) <= 1 ) {
				final ReentrantLock lock = this.lock;
				try {
					lock.lockInterruptibly( );
					notEmpty.signal( );
				} catch ( Exception e ) {
					return -1;
				} finally {
					lock.unlock( );
				}
			}
		} else {

			final ReentrantLock lock = this.lock;
			try {
				lock.lockInterruptibly( );
				queue.add( t );
				count.getAndIncrement( );
				notEmpty.signal( );
				return pr;
			} catch ( Exception e ) {
				throw e;
			} finally {
				lock.unlock( );
			}
		}
		
		return pr;
	}

	public T get( ) throws InterruptedException {
		T t;
		if ( count.get( ) > 0 ) {
			t = getData();
			if(t != null){
				return t;
			}
		}
		final ReentrantLock lock = this.lock;
		lock.lock( );
		try {
			while ( ( t = getData( ) ) == null )
				notEmpty.await( );
			return t;
		} finally {
			lock.unlock( );
		}
	}

	
	
	
	public static interface Priority< T > {
		int getPriority( T t );
	}

	public T getData(){
		int i = 0;
		T t;
		for( ; ;){
			t = lbqList.get( i++ ).poll( );
			if( t != null){
				count.getAndDecrement( );
				return t;
			}
			if( i >= capacity ){
				return null;
			}
		}
	}


	public int size( ) {
		return count.get( );
	}

	public boolean isEmpty( ) {
		return count.get( ) == 0;
	}


	
}
