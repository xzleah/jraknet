package com.netease.next.jraknet.protocol;

public class Unsigned24BitsCycleIndexGenerator extends UnsignedCycleIndexGenerator {

	public Unsigned24BitsCycleIndexGenerator(int initialValue) {
		super(initialValue);
	}

	@Override
	public int getAndIncrement() {
		return super.getAndIncrement() % 0xFFFFFF;
	}

}
