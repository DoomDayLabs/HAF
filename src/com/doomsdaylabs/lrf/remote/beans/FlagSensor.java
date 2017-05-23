package com.doomsdaylabs.lrf.remote.beans;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.codec.binary.StringUtils;

public class FlagSensor extends Sensor {

	public FlagSensor(String name, String[] flags) {
		super(name);
		this.flags = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(flags)));
	}

	public Set<String> flags;
	private Set<String> value;
	
	@Override
	public Object get() {
		return new HashSet<>(value);
	}

	@Override
	public boolean set(String value) {
		String[] flagsToSet = value.split(",");
		Set<String> newFlags = new HashSet<>();
		
		for(String flag:flagsToSet){
			if (!flags.contains(flag)){
				return false;
			}
		}
		
		for (String flag:flagsToSet){
			newFlags.add(flag);
		}
		
		this.value = newFlags;
		return true;
	}
	
	@Override
	public String asString() {
		StringBuilder sb= new StringBuilder("FLAG ");
		sb.append(getName());
		sb.append(" ");
		sb.append(String.join(",", flags));
		return sb.toString();
				
	}

	
	
	

}
