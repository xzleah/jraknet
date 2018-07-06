package com.netease.next.jraknet.protocol;

public enum ReliabilityType {
	unreliable {
		@Override public boolean isReliable() {return false;}
	},
	unreliableSequenced{
		@Override public boolean isReliable() {return false;}
	},
	reliable{
		@Override public boolean isReliable() {return true;}
	},
	reliableOrdered{
		@Override public boolean isReliable() {return true;}
	},
	reliableSequenced{
		@Override public boolean isReliable() {return true;}
	};

	abstract public boolean isReliable();

	public static ReliabilityType valueOfOrdinal(int ordinal) {
		for(ReliabilityType t : ReliabilityType.values()) {
			if(t.ordinal() == ordinal) {
				return t;
			}
		}
		return null;
	}
}
