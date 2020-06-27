package com.lamp.commons.lang.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class LampThreadFactory implements ThreadFactory{

	private final static String SEQARATOR = "_";
	
	
	private AtomicInteger threadIndex = new AtomicInteger(0);
	
	private final String name;
	
	private final String separator;
	
	
	public LampThreadFactory(String name){
		this(name , SEQARATOR);
	}
	
	public LampThreadFactory(String name , String separator){
		this.name = name;
		this.separator = separator;
		
	}

    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r, name + separator+this.threadIndex.incrementAndGet());
    }

	

}
