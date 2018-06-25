package com.netease.next.jraknet.packet;

public class NackPacket implements ToServerPacket {

	private int recordCount;

	private boolean isRange;

	private int index;

	private int startIndex;

	private int endIndex;

	@Override
	public void decodeForm(byte[] packetData) {
		// TODO Auto-generated method stub

	}

	public int getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}

	public boolean isRange() {
		return isRange;
	}

	public void setRange(boolean isRange) {
		this.isRange = isRange;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public int getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}

}
