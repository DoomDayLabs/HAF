import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.doomsdaylabs.lrf.remote.ProtocolHandler;
import com.doomsdaylabs.lrf.remote.beans.Endpoint;
import com.doomsdaylabs.lrf.remote.beans.Endpoint.State;

public class TestSend {

	Endpoint ep = new Endpoint("","","");
	ProtocolHandler proto = new ProtocolHandler(ep);
	
	@Before
	public void init(){
		ep.setState(State.CONNECTED);
	}
	
	@Test
	public void trigWithNoParam(){
		proto.processLine("TRIGGER TRIG");
		proto.processLine("READY");
		assertTrue(proto.validateSend("CALL TRIG"));
		assertFalse(proto.validateSend("CALL TRIG A"));				
	}
	
	@Test
	public void trigWithStrParam(){
		proto.processLine("TRIGGER TRIG STR");
		proto.processLine("READY");
		assertFalse(proto.validateSend("CALL TRIG"));
		assertTrue(proto.validateSend("CALL TRIG A"));
		assertFalse(proto.validateSend("CALL TRIG A B"));
	}
	
	@Test
	public void trigWithIntParam(){
		proto.processLine("TRIGGER TRIG INT 10 100");
		proto.processLine("READY");
		assertFalse(proto.validateSend("CALL TRIG"));
		assertFalse(proto.validateSend("CALL TRIG A"));
		assertFalse(proto.validateSend("CALL TRIG A B"));
		assertTrue(proto.validateSend("CALL TRIG 20"));
		assertFalse(proto.validateSend("CALL TRIG 9"));
		assertFalse(proto.validateSend("CALL TRIG 115"));
		assertFalse(proto.validateSend("CALL TRIG 10 10"));
		
	}
	
	@Test
	public void trigWithFloatParam(){
		proto.processLine("TRIGGER TRIG FLOAT 3.14 5.15");
		proto.processLine("READY");
		assertFalse(proto.validateSend("CALL TRIG"));
		assertFalse(proto.validateSend("CALL TRIG A"));
		assertFalse(proto.validateSend("CALL TRIG A B"));
		assertTrue(proto.validateSend("CALL TRIG 4.14"));
		assertFalse(proto.validateSend("CALL TRIG -1.144"));
		assertFalse(proto.validateSend("CALL TRIG 202.233"));
		assertFalse(proto.validateSend("CALL TRIG 4.123 5.123"));
		
	}
	
	@Test
	public void trigWithValParam(){
		proto.processLine("TRIGGER TRIG VAL VAL1,VAL2,VAL3");
		proto.processLine("READY");
		assertFalse(proto.validateSend("CALL TRIG"));
		assertFalse(proto.validateSend("CALL TRIG A"));
		assertFalse(proto.validateSend("CALL TRIG A B"));
		assertTrue(proto.validateSend("CALL TRIG VAL1"));
		assertTrue(proto.validateSend("CALL TRIG VAL2"));
		assertTrue(proto.validateSend("CALL TRIG VAL3"));
		assertFalse(proto.validateSend("CALL TRIG -1.144"));
		assertFalse(proto.validateSend("CALL TRIG 202.233"));
		assertFalse(proto.validateSend("CALL TRIG 4.123 5.123"));
	}
	
	@Test
	public void trigWithFlagParam(){
		proto.processLine("TRIGGER TRIG FLAG VAL1,VAL2,VAL3");
		proto.processLine("READY");
		assertFalse(proto.validateSend("CALL TRIG"));
		assertFalse(proto.validateSend("CALL TRIG A"));
		assertFalse(proto.validateSend("CALL TRIG A B"));
		assertTrue(proto.validateSend("CALL TRIG VAL1"));
		assertTrue(proto.validateSend("CALL TRIG VAL2"));
		assertTrue(proto.validateSend("CALL TRIG VAL3"));
		assertTrue(proto.validateSend("CALL TRIG VAL1,VAL3"));
		assertTrue(proto.validateSend("CALL TRIG VAL1,VAL2"));
		assertTrue(proto.validateSend("CALL TRIG VAL2,VAL1"));
		assertTrue(proto.validateSend("CALL TRIG VAL1,VAL3,VAL2"));
		assertFalse(proto.validateSend("CALL TRIG -1.144"));
		assertFalse(proto.validateSend("CALL TRIG 202.233"));
		assertFalse(proto.validateSend("CALL TRIG 4.123 5.123"));
	}
	
	
	@Test
	public void trigWithAllParams(){
		proto.processLine("TRIGGER TRIG INT 0 100 FLOAT 3.14 9.11 STR VAL VAL1,VAL2,VAL3 FLAG FLAG1,FLAG2,FLAG3");
		proto.processLine("READY");
		assertTrue(proto.validateSend("CALL TRIG 10 5.1222 TEST VAL1 FLAG2,FLAG1"));
		assertFalse(proto.validateSend("CALL TRIG -1 5.1222 TEST VAL1 FLAG2,FLAG1"));
		assertFalse(proto.validateSend("CALL TRIG 10 15.1222 TEST VAL1 FLAG2,FLAG1"));
		assertFalse(proto.validateSend("CALL TRIG 10 5.1222 TEST VAL41 FLAG2,FLAG1"));
		assertFalse(proto.validateSend("CALL TRIG 10 5.1222 TEST VAL1 FLAG12,FLAG1"));
		assertFalse(proto.validateSend("CALL TRIG 5.1222 TEST VAL1 FLAG2,FLAG1"));
		assertFalse(proto.validateSend("CALL TRIG 10 TEST VAL1 FLAG2,FLAG1"));
		assertFalse(proto.validateSend("CALL TRIG 10 5.1222  VAL1 FLAG2,FLAG1"));
		assertFalse(proto.validateSend("CALL TRIG 10 5.1222 TEST  FLAG2,FLAG1"));
		assertFalse(proto.validateSend("CALL TRIG 10 5.1222 TEST VAL1 "));		
	}
	
	
}
