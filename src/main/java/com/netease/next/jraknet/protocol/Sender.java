package com.netease.next.jraknet.protocol;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import com.netease.next.jraknet.PacketOutput;
import com.netease.next.jraknet.packet.ToClientPacket;

public class Sender implements FrameSetIndexGenerator {

	private PacketOutput out;
	private int mtu;
	private int maxFrameBodySize; // mtu - 60. 
	
	private Unsigned24BitsCycleIndexGenerator reliableFrameIndex = new Unsigned24BitsCycleIndexGenerator(0);
	private AtomicInteger compoundID = new AtomicInteger(0); // 16 bits
	private Unsigned24BitsCycleIndexGenerator frameSetIndex = new Unsigned24BitsCycleIndexGenerator(0);
	
	private FrameQueue frameQueue = new FrameQueue();
	
	public void send(ToClientPacket toClientPacket) throws IOException {
		out.write(toClientPacket.encode());
	}
	
	public void packAndSend(byte[] message) throws IOException {
		packAndSend(new ByteArrayInputStream(message), message.length);
	}
	
	public void packAndSend(InputStream messageStream, int length) throws IOException {
		long sendTime = System.currentTimeMillis();
		if(length > maxFrameBodySize) {
			byte[] frameBody = new byte[maxFrameBodySize];
			int offset = 0;
			int readLength;
			int compoundSize = (int) Math.ceil((float)length / (float)maxFrameBodySize);
			int fragmentIndex = 0;
			int compound = compoundID.getAndIncrement() % 0xFFFF;
			while((readLength = messageStream.read(frameBody, offset, maxFrameBodySize)) != -1) {
				Frame frame = new Frame(Arrays.copyOf(frameBody, readLength));
				frame.setSendTime(sendTime);
				frame.setReliabilityType(ReliabilityType.reliable);
				frame.setFragmented(true);
				frame.setReliableFrameIndex(reliableFrameIndex.getAndIncrement());
				frame.setCompoundSize(compoundSize);
				frame.setCompoundID(compound);
				frame.setFragmentIndex(fragmentIndex++);
				enqueue(frame);
			}
		} else {
			byte[] frameBody = new byte[length];
			messageStream.read(frameBody, 0, length);
			Frame frame = new Frame(frameBody);
			frame.setSendTime(sendTime);
			frame.setReliabilityType(ReliabilityType.reliable);
			frame.setFragmented(false);
			frame.setReliableFrameIndex(reliableFrameIndex.getAndIncrement());
			enqueue(frame);
		}
	}

	private void enqueue(Frame frame) {
		frameQueue.add(frame);
	}
	
	public int getMtu() {
		return mtu;
	}

	public void setMtu(int mtu) {
		this.mtu = mtu;
		this.maxFrameBodySize = mtu - 60;
	}
	
	public FrameQueue getFrameQueue() {
		return frameQueue;
	}

	@Override
	public int generateFrameSetIndex() {
		return frameSetIndex.getAndIncrement();
	}

}
