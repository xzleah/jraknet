package com.netease.next.jraknet.handler;

import java.io.IOException;
import java.net.InetAddress;

import com.netease.next.jraknet.RaknetException;
import com.netease.next.jraknet.packet.OfflineConnectionRequest2Packet;
import com.netease.next.jraknet.packet.OfflineConnectionResponse2Packet;
import com.netease.next.jraknet.protocol.Sender;

public class OfflineConnectionRequest2Handler implements PacketHandler<OfflineConnectionRequest2Packet> {

	private Sender sender;

	private long serverGUID;
	
	private InetAddress clientAddress;
	
	@Override
	public void handle(OfflineConnectionRequest2Packet request) throws RaknetException {
		OfflineConnectionResponse2Packet response = new OfflineConnectionResponse2Packet();
		response.setServerGUID(serverGUID);
		response.setClientAddress(clientAddress);
		response.setMtu(sender.getMtu());
		try {
			sender.send(response);
		} catch (IOException e) {
			throw new RaknetException(e.getMessage(), e);
		}
	}

}
