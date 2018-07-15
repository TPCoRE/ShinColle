package com.shincolle.primitive;

import java.util.UUID;

/**
 * 描述一只舰的特性
 * */
public final class Feature {
	
	private final UUID identifier;
	private final $Graph feature0;
	
	Feature($Graph f0) {
		this.identifier = null;
		this.feature0 = f0;
	}
	
	/**
	 * Create an instance, if the uuid is for steve, it will throw an exception
	 * */
	public Feature(UUID uuid) {
		this.identifier = uuid;
		this.feature0 = null; //TODO
	}
	
	/**
	 * Get the id for this feature
	 * */
	public final UUID identifier() {
		return this.identifier;
	}
}
