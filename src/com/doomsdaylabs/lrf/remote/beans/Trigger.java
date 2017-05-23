package com.doomsdaylabs.lrf.remote.beans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class Trigger {
	public static abstract class Param {
		
		public abstract boolean validate(String param);
		public abstract String asString();

	}
	
	public static class StrParam extends Param{

		@Override
		public boolean validate(String param) {
			return true;
		}
		
		@Override
		public String asString() {
			return "STR";
		}
	


		
	}
	public static class IntParam extends Param{
		public IntParam(Integer min, Integer max) {
			this.min = min;
			this.max = max;
		}
		public final Integer min;
		public final Integer max;
		
		@Override
		public boolean validate(String param) {
			try{
				Integer val = Integer.valueOf(param);
				return val>=min&&val<=max;
			} catch (NumberFormatException e) {
				return false;
			}
		}		
		
		@Override
		public String asString() {
			return "INT "+min+" "+max;
		}
	}
	
	public static class FloatParam extends Param{
		public FloatParam(Double min, Double max) {
			this.min = min;
			this.max = max;
		}
		public final Double min;
		public final Double max;
		@Override
		public boolean validate(String param) {
			try{
				Double val = Double.valueOf(param);
				return val>=min&&val<=max;
			} catch (NumberFormatException e) {
				return false;
			}
		}	
		@Override
		public String asString() {
			return "FLOAT "+min+" "+max;
		}
	}
	
	public static class ValParam extends Param{
		public ValParam(String[] options){
			this.options = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(options)));
		}
		public final Set<String> options;
		@Override
		public boolean validate(String param) {			
			return options.contains(param);
		}
		
		@Override
		public String asString() {
			return "VAL "+String.join(",", options);
		}
	}
	
	public static class FlagParam extends Param{
		public FlagParam(String[] options){
			this.options = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(options)));
		}
		public final Set<String> options;
		@Override
		public boolean validate(String param) {
			String[] flags = param.split(",");
			return Stream.of(flags).allMatch(options::contains);
		}
		@Override
		public String asString() {
			return "FLAG "+String.join(",", options);
		}
	}

	
	
	public final String name;
	private List<Param> params = new ArrayList<>();
	public Trigger(String name) {
		this.name = name;
	}
	public Trigger(String name, Param[] params){
		this.name = name;
		Stream.of(params).forEach(this::addParam);	
	}
	
	public Trigger.Param getParam(int i) {
		return params.get(i);		
	}

	public void addParam(Param p) {
		params.add(p);	
	}
	
	public Stream<Param> params(){
		return params.stream();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(this.name);		
		params.stream().map(p->" "+p.asString()).forEach(sb::append);		
		return sb.toString();
	}

}
