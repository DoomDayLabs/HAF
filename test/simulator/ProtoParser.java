package simulator;

import java.util.StringTokenizer;

import simulator.DeviceModel.Status;

public class ProtoParser {
	final DeviceModel deviceData;
	public ProtoParser(DeviceModel deviceData) {
		this.deviceData = deviceData;
	}

	public String parse(String line){
		StringTokenizer st = new StringTokenizer(line);
		String cmd = st.nextToken();
		switch (cmd){
			case "CONNECT":return connect(st);					
			case "CALL":return callTrigget(st);				
		}		
		return "";
	}
	
	private String callTrigget(StringTokenizer st) {		
		return "";
	}

	private String connect(StringTokenizer st){
		if (!deviceData.status.equals(Status.DISCONNECTED))
			return "";		
		String pinCode = st.nextToken();
		System.out.println("SIM: Process connect with PIN "+pinCode);
		if (deviceData.getPinCode().equals(pinCode)){
			deviceData.setStatus(Status.CONNECTED);
			System.out.println("SIM: Accept connection");
			return "ACCEPT";
		} else {
			return null;
		}
		
	}
	
}
