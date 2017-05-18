import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.doomsdaylabs.lrf.remote.ProtocolHandler;
import com.doomsdaylabs.lrf.remote.beans.Endpoint;
import com.doomsdaylabs.lrf.remote.beans.Trigger;
import com.doomsdaylabs.lrf.remote.beans.Endpoint.State;
import com.doomsdaylabs.lrf.remote.beans.Trigger.FlagParam;
import com.doomsdaylabs.lrf.remote.beans.Trigger.FloatParam;
import com.doomsdaylabs.lrf.remote.beans.Trigger.IntParam;
import com.doomsdaylabs.lrf.remote.beans.Trigger.ValParam;


public class ProtocolDefineTriggerTest {
	Endpoint ep = new Endpoint("","","");
	ProtocolHandler proto = new ProtocolHandler(ep);
	
	@Before
	public void init(){
		ep.setState(State.CONNECTED);
	}
	
	@Test
	public void noParamTrigger(){
		proto.processLine("TRIGGER TRIG1");
		assertNotNull(ep.getTrigger("TRIG1"));		
	}
	
	@Test
	public void intParamTrigger(){
		proto.processLine("TRIGGER TRIG1 INT 0 100");
		Trigger t = ep.getTrigger("TRIG1");
		//assertEquals(IntParam.class, t.getParam(0));
		IntParam p = (IntParam) t.getParam(0);		
		assertEquals(new Integer(0), p.min);
		assertEquals(new Integer(100), p.max);
	}
	
	@Test
	public void floatParamTrigger(){
		proto.processLine("TRIGGER TRIG1 FLOAT 0 100");
		Trigger t = ep.getTrigger("TRIG1");		
		FloatParam p = (FloatParam) t.getParam(0);		
		assertEquals(new Double(0), p.min);
		assertEquals(new Double(100), p.max);
	}
	
	
	@Test
	public void intFloatParamTrigger(){
		proto.processLine("TRIGGER TRIG1 FLOAT 0 100 TRIG2 INT 20 95");
		Trigger t = ep.getTrigger("TRIG1");		
		FloatParam pf = (FloatParam) t.getParam(0);		
		assertEquals(new Double(0), pf.min);
		assertEquals(new Double(100), pf.max);
		
		IntParam pi = (IntParam) t.getParam(1);		
		assertEquals(new Integer(20), pi.min);
		assertEquals(new Integer(95), pi.max);
	}
	
	@Test
	public void valParamTrigger(){
		proto.processLine("TRIGGER TRIG1 VAL VALUE1,VALUE2,VALUE3");
		Trigger t = ep.getTrigger("TRIG1");
		ValParam vp = (ValParam)t.getParam(0);
		assertEquals(3, vp.options.size());
		assertTrue(vp.options.contains("VALUE1"));
		assertTrue(vp.options.contains("VALUE2"));
		assertTrue(vp.options.contains("VALUE3"));
	}
	
	@Test
	public void flagParamTrigger(){
		proto.processLine("TRIGGER TRIG1 FLAG FLAG1,FLAG2,FLAG3");
		Trigger t = ep.getTrigger("TRIG1");
		FlagParam vp = (FlagParam)t.getParam(0);
		assertEquals(3, vp.options.size());
		assertTrue(vp.options.contains("FLAG1"));
		assertTrue(vp.options.contains("FLAG2"));
		assertTrue(vp.options.contains("FLAG3"));
	}
}
