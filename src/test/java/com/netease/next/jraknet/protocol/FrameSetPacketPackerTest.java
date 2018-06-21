package com.netease.next.jraknet.protocol;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;

import org.apache.commons.codec.binary.Hex;
import org.junit.Before;
import org.junit.Test;

import com.netease.next.jraknet.packet.FrameSetPacket;

public class FrameSetPacketPackerTest {

	private static final int MTU = 1464;
	private FrameSetPacketPacker packer;
	
	@Before
	public void setUp() {
		packer = new FrameSetPacketPacker();
		packer.setMtu(MTU);
		packer.setFrameSetIndexGenerator(new FrameSetIndexGenerator() {
			private int index = 0;
			
			@Override
			public int generateFrameSetIndex() {
				return index++;
			}
		});
	}
	
	@Test(expected = IllegalStateException.class)
	public void testNext_failue_withNullFrameQueue() {
		packer.setFrameQueue(null);
		packer.setSendTime(System.currentTimeMillis());
		packer.next();
	}
	
	@Test
	public void testNext_OK_withEmptyFrameQueue() {
		packer.setFrameQueue(new PriorityBlockingQueue<>());
		packer.setSendTime(System.currentTimeMillis());
		FrameSetPacket packet = packer.next();
		assertNull(packet);
	}
	
	@Test(expected = IllegalStateException.class)
	public void testNext_failue_withNotSetSendTime() {
		packer.setFrameQueue(new PriorityBlockingQueue<>());
		packer.next();
	}
	
	@Test
	public void testNext_OK_withOneFrameSendedInTimeWhichBeInsidedAQueue() throws Exception {
		Frame frame = new Frame(Hex.decodeHex("05020000000000000000"));
		frame.setSendTime(System.currentTimeMillis());
		frame.setReliabilityType(ReliabilityType.reliable);
		
		PriorityBlockingQueue<Frame> frameQueue = new PriorityBlockingQueue<>();
		frameQueue.add(frame);
		
		packer.setFrameQueue(frameQueue);
		packer.setSendTime(System.currentTimeMillis()); // always after or equal above frame send time.
		
		FrameSetPacket packet = packer.next();
		
		assertNotNull(packet);
		
		List<Frame> packetFrameList = packet.getFrameList();
		
		assertThat(packetFrameList.size(), equalTo(1));
		assertThat(packetFrameList.get(0), equalTo(frame));
		
		packet = packer.next();
		assertNull(packet);
	}
	
	@Test
	public void testNext_OK_withOneFrameNotSendedInTimeWhichBeInsidedAQueue() throws Exception {
		long sendTime = System.currentTimeMillis();
		
		Frame frame = new Frame(Hex.decodeHex("05020000000000000000"));
		frame.setSendTime(sendTime + 1);
		
		PriorityBlockingQueue<Frame> frameQueue = new PriorityBlockingQueue<>();
		frameQueue.add(frame);
		
		packer.setFrameQueue(frameQueue);
		packer.setSendTime(sendTime);
		
		FrameSetPacket packet = packer.next();
		
		assertNull(packet);
	}
	
	@Test
	public void testNext_OK_with2FramesSendedInTimeWhichBeInsidedAQueue() {
		long sendTime = System.currentTimeMillis();
		Frame frame1 = new Frame(ProtocolUtils.ramdomByteArray(200));
		frame1.setSendTime(sendTime - 1);
		frame1.setReliabilityType(ReliabilityType.reliable);
		
		Frame frame2 = new Frame(ProtocolUtils.ramdomByteArray(MTU - 1 - 3 - frame1.lengthOfBytes() - 1 - 2 - 3));
		frame2.setSendTime(sendTime);
		frame2.setReliabilityType(ReliabilityType.reliable);
		
		// order is frame1,frame2
		PriorityBlockingQueue<Frame> frameQueue = new PriorityBlockingQueue<>(); 
		frameQueue.add(frame1);
		frameQueue.add(frame2);
		
		packer.setFrameQueue(frameQueue);
		packer.setSendTime(sendTime);
		
		FrameSetPacket packet = packer.next();
		assertNotNull(packet);
		assertThat(packet.getFrameList().size(), equalTo(2));
		assertThat(packet.getFrameList().get(0), equalTo(frame1));
		assertThat(packet.getFrameList().get(1), equalTo(frame2));
		
		assertNull(packer.next());
	}
	
	@Test
	public void testNext_OK_returnNotNullPacketTwice() {
		long sendTime = System.currentTimeMillis();
		Frame frame1 = new Frame(ProtocolUtils.ramdomByteArray(200));
		frame1.setSendTime(sendTime - 2);
		frame1.setReliabilityType(ReliabilityType.reliable);
		
		Frame frame2 = new Frame(ProtocolUtils.ramdomByteArray(300));
		frame2.setSendTime(sendTime);
		frame2.setReliabilityType(ReliabilityType.reliable);
		
		Frame frame3 = new Frame(ProtocolUtils.ramdomByteArray(MTU - 1 - 3 - frame1.lengthOfBytes() - frame2.lengthOfBytes() - 6 + 1));
		frame3.setSendTime(sendTime - 1);
		frame3.setReliabilityType(ReliabilityType.reliable);

		// order is frame1,frame3,frame2
		PriorityBlockingQueue<Frame> frameQueue = new PriorityBlockingQueue<>(); 
		frameQueue.add(frame1);
		frameQueue.add(frame2);
		frameQueue.add(frame3);
		
		packer.setFrameQueue(frameQueue);
		packer.setSendTime(sendTime);
		
		FrameSetPacket packet = packer.next();
		assertNotNull(packet);
		assertThat(packet.getFrameList().size(), equalTo(2));
		assertThat(packet.getFrameList().get(0), equalTo(frame1));
		assertThat(packet.getFrameList().get(1), equalTo(frame3));
		
		packet = packer.next();
		assertNotNull(packet);
		assertThat(packet.getFrameList().size(), equalTo(1));
		assertThat(packet.getFrameList().get(0), equalTo(frame2));
		
		assertNull(packer.next());
	}
	
}
