package com.netease.next.jraknet.protocol;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.PriorityBlockingQueue;

import org.junit.Before;
import org.junit.Test;

public class SenderTest {

	private Sender sender;
	
	@Before
	public void setUp() {
		sender = new Sender();
	}
	
	@Test
	public void testPackAndSend_messageLengthLessThanFragmentBodyMaxSize() throws Exception {
		sender.setMtu(1464);
		byte[] message = ProtocolUtils.ramdomByteArray(500);
		
		sender.packAndSend(message);
		PriorityBlockingQueue<Frame> frameQueue = sender.getFrameQueue();
		assertThat(frameQueue.size(), is(1));
		Frame headFrame = frameQueue.take();
		assertFalse(headFrame.isFragmented());
	}
	
	@Test
	public void testPackAndSend_messageLengthGreaterThanFragmentBodyMaxSize() throws Exception {
		int mtu = 1464;
		int maxFragmentSize = 1464 - 60;
		sender.setMtu(mtu);
		byte[] message = ProtocolUtils.ramdomByteArray(1555);
		
		sender.packAndSend(message);
		PriorityBlockingQueue<Frame> frameQueue = sender.getFrameQueue();
		assertThat(frameQueue.size(), is(2));
		
		Frame firstFrame = frameQueue.poll();
		assertTrue(firstFrame.isFragmented());
		assertThat(firstFrame.getData().length, is(maxFragmentSize));
		assertEquals(firstFrame.getData()[0], message[0]);
		assertThat(firstFrame.getCompoundSize(), is(2));
		assertThat(firstFrame.getCompoundID(), is(0));
		assertThat(firstFrame.getFragmentIndex(), is(0));
		
		Frame secondFrame = frameQueue.poll();
		assertTrue(secondFrame.isFragmented());
		assertThat(secondFrame.getData().length, is(message.length - firstFrame.getData().length));
		assertEquals(secondFrame.getData()[0], message[firstFrame.getData().length]);
		assertThat(secondFrame.getCompoundSize(), is(2));
		assertThat(secondFrame.getCompoundID(), is(0));
		assertThat(secondFrame.getFragmentIndex(), is(1));
	}
}
