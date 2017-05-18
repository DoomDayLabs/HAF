package com.doomsdaylabs.lrf.remote.beans;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

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

	
	
	

}
