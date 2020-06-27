package com.lamp.commons.lang.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

public class LampDeque<E> implements Queue< E > {

	transient int size = 0;

    transient volatile Node<E> first;


	public LampDeque(){}
    
	
    private static class Node<E> {
        E item;
        Node<E> next;


        Node( E element, Node<E> next) {
            this.item = element;
            this.next = next;
        }
    }
    
	
	@Override
	public boolean isEmpty( ) {
		return false;
	}

	@Override
	public void clear( ) {
		first = null;		
	}

	@Override
	public boolean add( E e ) {
		Node<E> node, newNode = new Node< E >( e , null );
		for( ; ; ){
			node = this.first;
			if(casTabAt(this , node ,newNode)){
				newNode.next = node;
				return true;
			}
		}
	}
	
	@Override
	public Iterator< E > iterator( ) {
		if( this.first == null){
			return null;
		}
		Node<E> node;
		for( ; ; ){
			node = this.first;
			if(casTabAt(this , node ,null)){
				return new LampDequeIterator<>( node );
			}
		}
		
	}

	@Override
	public boolean offer( E e ) {
		throw new RuntimeException( );
	}

	@Override
	public E remove( ) {
		throw new RuntimeException( );
	}

	@Override
	public E poll( ) {
		throw new RuntimeException( );
	}

	@Override
	public E element( ) {
		throw new RuntimeException( );
	}

	@Override
	public E peek( ) {
		throw new RuntimeException( );
	}


	@Override
	public int size( ) {
		throw new RuntimeException( );
	}

	

	@Override
	public boolean remove( Object o ) {
		throw new RuntimeException( );
	}

	@Override
	public boolean contains( Object o ) {
		throw new RuntimeException( );
	}

	@Override
	public boolean addAll( Collection< ? extends E > c ) {
		throw new RuntimeException( );
	}

	@Override
	public boolean removeAll( Collection< ? > c ) {
		throw new RuntimeException( );
	}

	@Override
	public boolean retainAll( Collection< ? > c ) {
		throw new RuntimeException( );
	}
	
	@Override
	public Object[ ] toArray( ) {
		throw new RuntimeException( );
		
	}

	@Override
	public boolean containsAll( Collection< ? > c ) {
		throw new RuntimeException( );
	}
	
	@Override
	public < T > T[ ] toArray( T[ ] a ) {
		throw new RuntimeException( );
	}
	
	

	static class LampDequeIterator<E> implements Iterator<E>{

		
		private Node< E > first;
		
		public LampDequeIterator(Node< E > first ) {
			this.first = first;
		}
		
		@Override
		public boolean hasNext( ) {
			return first != null;
		}

		@Override
		public E next( ) {
			Node< E > node = first;
			first = node.next;
			node.next = null;
			return node.item;
		}
		
	}
	
	

    static final <E> boolean casTabAt(LampDeque<E> lampDeque,Node<E> c, Node<E> v) {
        return U.compareAndSwapObject(lampDeque, FIRST, c, v);
    }
	
	private static final sun.misc.Unsafe U;

	private static final long FIRST;

    static {
        try {
            U = sun.misc.Unsafe.getUnsafe();
            Class<?> k = LampDeque.class;
            FIRST = U.objectFieldOffset(k.getDeclaredField("first"));
        } catch (Exception e) {
            throw new Error(e);
        }
    }
	
}
