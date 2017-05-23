package simulator;

import java.util.function.Consumer;

import simulator.DeviceModel.Status;

public class DeviceSimulator implements Runnable{
	public static void main(String[] args){
		
	}
	final ProtoParser protoParser;
	final DeviceModel deviceData;	
	Thread t;
	TcpWorker worker;
	Consumer<DeviceModel> onSetup;
	Consumer<DeviceModel> onLoop;
	
	public DeviceSimulator(String mcastGroup, String endpointClass, String endpointSerial, String endpointName,String pinCode) {		
		deviceData = new DeviceModel(mcastGroup, endpointClass, endpointSerial, endpointName,pinCode);
		worker = new TcpWorker(mcastGroup, endpointClass, endpointSerial, endpointName);
		protoParser = new ProtoParser(deviceData);		
	}
	
	public DeviceSimulator setup(Consumer<DeviceModel> onSetup){
		this.onSetup = onSetup;
		return this;
	}
	
	public DeviceSimulator loop(Consumer<DeviceModel> onLoop){
		this.onLoop = onLoop;
		return this;
	}
	
	
	
	

	public void start(){
		worker.start();		
		t = new Thread(this);
		t.start();				
	}
	
	public void stop(){
		worker.stop();
		
		if (t!=null){
			t.interrupt();
		}
	}
		
	
	@Override
	public void run() {
		if (onSetup!=null){
			onSetup.accept(deviceData);
		}
		while(true){
			String read = worker.read();
			String write = "";
			if (read!=null){
				write = protoParser.parse(read);
				if (write==null){
					worker.resetConnection();				
				}
			}
						
			if (onLoop!=null){
				onLoop.accept(deviceData);
			}
			
			if (write==null||write.isEmpty())
				write = deviceData.getStringToSend();
			
			if (write!=null){
				worker.write(write);
			}
			if (read!=null&&read.startsWith("CONNECT ")&&deviceData.status.equals(Status.CONNECTED)){
				deviceData.getSensors().stream()
				.map(sensor->"SENSOR "+sensor.asString())
				.forEach(worker::write);
				deviceData.getTriggers().stream()
				.map(trigger->"TRIGGER "+trigger.toString())
				.forEach(worker::write);
				worker.write("READY");
			}
			
						
		}		
	}

	public boolean isConnected() {
		return worker.isConnected();
		
	}

	public DeviceModel getModel() {
		return deviceData;
	}
	
}
