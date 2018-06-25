package com.netease.next.jraknet.handler;

import com.netease.next.jraknet.packet.ACKPacket;
import com.netease.next.jraknet.protocol.FrameQueue;
import com.netease.next.jraknet.protocol.Sender;

public class AckPacketHandler implements PacketHandler<ACKPacket> {

	private Sender sender;
	
	public AckPacketHandler() {
		this(null);
	}
	
	public AckPacketHandler(Sender sender) {
		this.sender = sender;
	}
	
	@Override
	public void handle(ACKPacket ackPacket) {
		FrameQueue frameQueue = sender.getFrameQueue();
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

	public void setSender(Sender sender) {
		this.sender = sender;
	}

}
