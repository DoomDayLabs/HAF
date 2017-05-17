package com.doomsdaylabs.haf.remote.beans;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ValSensor extends Sensor {

	

	public ValSensor(String name, String[] options) {
		super(name);
		this.options = Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(options)));
		value = options[0];
	}
	String value;
	public final Set<String> options;

}
