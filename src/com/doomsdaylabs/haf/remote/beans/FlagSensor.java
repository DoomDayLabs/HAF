package com.doomsdaylabs.haf.remote.beans;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class FlagSensor extends Sensor {

	public FlagSensor(String name, String[] flags) {
		super(name);
		this.flags = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(flags)));
	}

	public Set<String> flags;
	public Set<String> value;

}
