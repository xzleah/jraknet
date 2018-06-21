package com.netease.next.jraknet;

import static com.netease.next.jraknet.packet.PacketIdDefinition.OFFLINE_CONNECTION_REQUEST_1;

import com.netease.next.jraknet.packet.OfflineCoinnectionRequest1;
import com.netease.next.jraknet.packet.OfflineConnectionResponse1;

public class PacketHandler {

	public void handle(byte[] packetData, Client client) {
		int packetId = packetData[0];
		switch (packetId) {
		case OFFLINE_CONNECTION_REQUEST_1:
			handleOfflineConnectionRequest(packetData, client);
			break;
//		case OFFLINE_CONNECTION_REQUEST_2:
//			
//			break;

		default:
			break;
		}
	}

	private void handleOfflineConnectionRequest(byte[] packetData, Client client) {
//		ClientContext clientContext = client.getContext();
		OfflineCoinnectionRequest1 request = new OfflineCoinnectionRequest1();
		request.decodeForm(packetData);
		int mtu = request.getMtu();
//		clientContext.setMtu(mtu);
		
		OfflineConnectionResponse1 response = new OfflineConnectionResponse1();
		response.setServerGUID(1L);
		response.setUseSecurity(false);
		response.setMtu(mtu);
		response.sendTo(client);
	}
}
