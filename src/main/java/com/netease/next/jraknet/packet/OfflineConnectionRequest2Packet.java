package com.netease.next.jraknet.packet;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.netease.next.jraknet.RaknetException;
import com.netease.next.jraknet.binary.BinaryInputStream;

public class OfflineConnectionRequest2Packet implements ToServerPacket {

	private InetAddress serverAddress;
	private int port;
	private int mtu;
	private long clientGUID;
	
	@Override
	public void decodeForm(byte[] packetData) {
		BinaryInputStream in = new BinaryInputStream(packetData);
		in.skipMagic();
		byte version = in.getByte();
		if(version != (byte)0x04) {
			throw new RaknetException("Unsupported ip version: " + version);
		}
		byte[] address = new byte[4];
		for(int i = 0; i < 4; i++) {
			address[0] = in.getByte();
		}
		try {
			serverAddress = InetAddress.getByAddress(address);
		} catch (UnknownHostException e) {
			throw new RaknetException(e.getMessage(), e);
		}
		port = in.getShort();
		mtu = in.getShort();
		clientGUID = in.getLong();
	}

	public InetAddress getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(InetAddress serverAddress) {
		this.serverAddress = serverAddress;
	}

	public int getMtu() {
		return mtu;
	}

	public void setMtu(int mtu) {
		this.mtu = mtu;
	}

	public long getClientGUID() {
		return clientGUID;
	}

	public void setClientGUID(long clientGUID) {
		this.clientGUID = clientGUID;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

}
