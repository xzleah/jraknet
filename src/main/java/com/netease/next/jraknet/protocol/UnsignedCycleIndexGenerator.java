package com.netease.next.jraknet.protocol;

import java.util.concurrent.atomic.AtomicInteger;

public class UnsignedCycleIndexGenerator {

	private AtomicInteger index;
	
	public UnsignedCycleIndexGenerator() {
		this(0);
	}
	
	public UnsignedCycleIndexGenerator(int initialValue) {
		if(initialValue < 0) {
			throw new IllegalArgumentException();
		}
		index = new AtomicInteger(initialValue);
	}
	
	public int getAndIncrement() {
		int value = index.getAndIncrement();
		if(value < 0) {
			synchronized (index) {
				if(index.get() < 0) {
					index.set(0);
				}
			}
			value = index.getAndIncrement();
		}
		return value;
	}
}
