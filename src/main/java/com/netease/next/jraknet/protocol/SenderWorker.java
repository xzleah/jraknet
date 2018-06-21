package com.netease.next.jraknet.protocol;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netease.next.jraknet.packet.FrameSetPacket;

public class SenderWorker implements Runnable {
	
	private static final long DELAY = 200;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private BlockingQueue<Sender> senderQueue;
	private boolean running = true;
	
	public SenderWorker() {
		senderQueue = new LinkedBlockingQueue<>();
	}
	
	@Override
	public void run() {
		while(running) {
			Sender sender;
			try {
				sender = senderQueue.take();
			} catch (InterruptedException e1) {
				running = false;
				continue;
			}
			try {
				doSend(sender);
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}
	
	private void doSend(Sender sender) throws IOException {
		long now = System.currentTimeMillis();
		FrameSetPacketPacker packer = new FrameSetPacketPacker();
		packer.setSendTime(now);
		packer.setFrameQueue(sender.getFrameQueue());
		packer.setFrameSetIndexGenerator(sender);
		packer.setMtu(sender.getMtu());
		
		FrameSetPacket packet = null;
		while((packet = packer.next()) != null) {
			sender.send(packet);
			resendReliableFrame(packet.getFrameList(), sender.getFrameQueue(), now + DELAY);
		}
	}


	private void resendReliableFrame(List<Frame> frameList, PriorityBlockingQueue<Frame> frameQueue, long resendTime) {
		for(Frame frame : frameList) {
			if(frame.isReliable()) {
				frame.setSendTime(resendTime);
				frameQueue.add(frame);
			}
		}
	}

	public void add(Sender sender) {
		senderQueue.add(sender);
		synchronized (this) {
			this.notify();
		}
	}

	public void stop() {
		running = false;
		synchronized (this) {
			this.notify();
		}
	}
}
