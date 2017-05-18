import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.doomsdaylabs.lrf.remote.ProtocolHandler;
import com.doomsdaylabs.lrf.remote.beans.Endpoint;
import com.doomsdaylabs.lrf.remote.beans.IntSensor;



public class TestSetSensor {
	Endpoint ep = new Endpoint("","","");
	ProtocolHandler proto = new ProtocolHandler(ep);
	
	@Before
	public void init(){
		ep.setState(Endpoint.State.CONNECTED);
	}
	
	@Test
	public void setNotExisitSensor(){
		ep.setState(Endpoint.State.ARMED);
		proto.processLine("SET SENSOR1 10");
	}
	
	@Test
	public void setIntSensorValue(){
		proto.processLine("SENSOR INT SENSOR1 10 30");
		proto.processLine("READY");
		proto.processLine("SET SENSOR1 11");
		IntSensor is = (IntSensor) ep.getSensor("SENSOR1");
		assertEquals(11,is.get());
		proto.processLine("SET SENSOR1 40");
		assertEquals(11,is.get());
	}

	
	
	
	
	
	
	
	
}
