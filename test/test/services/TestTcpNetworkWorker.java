package test.services;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.junit.Ignore;
import org.junit.Test;

import com.doomsdaylabs.lrf.remote.TcpNetworkWorker;

import simulator.DeviceModel;
import simulator.DeviceSimulator;

public class TestTcpNetworkWorker {

	DeviceSimulator simulator = new DeviceSimulator("245.0.0.1", "TEST", "12345", "TEST", "12345");
	
	@Test
	public void connectionTest() throws InterruptedException{
		simulator.start();
		TimeUnit.SECONDS.sleep(1);
		TcpNetworkWorker worker = new TcpNetworkWorker("127.0.0.1");
		worker.start();
		TimeUnit.SECONDS.sleep(1);
		assertTrue(simulator.isConnected());
		simulator.stop();		
	}
	
	@Test
	public void manualAuthTest() throws InterruptedException{
		simulator.start();
		TimeUnit.MILLISECONDS.sleep(100);
		TcpNetworkWorker worker = new TcpNetworkWorker("127.0.0.1");
		worker.start();
		TimeUnit.MILLISECONDS.sleep(1000);
		worker.write("CONNECT 12345");
		TimeUnit.MILLISECONDS.sleep(1000);
		assertEquals(DeviceModel.Status.CONNECTED, simulator.getModel().getStatus());
		String str = worker.read();
		assertEquals("ACCEPT", str);		
	}
}
