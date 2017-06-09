package com.doomsdaylabs.lrf.remote;

import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import com.doomsdaylabs.lrf.remote.beans.Endpoint;
import com.doomsdaylabs.lrf.remote.beans.FlagSensor;
import com.doomsdaylabs.lrf.remote.beans.FloatSensor;
import com.doomsdaylabs.lrf.remote.beans.IntSensor;
import com.doomsdaylabs.lrf.remote.beans.Sensor;
import com.doomsdaylabs.lrf.remote.beans.StrSensor;
import com.doomsdaylabs.lrf.remote.beans.Trigger;
import com.doomsdaylabs.lrf.remote.beans.ValSensor;
import com.doomsdaylabs.lrf.remote.beans.Endpoint.State;

public class ProtocolHandler {
	private final Endpoint endpoint;

	public ProtocolHandler(Endpoint endpoint) {
		super();
		this.endpoint = endpoint;
	}
	
	public Endpoint getEndpoint() {
		return endpoint;
	}
	
	public String processLine(String line) throws IllegalStateException{
		System.out.println("Process Line "+line);
		StringTokenizer st = new StringTokenizer(line, " ");
		switch (st.nextToken()) {
		case "SENSOR":
			if (!endpoint.getState().equals(State.CONNECTED)) throw new IllegalStateException();
			return defineSensor(st);
		case "TRIGGER":
			if (!endpoint.getState().equals(State.CONNECTED)) throw new IllegalStateException();
			return defineTrigger(st);
		case "ACCEPT":
			if (!endpoint.getState().equals(State.STORED)) throw new IllegalStateException();
			return acceptEndpoint(st);
		case "READY":
			if (!endpoint.getState().equals(State.CONNECTED)) throw new IllegalStateException();
			return armEndpoint(st);
		case "SET":
			if (!endpoint.getState().equals(State.ARMED)) throw new IllegalStateException();
			return setSensor(st);
		}
		
		return "ERROR";		
	}

	private String setSensor(StringTokenizer st) {
		String sensorName = st.nextToken();
		String value = st.nextToken();
		Sensor s = endpoint.getSensor(sensorName);
		if (s!=null){
			s.set(value);
		}
		return null;
	}

	private String armEndpoint(StringTokenizer st) {
		endpoint.setState(Endpoint.State.ARMED);
		return null;
	}

	private String acceptEndpoint(StringTokenizer st) {
		endpoint.setState(Endpoint.State.CONNECTED);
		return null;
	}

	private String defineTrigger(StringTokenizer st) {
		String name = st.nextToken();
		Trigger t = new Trigger(name);
		try{
			while (st.hasMoreTokens()){
				switch(st.nextToken()){
				case "STR":addStrToTrigger(t,st);break;
				case "INT":addIntToTrigger(t,st);break;
				case "FLOAT":addFloatToTrigger(t,st);break;			
				case "VAL":addValToTrigger(t,st);break;
				case "FLAG":addFlatToTrigger(t,st);break;			
				}
			
			}			
			endpoint.addTrigger(t);
		} catch (NoSuchElementException e){
			
		}
		
		return "OK";
	}

	private void addStrToTrigger(Trigger t, StringTokenizer st) {
		t.addParam(new Trigger.StrParam());		
	}

	private void addFlatToTrigger(Trigger t, StringTokenizer st) {
		String flagList = st.nextToken();
		String[] flags = flagList.split(",");
		t.addParam(new Trigger.FlagParam(flags));		
	}

	private void addValToTrigger(Trigger t, StringTokenizer st) {
		String optionList = st.nextToken();
		String[] options = optionList.split(",");
		t.addParam(new Trigger.ValParam(options));
		
	}

	private void addFloatToTrigger(Trigger t, StringTokenizer st) {
		String minVal = st.nextToken();
		String maxVal = st.nextToken();		
		t.addParam(new Trigger.FloatParam(Double.valueOf(minVal),Double.valueOf(maxVal)));
		
	}

	private void addIntToTrigger(Trigger t, StringTokenizer st) {		
		String minVal = st.nextToken();
		String maxVal = st.nextToken();
		t.addParam(new Trigger.IntParam(Integer.valueOf(minVal),Integer.valueOf(maxVal)));
	}

	private String defineSensor(StringTokenizer st) {
		String name = st.nextToken();
		try{
			switch (st.nextToken()){
				case "FLOAT":	defineSensorFloat(name,st);break;
				case "INT":		defineSensorInt(name, st);break;
				case "STR":		defineSensorStr(name, st);break;
				case "VAL":		defineSensorVal(name, st);break;
				case "FLAG":	defineSensorFlag(name, st);break;
				default:		return "ERROR";			
			}
			return "OK";
		} catch (NoSuchElementException e){
			return "ERROR";
		}
		
	}

	private void defineSensorFlag(String name, StringTokenizer st) {
		
		String flagList = st.nextToken();
		String[] flags = flagList.split(",");
		FlagSensor s = new FlagSensor(name,flags);
		endpoint.addSensor(s);
		
	}

	private void defineSensorVal(String name, StringTokenizer st) {
		
		String optionsList = st.nextToken();
		String[] options = optionsList.split(",");
		ValSensor s = new ValSensor(name,options);
		endpoint.addSensor(s);
		
	}

	private void defineSensorStr(String name, StringTokenizer st) {
		
		StrSensor s = new StrSensor(name);
		endpoint.addSensor(s);
		
	}

	private void defineSensorInt(String name, StringTokenizer st) {
		
		String valMin = st.nextToken();
		String valMax = st.nextToken();
		Integer min = Integer.valueOf(valMin);
		Integer max = Integer.valueOf(valMax);
		IntSensor s = new IntSensor(name,min,max);
		endpoint.addSensor(s);
		
	}

	private void defineSensorFloat(String name, StringTokenizer st) {
		
		String valMin = st.nextToken();
		String valMax = st.nextToken();
		Double min = Double.valueOf(valMin);
		Double max = Double.valueOf(valMax);
		
		FloatSensor s = new FloatSensor(name,min,max);
		endpoint.addSensor(s);
		
	}

	public boolean validateSend(String input) {
		StringTokenizer st = new StringTokenizer(input, " ");
		String cmd = st.nextToken();
		switch(cmd){
		case "CONNECT":
			if (!State.STORED.equals(endpoint.getState())) throw new IllegalStateException();
			return validateAuth(st);			
		case "CALL":
			if (!State.ARMED.equals(endpoint.getState())) throw new IllegalStateException();
			return validateCall(st);
		}
		
		
		return false;		
	}

	private boolean validateCall(StringTokenizer st) {
		String triggerName = st.nextToken();
		Trigger t = endpoint.getTrigger(triggerName);
		try{
			return t!=null&&t.params().allMatch(param->param.validate(st.nextToken()))&&!st.hasMoreTokens();
		} catch (NoSuchElementException e){
			return false;
		}
	}

	private boolean validateAuth(StringTokenizer st) {
		String pin = st.nextToken();
		return pin!=null&&!pin.isEmpty();
	}

	
}
