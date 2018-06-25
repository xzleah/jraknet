package com.netease.next.jraknet.protocol;

import java.util.LinkedList;
import java.util.List;

import com.netease.next.jraknet.packet.FrameSetPacket;

public class FrameSetPacketPacker {

	private static final int ID_AND_FRAME_SET_INDEX_LEGTH = 4;
	private FrameQueue frameQueue;
	private Long sendTime;
	private int mtu;
	private FrameSetIndexGenerator frameSetIndexGenerator;
	private Frame overloadedFrame = null;

	public FrameSetPacket next() {
		if(this.sendTime == null || frameQueue == null) {
			throw new IllegalStateException();
		}
		List<Frame> frameList = new LinkedList<>();
		int packetLength = ID_AND_FRAME_SET_INDEX_LEGTH;
		
		if(overloadedFrame != null) {
			frameList.add(overloadedFrame);
			packetLength += overloadedFrame.lengthOfBytes();
			overloadedFrame = null;
		}
		
		Frame headFrame;
		while((headFrame =  frameQueue.poll()) != null) {
			
			if(headFrame.getSendTime() > sendTime) {
				frameQueue.add(headFrame);
				break;
			}
			
			packetLength += headFrame.lengthOfBytes();
			if(packetLength <= mtu) {
				frameList.add(headFrame);
			} else {
				overloadedFrame = headFrame;
				
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
	
	public void setFrameQueue(FrameQueue frameQueue) {
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
