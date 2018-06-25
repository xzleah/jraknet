package com.netease.next.jraknet.handler;

import java.io.IOException;

import com.netease.next.jraknet.RaknetException;
import com.netease.next.jraknet.packet.OfflineConnectionRequest1Packet;
import com.netease.next.jraknet.packet.OfflineConnectionResponse1Packet;
import com.netease.next.jraknet.protocol.Sender;

public class OfflineConnectionRequest1Handler implements PacketHandler<OfflineConnectionRequest1Packet> {

	private Sender sender;
	
	private long serverGUID;
	
	@Override
	public void handle(OfflineConnectionRequest1Packet request) {
		int mtu = determineMtu(request);
		sender.setMtu(mtu);
		
		OfflineConnectionResponse1Packet response = generateResponse(request, mtu);
		try {
			sender.send(response);
		} catch (IOException e) {
			throw new RaknetException(e.getMessage(), e);
		}
	}

	protected OfflineConnectionResponse1Packet generateResponse(OfflineConnectionRequest1Packet request, int mtu) {
		OfflineConnectionResponse1Packet response = new OfflineConnectionResponse1Packet();
		response.setServerGUID(serverGUID);
		response.setUseSecurity(false);
		response.setMtu(mtu);
		return response;
	}

	protected int determineMtu(OfflineConnectionRequest1Packet request) {
		int mtu = request.getMtu();
		return mtu;
	}

	public void setSender(Sender sender) {
		this.sender = sender;
	}

	public void setServerGUID(long serverGUID) {
		this.serverGUID = serverGUID;
	}

}
