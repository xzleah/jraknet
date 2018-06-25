package com.netease.next.jraknet.handler;

import com.netease.next.jraknet.packet.ToServerPacket;

public interface PacketHandler<T extends ToServerPacket> {

	void handle(T packet);
}
