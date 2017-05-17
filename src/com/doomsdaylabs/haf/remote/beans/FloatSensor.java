package com.doomsdaylabs.haf.remote.beans;

public class FloatSensor extends Sensor{
	final Double min;
	final Double max;
	Double value;
	
	public FloatSensor(String name, Double min, Double max) {
		super(name);
		this.min = min;
		this.max = max;	
		this.value = min;
	}

}
