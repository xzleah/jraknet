package com.netease.next.jraknet.packet;

import com.netease.next.jraknet.binary.BinaryInputStream;

public class OfflineConnectionRequest1Packet implements ToServerPacket {

	private short protocolVersion;
	
	private int mtu;
	
	public short getProtocolVersion() {
		return protocolVersion;
	}

	public int getMtu() {
		return mtu;
	}

	@Override
	public void decodeForm(byte[] packetData) {
		BinaryInputStream stream = new BinaryInputStream(packetData);
		stream.skipByte(); // packet id
		stream.skipMagic();
		protocolVersion = stream.getByte();
		mtu = stream.getShort();
	}

}
