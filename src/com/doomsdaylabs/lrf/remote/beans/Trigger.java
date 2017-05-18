package com.doomsdaylabs.lrf.remote.beans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Trigger {
	public static class Param {

	}
	
	public static class StrParam extends Param{
		
	}
	public static class IntParam extends Param{
		public IntParam(Integer min, Integer max) {
			this.min = min;
			this.max = max;
		}
		public final Integer min;
		public final Integer max;		
	}
	
	public static class FloatParam extends Param{
		public FloatParam(Double min, Double max) {
			this.min = min;
			this.max = max;
		}
		public final Double min;
		public final Double max;		
	}
	
	public static class ValParam extends Param{
		public ValParam(String[] options){
			this.options = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(options)));
		}
		public final Set<String> options;
	}
	
	public static class FlagParam extends Param{
		public FlagParam(String[] options){
			this.options = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(options)));
		}
		public final Set<String> options;
	}

	
	
	public final String name;
	private List<Param> params = new ArrayList<>();
	public Trigger(String name) {
		this.name = name;
	}
	
	public Trigger.Param getParam(int i) {
		return params.get(i);
		
	}

	public void addParam(Param p) {
		params.add(p);
		
	}

}
