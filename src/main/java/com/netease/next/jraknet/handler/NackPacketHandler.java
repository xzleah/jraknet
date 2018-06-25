package com.netease.next.jraknet.handler;

import com.netease.next.jraknet.packet.NackPacket;
import com.netease.next.jraknet.protocol.FrameQueue;
import com.netease.next.jraknet.protocol.Sender;

public class NackPacketHandler implements PacketHandler<NackPacket> {

	private Sender sender;

	public NackPacketHandler() {
		this(null);
	}

	public NackPacketHandler(Sender sender) {
		this.sender = sender;
	}

	@Override
	public void handle(NackPacket nackPacket) {
		long sendTime = System.currentTimeMillis();
		FrameQueue frameQueue = sender.getFrameQueue();
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

	public void setSender(Sender sender) {
		this.sender = sender;
	}

}
