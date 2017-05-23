package test.services;

import java.util.concurrent.TimeUnit;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import com.doomsdaylabs.lrf.remote.EndpointWorker;
import com.doomsdaylabs.lrf.remote.TcpNetworkWorker;
import com.doomsdaylabs.lrf.remote.beans.Endpoint;
import com.doomsdaylabs.lrf.remote.beans.IntSensor;
import com.doomsdaylabs.lrf.remote.beans.Trigger;

import simulator.DeviceSimulator;

public class TestEndpointWorker {

	
	DeviceSimulator simulator = new DeviceSimulator("245.0.0.1", "TEST", "12345", "TEST", "12345");
	Endpoint endpoint = new Endpoint("127.0.0.1", "TEST", "12345");	
	@Before
	public void init(){
		endpoint.setPinCode("12345");
		endpoint.setState(Endpoint.State.STORED);
		simulator.setup(d->{			
			d.sensor(new IntSensor("int1", 0, 100));
			d.trigger(new Trigger("trig1",new Trigger.Param[]{
					new Trigger.IntParam(0, 100),
					new Trigger.FloatParam(0.0,10.0)
			}));
		});
	}
	
	
	@Test	
	public void endpointWorkerAuthCorrectPin() throws InterruptedException{
		TcpNetworkWorker networkWorker = new TcpNetworkWorker("127.0.0.1");
		EndpointWorker endpointWorker = new EndpointWorker(endpoint, networkWorker);		
		simulator.start();
		TimeUnit.SECONDS.sleep(1);
		endpointWorker.start();
		TimeUnit.SECONDS.sleep(2);
		assertEquals(Endpoint.State.ARMED, endpoint.getState());
		assertEquals(1, endpoint.getSensors().size());
		assertNotNull(endpoint.getSensor("int1"));
		assertEquals(Integer.valueOf(0), ((IntSensor)endpoint.getSensor("int1")).getMin());
		assertEquals(Integer.valueOf(100), ((IntSensor)endpoint.getSensor("int1")).getMax());
		
		assertEquals(1, endpoint.getTiggers().size());
		assertNotNull(endpoint.getTrigger("trig1"));
		simulator.stop();
		TimeUnit.SECONDS.sleep(1);
		assertFalse(networkWorker.isConnected());
		assertEquals(Endpoint.State.STORED, endpoint.getState());
	}
	
	
	@Test
	@Ignore
	public void endpointWorkerAuthInCorrectPin() throws InterruptedException{
		TcpNetworkWorker networkWorker = new TcpNetworkWorker("127.0.0.1");
		endpoint.setPinCode("666");
		EndpointWorker endpointWorker = new EndpointWorker(endpoint, networkWorker);		
		simulator.start();
		TimeUnit.SECONDS.sleep(1);
		endpointWorker.start();
		TimeUnit.SECONDS.sleep(2);
		assertEquals(Endpoint.State.STORED, endpoint.getState());		
		
	}
	
}
