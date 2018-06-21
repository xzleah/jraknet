package com.netease.next.jraknet.packet;

public interface PacketIdDefinition {

	byte CONNECTED_PING = (byte) 0x00;
	byte UNCONNECTED_PING = (byte) 0x01;
	byte UNCONNECTED_PING_2 = (byte) 0x02;
	byte CONNECTED_PONG = (byte) 0x03;
	byte OFFLINE_CONNECTION_REQUEST_1 = (byte) 0x05;
	byte OFFLINE_CONNECTION_RESPONSE_1 = (byte) 0x06;

}
