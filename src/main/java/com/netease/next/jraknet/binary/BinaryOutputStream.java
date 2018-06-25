package com.netease.next.jraknet.binary;

import java.util.ArrayList;
import java.util.List;

public class BinaryOutputStream {
	
	private List<Byte> buf;
	
	public BinaryOutputStream() {
		buf = new ArrayList<>();
	}
	
	public BinaryOutputStream(int size) {
		buf = new ArrayList<>(size);
	}
	
	public byte[] get() {
		byte[] byteArray = new byte[buf.size()];
		for(int i = 0; i < buf.size(); i++) {
			byteArray[i] = buf.get(0);
		}
		return byteArray;
	}

	public void putByte(byte b) {
		buf.add(b);
	}

	public void putMagic() {
		putByteArray(Magic.VALUE);
	}
	
	public void putByteArray(byte[] byteArray) {
		for(byte b : byteArray) {
			buf.add(b);
		}
	}

	public void putLong(long l) {
		putByteArray(new byte[]{
                (byte) (l >>> 56),
                (byte) (l >>> 48),
                (byte) (l >>> 40),
                (byte) (l >>> 32),
                (byte) (l >>> 24),
                (byte) (l >>> 16),
                (byte) (l >>> 8),
                (byte) (l)
        });
	}

	public void putBoolean(boolean b) {
		if(b) {
			putByte((byte)1);
		} else {
			putByte((byte)0);
		}
	}

	public void putShort(short s) {
		putByteArray(new byte[]{
                (byte) ((s >>> 8) & 0xFF),
                (byte) (s & 0xFF)
        });
	}

	public void putUint24Le(int value) {
		putByteArray(new byte[]{
	                (byte) (value & 0xFF),
	                (byte) ((value >>> 8) & 0xFF),
	                (byte) ((value >>> 16) & 0xFF)
	        });
	}

}
