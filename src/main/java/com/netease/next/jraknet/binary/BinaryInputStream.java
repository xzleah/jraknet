package com.netease.next.jraknet.binary;

import static com.netease.next.jraknet.binary.Magic.VALUE;

public class BinaryInputStream {
	
	private byte[] buf;
	private int offset;
	
	public BinaryInputStream(byte[] buf) {
		this.buf = buf;
		offset = 0;
	}

	/**
	 * All packets in Minecraft: Pocket Edition start with their ID, which is an unsigned byte. 
	 */
	public byte getPacketId() {
		return buf[0];
	}

	public void skipByte() {
		offset++;
	}

	public void skipMagic() {
		offset += VALUE.length;
	}

	public byte getByte() {
		return buf[offset];
	}

	public short getShort() {
		short value = (short)((buf[offset] << 8) & buf[offset + 1]);
		offset ++;
		return value;
	}

	public long getLong() {
		// TODO Auto-generated method stub
		return 0;
	}
}
