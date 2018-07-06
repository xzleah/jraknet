package com.netease.next.jraknet.binary;

import static com.netease.next.jraknet.binary.Magic.VALUE;

import java.util.Arrays;

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
		byte[] bytes = getByteArray(8);
		return (((long) bytes[0] << 56) +
				((long) (bytes[1] & 0xFF) << 48) +
				((long) (bytes[2] & 0xFF) << 40) +
				((long) (bytes[3] & 0xFF) << 32) +
				((long) (bytes[4] & 0xFF) << 24) +
				((bytes[5] & 0xFF) << 16) +
				((bytes[6] & 0xFF) << 8) +
				((bytes[7] & 0xFF)));
	}

	public int getUint24Le() {
		byte[] bytes = getByteArray(3);
		return (0xff << 24) +
				((bytes[2] & 0xff) << 16) +
				((bytes[1] & 0xff) << 8) +
				(bytes[0] & 0xff);
	}

	public boolean isEnd() {
		return offset >= buf.length;
	}

	public int remainingLength() {
		int remainingLength = buf.length - offset;
		return remainingLength < 0 ? 0 : remainingLength;
	}

	public byte[] getByteArray(int length) {
		return Arrays.copyOfRange(buf, offset, length);
	}
}
