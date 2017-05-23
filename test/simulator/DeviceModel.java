package simulator;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.doomsdaylabs.lrf.remote.beans.Sensor;
import com.doomsdaylabs.lrf.remote.beans.Trigger;

public class DeviceModel {
	public static enum Status{
		DISCONNECTED,
		CONNECTED,
		ARMED
	}
	final String mcastGroup;
	final String endpointClass;
	final String endpointSerial;
	final String endpointName;
	final String pinCode;
	Status status = Status.DISCONNECTED;
	final Set<Sensor> sensors = new HashSet<>();
	private Set<Trigger> triggers = new HashSet<>();
	
	public DeviceModel(String mcastGroup, String endpointClass, String endpointSerial, String endpointName,String pinCode) {
		super();
		this.mcastGroup = mcastGroup;
		this.endpointClass = endpointClass;
		this.endpointSerial = endpointSerial;
		this.endpointName = endpointName;
		this.pinCode = pinCode;
	}

	public String getStringToSend() {
		return null;
	}
	
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}

	public String getEndpointClass() {
		return endpointClass;
	}

	public String getEndpointSerial() {
		return endpointSerial;
	}

	public String getEndpointName() {
		return endpointName;
	}
	
	public String getPinCode() {
		return pinCode;
	}

	public void sensor(Sensor sensor) {
		sensors.add(sensor);				
	}
	
	public Set<Sensor> getSensors() {
		return Collections.unmodifiableSet(sensors);
	}
	

	public void trigger(Trigger trigger) {
		triggers.add(trigger);		
	}
	public Set<Trigger> getTriggers() {
		return Collections.unmodifiableSet(triggers);
	}

	
	
	
	
	
}
