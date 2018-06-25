package com.netease.next.jraknet.packet;

import com.netease.next.jraknet.binary.BinaryOutputStream;

public class OfflineConnectionResponse1Packet implements ToClientPacket {
	
	private static final byte PACK_ID = PacketIdDefinition.OFFLINE_CONNECTION_RESPONSE_1;

	private long serverGUID;

	private boolean useSecurity;
	
	private int mtu;

	public long getServerGUID() {
		return serverGUID;
	}

	public void setServerGUID(long serverGUID) {
		this.serverGUID = serverGUID;
	}

	public boolean isUseSecurity() {
		return useSecurity;
	}

	public void setUseSecurity(boolean useSecurity) {
		this.useSecurity = useSecurity;
	}

	public int getMtu() {
		return mtu;
	}

	public void setMtu(int mtu) {
		this.mtu = mtu;
	}

	@Override
	public byte[] encode() {
		BinaryOutputStream outputStream = new BinaryOutputStream();
		outputStream.putByte(PACK_ID);
		outputStream.putMagic();
		outputStream.putLong(serverGUID);
		outputStream.putBoolean(useSecurity);
		outputStream.putShort((short)mtu);
		return outputStream.get();
	}

}
