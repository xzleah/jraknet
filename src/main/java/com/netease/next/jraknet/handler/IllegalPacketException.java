package com.netease.next.jraknet.handler;

import com.netease.next.jraknet.RaknetException;

public class IllegalPacketException extends RaknetException {

	private static final long serialVersionUID = 5386635259411833611L;

	public IllegalPacketException(String message) {
		super(message);
	}
}
