package com.doomsdaylabs.lrf.remote.beans;

public abstract class Sensor {
	private final String name;
	
	public Sensor(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public abstract Object get();

	public abstract boolean set(String value);
	
	public abstract String asString();

}
