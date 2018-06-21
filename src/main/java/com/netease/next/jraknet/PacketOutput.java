package com.netease.next.jraknet;

import java.io.IOException;

public interface PacketOutput {

	void write(byte[] packetData) throws IOException;
}
