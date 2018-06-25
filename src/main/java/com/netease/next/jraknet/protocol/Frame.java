package com.netease.next.jraknet.protocol;

import java.util.Arrays;

public class Frame implements Comparable<Frame>, Cloneable {

	/**
	 * 1 byte for flags. 2 bytes for length.
	 */
	private static final int FLAGS_AND_LENGTH = 3;

	private byte[] data;
	private ReliabilityType reliabilityType;
	private boolean fragmented;
	
	private int reliableFrameIndex;
	private int sequencedFrameIndex;
	private int orderedFrameIndex;
	private short orderChannel;
	
	private int compoundSize;
	private int compoundID;
	private int fragmentIndex;

	private long sendTime;

	private int sendTimes;

	public Frame(byte[] data) {
		this.data = data;
		sendTimes = 0;
	}
	
	public int getBodyLengthInBits() {
		return data.length;
	}

	public int getCompoundSize() {
		return compoundSize;
	}

	public void setCompoundSize(int compoundSize) {
		this.compoundSize = compoundSize;
	}

	public int getCompoundID() {
		return compoundID;
	}

	public void setCompoundID(int compoundID) {
		this.compoundID = compoundID;
	}

	public int getFragmentIndex() {
		return fragmentIndex;
	}

	public void setFragmentIndex(int fragmentIndex) {
		this.fragmentIndex = fragmentIndex;
	}

	public byte[] getData() {
		return data;
	}

	public void setReliabilityType(ReliabilityType reliabilityType) {
		this.reliabilityType = reliabilityType;
	}

	public void setFragmented(boolean fragmented) {
		this.fragmented = fragmented;
	}

	public int getReliableFrameIndex() {
		return reliableFrameIndex;
	}

	public void setReliableFrameIndex(int reliableFrameIndex) {
		this.reliableFrameIndex = reliableFrameIndex;
	}

	public void setSendTime(long sendTime) {
		this.sendTime = sendTime;
	}

	public long getSendTime() {
		return sendTime;
	}

	public ReliabilityType getReliabilityType() {
		return reliabilityType;
	}

	public boolean isFragmented() {
		return fragmented;
	}

	public int lengthOfBytes() {
		int length = FLAGS_AND_LENGTH;
		switch (reliabilityType) {
		case unreliable:
			break;
		case unreliableSequenced:
			length += 7;
			break;
		case reliable:
			length += 3;
			break;
		case reliableOrdered:
			length += 7;
			break;
		case reliableSequenced:
			length += 10;
			break;
		default:
			throw new IllegalStateException("unknow reliability type of " + reliabilityType.name());
		}
		if(fragmented) {
			length += 12;
		}
		length += data.length;
		return length;
	}

	public int getSequencedFrameIndex() {
		return sequencedFrameIndex;
	}

	public void setSequencedFrameIndex(int sequencedFrameIndex) {
		this.sequencedFrameIndex = sequencedFrameIndex;
	}

	public int getOrderedFrameIndex() {
		return orderedFrameIndex;
	}

	public void setOrderedFrameIndex(int orderedFrameIndex) {
		this.orderedFrameIndex = orderedFrameIndex;
	}

	public short getOrderChannel() {
		return orderChannel;
	}

	public void setOrderChannel(short orderChannel) {
		this.orderChannel = orderChannel;
	}

	@Override
	public int compareTo(Frame other) {
		long gap = this.sendTime - other.sendTime;
		if(gap < 0) {
			return -1;
		} else if(gap == 0) {
			return 0;
		} else {
			return 1;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + compoundID;
		result = prime * result + compoundSize;
		result = prime * result + Arrays.hashCode(data);
		result = prime * result + fragmentIndex;
		result = prime * result + (fragmented ? 1231 : 1237);
		result = prime * result + orderChannel;
		result = prime * result + orderedFrameIndex;
		result = prime * result + ((reliabilityType == null) ? 0 : reliabilityType.hashCode());
		result = prime * result + reliableFrameIndex;
		result = prime * result + (int) (sendTime ^ (sendTime >>> 32));
		result = prime * result + sendTimes;
		result = prime * result + sequencedFrameIndex;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Frame other = (Frame) obj;
		if (compoundID != other.compoundID)
			return false;
		if (compoundSize != other.compoundSize)
			return false;
		if (!Arrays.equals(data, other.data))
			return false;
		if (fragmentIndex != other.fragmentIndex)
			return false;
		if (fragmented != other.fragmented)
			return false;
		if (orderChannel != other.orderChannel)
			return false;
		if (orderedFrameIndex != other.orderedFrameIndex)
			return false;
		if (reliabilityType != other.reliabilityType)
			return false;
		if (reliableFrameIndex != other.reliableFrameIndex)
			return false;
		if (sendTime != other.sendTime)
			return false;
		if (sendTimes != other.sendTimes)
			return false;
		if (sequencedFrameIndex != other.sequencedFrameIndex)
			return false;
		return true;
	}

	public boolean isReliable() {
		return reliabilityType.isReliable();
	}

	public int timesOfSend() {
		return sendTimes;
	}

	public void increaseTimesOfSend() {
		sendTimes++;
	}

	@Override
	public Frame clone() throws CloneNotSupportedException {
		Frame clone = new Frame(this.data);
		clone.data = this.data;
		clone.reliabilityType = this.reliabilityType;
		clone.fragmented = this.fragmented;
		clone.reliableFrameIndex = this.reliableFrameIndex;
		clone.sequencedFrameIndex = this.sequencedFrameIndex;
		clone.orderedFrameIndex = this.orderedFrameIndex;
		clone.orderChannel = this.orderChannel;
		clone.compoundSize = this.compoundSize;
		clone.compoundID = this.compoundID;
		clone.fragmentIndex = this.fragmentIndex;
		clone.sendTime = this.sendTime;
		clone.sendTimes = this.sendTimes;
		return clone;
	}

}
