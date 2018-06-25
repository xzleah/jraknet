package com.netease.next.jraknet;

import java.io.IOException;

public class RaknetException extends RuntimeException {

	private static final long serialVersionUID = -2132921032456564691L;
	
	public RaknetException(String message, IOException e) {
		super(message, e);
	}

	public RaknetException(String message) {
		super(message);
	}

}
