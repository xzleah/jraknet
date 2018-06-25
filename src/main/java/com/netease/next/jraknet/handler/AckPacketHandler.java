package com.netease.next.jraknet.handler;

import com.netease.next.jraknet.packet.ACKPacket;
import com.netease.next.jraknet.protocol.FrameQueue;

public class AckPacketHandler implements PacketHandler<ACKPacket> {

	private FrameQueue frameQueue;
	
	public AckPacketHandler() {
		this(null);
	}
	
	public AckPacketHandler(FrameQueue frameQueue) {
		this.frameQueue = frameQueue;
	}
	
	@Override
	public void handle(ACKPacket ackPacket) {
		if(frameQueue == null) {
			throw new IllegalStateException("can not find frame queue.");
		}
		if(ackPacket.isRange()) {
			int startIndex = ackPacket.getStartIndex();
			int endIndex = ackPacket.getEndIndex();
			if(startIndex > endIndex) {
				throw new IllegalPacketException("Illegal ack packet with end index large than start index.");
			}
			for(int i = startIndex; i <= endIndex; i++) {
				frameQueue.removeAllFrames(i);
			}
		} else {
			frameQueue.removeAllFrames(ackPacket.getIndex());
		}
	}

	public void setFrameQueue(FrameQueue frameQueue) {
		this.frameQueue = frameQueue;
	}

}
