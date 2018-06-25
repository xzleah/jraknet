package com.netease.next.jraknet.packet;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;

import com.netease.next.jraknet.RaknetException;
import com.netease.next.jraknet.binary.BinaryOutputStream;

public class OfflineConnectionResponse2Packet implements ToClientPacket {

	private long serverGUID;
	private InetAddress clientAddress;
	private int port;
	private int mtu;
	private boolean encryptionEnabled;
	
	@Override
	public byte[] encode() {
		BinaryOutputStream out = new BinaryOutputStream();
		out.putMagic();
		out.putLong(serverGUID);
		putAddress(out);
		out.putShort((short)port);
		return out.get();
	}

	private void putAddress(BinaryOutputStream out) {
		if(clientAddress instanceof Inet4Address) {
			out.putByte((byte)0x04);
			out.putByteArray(clientAddress.getAddress());
		} else if(clientAddress instanceof Inet6Address){
			throw new RaknetException("Unsupport ipv6");
		} else {
			throw new RaknetException("Unknow class of address: " + clientAddress.getClass().getName());
		}
	}

	public long getServerGUID() {
		return serverGUID;
	}

	public void setServerGUID(long serverGUID) {
		this.serverGUID = serverGUID;
	}

	public InetAddress getClientAddress() {
		return clientAddress;
	}

	public void setClientAddress(InetAddress clientAddress) {
		this.clientAddress = clientAddress;
	}

	public int getMtu() {
		return mtu;
	}

	public void setMtu(int mtu) {
		this.mtu = mtu;
	}

	public boolean isEncryptionEnabled() {
		return encryptionEnabled;
	}

	public void setEncryptionEnabled(boolean encryptionEnabled) {
		this.encryptionEnabled = encryptionEnabled;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

}
