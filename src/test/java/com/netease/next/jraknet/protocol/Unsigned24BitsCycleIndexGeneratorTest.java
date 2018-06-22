package com.netease.next.jraknet.protocol;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class Unsigned24BitsCycleIndexGeneratorTest {

	private Unsigned24BitsCycleIndexGenerator generator;
	
	@Test
	public void testGetAndIncrement_AfterMaxValue() {
		generator = new Unsigned24BitsCycleIndexGenerator(Integer.MAX_VALUE);
		assertThat(generator.getAndIncrement(), equalTo(Integer.MAX_VALUE % 0xFFFFFF));
		assertThat(generator.getAndIncrement(), equalTo(0));
	}
}
