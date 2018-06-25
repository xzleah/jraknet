package com.netease.next.jraknet.protocol;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class FrameQueue {
	
	private PriorityQueue<Frame> queue;
	private Map<Integer, List<Frame>> frameSetMap;
	
	public FrameQueue() {
		queue = new PriorityQueue<>();
		frameSetMap = new HashMap<>();
	}

	public boolean add(Frame frame) {
		return queue.add(frame);
	}
	
	public Frame poll() {
		return queue.poll();
	}
	
	public boolean add(int frameSetIndex, Frame frame) {
		boolean ok = queue.add(frame);
		if(ok) {
			List<Frame> frameList = frameSetMap.get(frameSetIndex);
			if(frameList == null) {
				frameList = new LinkedList<>();
				frameSetMap.put(frameSetIndex, frameList);
			}
			frameList.add(frame);
		}
		return ok;
	}
	
	public void removeAllFrames(int frameSetIndex) {
		List<Frame> frameList = frameSetMap.get(frameSetIndex);
		if(frameList == null) {
			return;
		}
		queue.removeAll(frameList);
		frameSetMap.remove(frameSetIndex);
	}

	public int size() {
		return queue.size();
	}

	public void resetSendTime(int frameSetIndex, long sendTime) {
		List<Frame> frameList = frameSetMap.get(frameSetIndex);
		if(frameList == null) {
			return;
		}
		queue.removeAll(frameList);
		
		for(Frame f : frameList) {
			Frame clone = f;
			try {
				clone = f.clone();
			} catch (CloneNotSupportedException e) {
				// never throw.
			}
			clone.setSendTime(sendTime);
			queue.add(clone);
		}
		
	}
}
