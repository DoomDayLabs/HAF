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
	
	@Override
	public String asString() {
		StringBuilder sb = new StringBuilder("INT ");
		sb.append(getName());
		sb.append(" ");
		sb.append(min);
		sb.append(" ");
		sb.append(max);
		return sb.toString();
	}

	public Integer getMin() {
		return min;
	}

	public Integer getMax() {
		return max;
	}

}
