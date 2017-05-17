
import org.junit.Test;
import static org.junit.Assert.*;


import com.doomsdaylabs.haf.remote.ProtocolHandler;
import com.doomsdaylabs.haf.remote.beans.Endpoint;
import com.doomsdaylabs.haf.remote.beans.FlagSensor;
import com.doomsdaylabs.haf.remote.beans.FloatSensor;
import com.doomsdaylabs.haf.remote.beans.IntSensor;
import com.doomsdaylabs.haf.remote.beans.StrSensor;
import com.doomsdaylabs.haf.remote.beans.ValSensor;

public class ProtocolDefineSensorTest {
	Endpoint ep = new Endpoint("","","");
	ProtocolHandler proto = new ProtocolHandler(ep);
	@Test
	public void defineIntTriggerTest(){		
		proto.processLine("SENSOR INT TEMPERATURE 0 100");
		assertEquals(1, ep.getSensors().size());
	}
	
	@Test
	public void defineTwoSensors(){
		proto.processLine("SENSOR FLOAT TEMPERATURE 0 100");
		proto.processLine("SENSOR INT LEVEL 0 2");
		assertEquals(2, ep.getSensors().size());
	}
	
	@Test
	public void defineNamedFloatSensor(){
		proto.processLine("SENSOR FLOAT TEMPERATURE 0 100");
		assertNotNull(ep.getSensors().get("TEMPERATURE"));
		assertEquals("TEMPERATURE", ep.getSensors().get("TEMPERATURE").getName());
		assertEquals(FloatSensor.class, ep.getSensor("TEMPERATURE").getClass());
	}
	
	@Test
	public void defineNamedIntSensor(){
		proto.processLine("SENSOR INT WATERLEVEL 0 2");
		assertEquals(IntSensor.class, ep.getSensor("WATERLEVEL").getClass());		
	}
	
	@Test
	public void defineNamedStringSensor(){
		proto.processLine("SENSOR STR HELLO");
		assertEquals(StrSensor.class, ep.getSensor("HELLO").getClass());
	}
	
	@Test
	public void defineNamedValSensor(){
		proto.processLine("SENSOR VAL VALSENSOR VALUE1,VALUE2,VALUE3");
		assertEquals(ValSensor.class, ep.getSensor("VALSENSOR").getClass());
		ValSensor vs = (ValSensor) ep.getSensor("VALSENSOR");
		assertEquals(3,vs.options.size());
		assertTrue(vs.options.contains("VALUE2"));		
	}
	
	@Test
	public void defineNamedFlagSensor(){
		proto.processLine("SENSOR FLAG FLAGSENSOR FLAG1,FLAG2,FLAG3");
		assertEquals(FlagSensor.class, ep.getSensor("FLAGSENSOR").getClass());
		FlagSensor fs = (FlagSensor) ep.getSensor("FLAGSENSOR");
		assertEquals(3, fs.flags.size());
		assertTrue(fs.flags.contains("FLAG1"));
		
	}
	
}
