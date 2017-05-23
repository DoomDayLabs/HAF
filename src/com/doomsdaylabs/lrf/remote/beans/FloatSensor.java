package com.doomsdaylabs.lrf.remote.beans;

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

	@Override
	public Object get() {
		return value;
	}

	@Override
	public boolean set(String value) {
		try{
			Double val = Double.valueOf(value);
			if (val>=min&&val<=max){
				this.value = val;
				return true;
			}
			return false;
		} catch(NumberFormatException e){
			return false;
		}
		
	}
	
	@Override
	public String asString() {
		StringBuilder sb = new StringBuilder("FLOAT ");
		sb.append(getName());
		sb.append(" ");
		sb.append(min);
		sb.append(" ");
		sb.append(max);
		return sb.toString();
	}

}
