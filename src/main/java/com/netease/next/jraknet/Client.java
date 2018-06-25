package com.netease.next.jraknet;

import java.io.IOException;

import com.netease.next.jraknet.protocol.Sender;

public class Client {
	
	private Sender sender;

	public void send(byte[] packetData) throws RaknetException {
		try {
			sender.packAndSend(packetData);
		} catch (IOException e) {
			throw new RaknetException(e.getMessage(), e);
		}
	}
	
}
