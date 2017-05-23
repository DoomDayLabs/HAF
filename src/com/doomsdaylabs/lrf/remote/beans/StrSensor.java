package com.doomsdaylabs.lrf.remote.beans;

public class StrSensor extends Sensor {

	public StrSensor(String name) {
		super(name);
	}
	String value;
	
	@Override
	public Object get() {
		return value;
	}

	@Override
	public boolean set(String value) {
		this.value = value;
		return true;
	}
	
	@Override
	public String asString() {
		return "STR "+getName();		
	}
	

}
