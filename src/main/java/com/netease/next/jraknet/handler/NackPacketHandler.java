package com.netease.next.jraknet.handler;

import com.netease.next.jraknet.packet.NackPacket;
import com.netease.next.jraknet.protocol.FrameQueue;

public class NackPacketHandler implements PacketHandler<NackPacket> {

	private FrameQueue frameQueue;

	public NackPacketHandler() {
		this(null);
	}

	public NackPacketHandler(FrameQueue frameQueue) {
		this.frameQueue = frameQueue;
	}

	@Override
	public void handle(NackPacket nackPacket) {
		if(frameQueue == null) {
			throw new IllegalStateException("can not find frame queue.");
		}
		long sendTime = System.currentTimeMillis();
		if(nackPacket.isRange()) {
			int startIndex = nackPacket.getStartIndex();
			int endIndex = nackPacket.getEndIndex();
			if(startIndex > endIndex) {
				throw new IllegalPacketException("Illegal nack packet with end index large than start index.");
			}
			for(int i = startIndex; i <= endIndex; i++) {
				frameQueue.resetSendTime(i, sendTime);
			}
		} else {
			frameQueue.resetSendTime(nackPacket.getIndex(), sendTime);
		}
	}

	public void setFrameQueue(FrameQueue frameQueue) {
		this.frameQueue = frameQueue;
	}

}
