package com.netease.next.jraknet.protocol;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;

import com.netease.next.jraknet.packet.FrameSetPacket;

public class FrameSetPacketPacker {

	private static final int ID_AND_FRAME_SET_INDEX_LEGTH = 4;
	private PriorityBlockingQueue<Frame> frameQueue;
	private Long sendTime;
	private int mtu;
	private FrameSetIndexGenerator frameSetIndexGenerator;

	public FrameSetPacket next() {
		if(this.sendTime == null || frameQueue == null) {
			throw new IllegalStateException();
		}
		Frame headFrame;
		int packetLength = ID_AND_FRAME_SET_INDEX_LEGTH;
		
		List<Frame> frameList = new LinkedList<>();
		while((headFrame = frameQueue.poll()) != null 
				&& headFrame.getSendTime() <= sendTime) {
			
			packetLength += headFrame.lengthOfBytes();
			if(packetLength <= mtu) {
				frameList.add(headFrame);
			} else {
				frameQueue.add(headFrame);
				
				FrameSetPacket frameSetPacket = new FrameSetPacket(frameList);
				frameSetPacket.setFrameSetIndex(frameSetIndexGenerator.generateFrameSetIndex());
				return frameSetPacket;
			}
		}
		if(frameList.size() > 0) {
			FrameSetPacket frameSetPacket = new FrameSetPacket(frameList);
			frameSetPacket.setFrameSetIndex(frameSetIndexGenerator.generateFrameSetIndex());
			return frameSetPacket;
		}
		return null;
	}
	
	public void setFrameQueue(PriorityBlockingQueue<Frame> frameQueue) {
		this.frameQueue = frameQueue;
		
	}

	public void setSendTime(Long sendTime) {
		this.sendTime = sendTime;
		
	}

	public void setMtu(int mtu) {
		this.mtu = mtu;
	}

	public void setFrameSetIndexGenerator(FrameSetIndexGenerator frameSetIndexGenerator) {
		this.frameSetIndexGenerator = frameSetIndexGenerator;
	}

}
