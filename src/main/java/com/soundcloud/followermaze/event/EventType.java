package com.soundcloud.followermaze.event;

public enum EventType {
	FOLLOW("F"),
	UNFOLLOW("U"),
	BROADCAST("B"),
	PRIVATE_MESSAGE("P"),
	STATUS_EVENT("S");

	private String flag;

	private EventType(String flag) {
		this.flag = flag;
	}

	public String flag() {
		return flag;
	}
}
