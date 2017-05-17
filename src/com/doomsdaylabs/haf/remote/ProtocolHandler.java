package com.doomsdaylabs.haf.remote;

import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import com.doomsdaylabs.haf.remote.beans.Endpoint;
import com.doomsdaylabs.haf.remote.beans.FlagSensor;
import com.doomsdaylabs.haf.remote.beans.FloatSensor;
import com.doomsdaylabs.haf.remote.beans.IntSensor;
import com.doomsdaylabs.haf.remote.beans.StrSensor;
import com.doomsdaylabs.haf.remote.beans.Trigger;
import com.doomsdaylabs.haf.remote.beans.ValSensor;

public class ProtocolHandler {
	private final Endpoint endpoint;

	public ProtocolHandler(Endpoint endpoint) {
		super();
		this.endpoint = endpoint;
	}
	
	public Endpoint getEndpoint() {
		return endpoint;
	}
	
	public String processLine(String line){
		StringTokenizer st = new StringTokenizer(line, " ");
		switch (st.nextToken()) {
		case "SENSOR":
			return defineSensor(st);
		case "TRIGGER":
			return defineTrigger(st);		
		}
		
		return "ERROR";
		
	}

	private String defineTrigger(StringTokenizer st) {
		String name = st.nextToken();
		Trigger t = new Trigger(name);
		
		while (st.hasMoreTokens()){
			switch(st.nextToken()){
			case "INT":addIntToTrigger(t,st);break;
			case "FLOAT":addFloatToTrigger(t,st);break;
			case "VAL":addValToTrigger(t,st);break;
			case "FLAG":addFlatToTrigger(t,st);break;
			case "STR":addStrToTrigger(t,st);break;
			}
		
		}		
		endpoint.addTrigger(t);
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
		try{
			switch (st.nextToken()){
				case "FLOAT":	defineSensorFloat(st);break;
				case "INT":		defineSensorInt(st);break;
				case "STR":		defineSensorStr(st);break;
				case "VAL":		defineSensorVal(st);break;
				case "FLAG":	defineSensorFlag(st);break;
				default:		return "ERROR";			
			}
			return "OK";
		} catch (NoSuchElementException e){
			return "ERROR";
		}
		
	}

	private void defineSensorFlag(StringTokenizer st) {
		String name = st.nextToken();
		String flagList = st.nextToken();
		String[] flags = flagList.split(",");
		FlagSensor s = new FlagSensor(name,flags);
		endpoint.addSensor(s);
		
	}

	private void defineSensorVal(StringTokenizer st) {
		String name = st.nextToken();
		String optionsList = st.nextToken();
		String[] options = optionsList.split(",");
		ValSensor s = new ValSensor(name,options);
		endpoint.addSensor(s);
		
	}

	private void defineSensorStr(StringTokenizer st) {
		String name = st.nextToken();
		StrSensor s = new StrSensor(name);
		endpoint.addSensor(s);
		
	}

	private void defineSensorInt(StringTokenizer st) {
		String name = st.nextToken();
		String valMin = st.nextToken();
		String valMax = st.nextToken();
		Integer min = Integer.valueOf(valMin);
		Integer max = Integer.valueOf(valMax);
		IntSensor s = new IntSensor(name,min,max);
		endpoint.addSensor(s);
		
	}

	private void defineSensorFloat(StringTokenizer st) {
		String name = st.nextToken();
		String valMin = st.nextToken();
		String valMax = st.nextToken();
		Double min = Double.valueOf(valMin);
		Double max = Double.valueOf(valMax);
		
		FloatSensor s = new FloatSensor(name,min,max);
		endpoint.addSensor(s);
		
	}

	
	
	
	
}
