package com.netease.next.jraknet.packet;

import com.netease.next.jraknet.Client;

public interface ToClientPacket {

	byte[] encode();

	default void sendTo(Client client) {
		client.send(encode());
	}

}
