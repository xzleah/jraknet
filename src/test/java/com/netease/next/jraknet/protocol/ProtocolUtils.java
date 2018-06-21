package com.netease.next.jraknet.protocol;

import java.util.Random;

public class ProtocolUtils {

	public static byte[] ramdomByteArray(int length) {
		Random random = new Random();
		byte[] byteArray = new byte[length];
		random.nextBytes(byteArray);
		return byteArray;
	}
}
