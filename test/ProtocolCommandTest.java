import org.junit.Test;
import static org.junit.Assert.*;

import org.junit.Before;

import com.doomsdaylabs.lrf.remote.ProtocolHandler;
import com.doomsdaylabs.lrf.remote.beans.Endpoint;
import com.doomsdaylabs.lrf.remote.beans.Endpoint.State;

public class ProtocolCommandTest {
	Endpoint ep = new Endpoint("","","");
	ProtocolHandler proto = new ProtocolHandler(ep);

	@Before
	public void init(){
	
	}
	
	@Test	
	public void Test1(){
		ep.setState(State.STORED);
		proto.processLine("ACCEPT SERVERPIN");
		assertEquals(Endpoint.State.CONNECTED, ep.getState());
	}
	
	@Test
	public void defineOnStored(){
		
	}
	
}
