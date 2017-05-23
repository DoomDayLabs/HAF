package com.doomsdaylabs.lrf.remote.beans;

import java.util.HashMap;
import java.util.Map;

public class Endpoint {
	public static enum State {
		DISCOVERED,
		STORED,
		CONNECTED,
		ARMED		
	}
	private State state;
	private String localAddr;
	private String endpointClass;
	private String serial;
	private String name;
	private String pinCode;
	
	public Endpoint(String locaAddr,String name, String serial) {
		this.localAddr = locaAddr;
		this.name = name;
		this.serial = serial;
		this.state = State.DISCOVERED;
	}	
	
	private Map<String,Sensor> sensors = new HashMap<String, Sensor>();
	private Map<String,Trigger> triggers = new HashMap<String,Trigger>();

	public Map<String,Sensor> getSensors() {
		return sensors;
	}

	public Sensor getSensor(String name) {
		return sensors.get(name);
	}

	public void addSensor(Sensor s) {
		sensors.put(s.getName(), s);
		
	}

	public Trigger getTrigger(String name) {
		return triggers.get(name);
	}

	public void addTrigger(Trigger t) {
		triggers.put(t.name, t);		
	}

	
	public String getEndpointClass() {
		return endpointClass;
	}
	
	public String getName() {
		return name;
	}
	public String getSerial() {
		return serial;
	}
	public String getLocalAddr() {
		return localAddr;
	}
	public State getState() {
		return state;
	}
	
	public void setState(State state) {
		this.state = state;
	}

	public String getPinCode() {
		return pinCode;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}

	public Map<String, Trigger> getTiggers() {
		return triggers;
	}
	
	
	
	
}
