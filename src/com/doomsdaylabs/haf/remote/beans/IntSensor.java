package com.doomsdaylabs.haf.remote.beans;

public class IntSensor extends Sensor{

	final Integer min;
	final Integer max;
	Integer value;

	public IntSensor(String name, Integer min, Integer max) {
		super(name);
		this.min = min;
		this.max = max;
		this.value = min;
	}

}
