package com.netease.next.jraknet.packet;

import java.util.ArrayList;
import java.util.List;

import com.netease.next.jraknet.RaknetException;
import com.netease.next.jraknet.binary.BinaryInputStream;
import com.netease.next.jraknet.binary.BinaryOutputStream;
import com.netease.next.jraknet.protocol.Frame;
import com.netease.next.jraknet.protocol.ReliabilityType;

public class FrameSetPacket implements ToClientPacket, ToServerPacket {

	private List<Frame> frameList;
	private byte packetId;
	private int frameSetIndex;

	public FrameSetPacket(List<Frame> frameList) {
		this(frameList, (byte)0x80);
	}
	
	public FrameSetPacket(List<Frame> frameList, byte packetId) {
		if(frameList == null) {
			throw new IllegalArgumentException("frame list can't be null.");
		}
		this.frameList = frameList;
		this.packetId = packetId;
	}

	@Override
	public byte[] encode() {
		BinaryOutputStream outputStream = new BinaryOutputStream();
		outputStream.putByte(packetId);
		outputStream.putUint24Le(frameSetIndex);
		for(Frame frame : frameList) {
			int flags = getFlags(frame);
			outputStream.putByte((byte)flags);
			outputStream.putShort((short)frame.getBodyLengthInBits());
			switch (frame.getReliabilityType()) {
			case unreliable:
				break;
			case unreliableSequenced:
				outputStream.putUint24Le(frame.getSequencedFrameIndex());
				outputStream.putUint24Le(frame.getOrderedFrameIndex());
				outputStream.putByte((byte)frame.getOrderChannel());
				break;
			case reliable:
				outputStream.putUint24Le(frame.getReliableFrameIndex());
				break;
			case reliableOrdered:
				outputStream.putUint24Le(frame.getReliableFrameIndex());
				outputStream.putUint24Le(frame.getOrderedFrameIndex());
				outputStream.putByte((byte)frame.getOrderChannel());
				break;
			case reliableSequenced:
				outputStream.putUint24Le(frame.getReliableFrameIndex());
				outputStream.putUint24Le(frame.getSequencedFrameIndex());
				outputStream.putUint24Le(frame.getOrderedFrameIndex());
				outputStream.putByte((byte)frame.getOrderChannel());
				break;
			default:
				throw new IllegalStateException("unknow reliability type of " + frame.getReliabilityType().name());
			}
			outputStream.putByteArray(frame.getData());
		}
		return outputStream.get();
	}

	/**
	 * Top 3 bits are reliability type,
	 * fourth bit is 1 when the frame is fragmented and part of a compound.
	 */
	protected int getFlags(Frame frame) {
		ReliabilityType reliabilityType = frame.getReliabilityType();
		int flags = reliabilityType.ordinal();
		flags = flags << 5;
		if(frame.isFragmented()) {
			flags = flags & 0x10;
		}
		return flags;
	}

	public void setPacketId(byte packetId) {
		this.packetId = packetId;
	}

	public void setFrameSetIndex(int frameSetIndex) {
		this.frameSetIndex = frameSetIndex;
	}

	public List<Frame> getFrameList() {
		return frameList;
	}

	public int getIndex() {
		return frameSetIndex;
	}

	@Override
	public void decodeForm(byte[] packetData) {
		BinaryInputStream in = new BinaryInputStream(packetData);
		packetId = in.getByte();
		frameSetIndex = in.getUint24Le();
		frameList = new ArrayList<>();
		while(!in.isEnd()) {
			Frame frame = new Frame();
			byte flags = in.getByte();
			ReliabilityType reliabilityType = getReliabilityType(flags);
			if(reliabilityType == null) {
				throw new RaknetException("Invalid frame set packet.");
			}
			frame.setReliabilityType(reliabilityType); ;
			frame.setFragmented(isFragmented(flags));
			short length = in.getShort();
			switch (reliabilityType) {
			case unreliable:
				break;
			case unreliableSequenced:
				frame.setSequencedFrameIndex(in.getUint24Le());
				frame.setOrderedFrameIndex(in.getUint24Le());
				frame.setOrderChannel(in.getByte());
				break;
			case reliable:
				frame.setReliableFrameIndex(in.getUint24Le());
				break;
			case reliableOrdered:
				frame.setReliableFrameIndex(in.getUint24Le());
				frame.setOrderedFrameIndex(in.getUint24Le());
				frame.setOrderChannel(in.getByte());
				break;
			case reliableSequenced:
				frame.setReliableFrameIndex(in.getUint24Le());
				frame.setSequencedFrameIndex(in.getUint24Le());
				frame.setOrderedFrameIndex(in.getUint24Le());
				frame.setOrderChannel(in.getByte());
				break;
			default:
				throw new IllegalStateException("unknow reliability type of " + frame.getReliabilityType().name());
			}
			length = (short) Math.min(length, in.remainingLength());
			frame.setData(in.getByteArray(length));
		}
	}

	private boolean isFragmented(byte flags) {
		return ((flags & 0x10) == 1) ? true : false;
	}

	private ReliabilityType getReliabilityType(byte flags) {
		int ordinal = flags >>> 5;
		return ReliabilityType.valueOfOrdinal(ordinal);
	}

}
