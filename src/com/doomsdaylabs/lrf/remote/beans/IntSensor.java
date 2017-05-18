package com.doomsdaylabs.lrf.remote.beans;

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

	@Override
	public Object get() {
		return value;
	}

	@Override
	public boolean set(String value) {
		try{
			Integer val = Integer.valueOf(value);
			if (val>=min&&val<=max){
				this.value = val;
				return true;
			}
			return false;
			
		} catch(NumberFormatException e){
			return false;
		}
	}

}
