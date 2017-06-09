package devicetest;

import java.util.concurrent.TimeUnit;

import org.junit.Ignore;
import org.junit.Test;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.doomsdaylabs.lrf.remote.EndpointWorker;
import com.doomsdaylabs.lrf.remote.IEndpointWorker;
import com.doomsdaylabs.lrf.remote.IEndpointWorkerFactory;
import com.doomsdaylabs.lrf.remote.INetworkWorker;
import com.doomsdaylabs.lrf.remote.TcpNetworkWorker;
import com.doomsdaylabs.lrf.remote.beans.Endpoint;
import com.doomsdaylabs.lrf.remote.beans.Endpoint.State;
import com.doomsdaylabs.lrf.remote.beans.IntSensor;
import com.doomsdaylabs.lrf.service.IDiscoveryService;
import com.doomsdaylabs.lrf.service.IEndpointFactory;
import com.doomsdaylabs.lrf.service.IEndpointRepository;
import com.doomsdaylabs.lrf.service.TcpDiscoveryService;

import test.services.DiscoverServiceTest.Maker;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={DiscoveryTest.Config.class})
public class DiscoveryTest {
	
	@Autowired
	IEndpointFactory endpointFactory;
	
	@Autowired
	IDiscoveryService discover;
	
	@Ignore
	@Test
	public void discoveryTest() throws InterruptedException{
		when(endpointFactory.buildEndpoint(anyString(), anyString(), anyString(), anyString()))
		.thenReturn(Maker.make(()->{
			Endpoint ep = new Endpoint("127.0.0.1", "TEST1", "0001");
			ep.setState(Endpoint.State.DISCOVERED);
			ep.setPinCode("111");
			return ep;
		}));
		
		discover.startDiscovering();
		TimeUnit.SECONDS.sleep(5);
	}
	
	
	@Test
	public void connectTest() throws InterruptedException{
		Endpoint e = new  Endpoint("192.168.0.176", "DEVICE", "12345");
		e.setPinCode("1234");
		e.setState(State.STORED);
		INetworkWorker networkWorker = new TcpNetworkWorker("192.168.0.176");
		IEndpointWorker endpointWorker = new EndpointWorker(e, networkWorker);
		endpointWorker.start();
		TimeUnit.SECONDS.sleep(2);
		IntSensor temperature = (IntSensor) e.getSensor("TEMPERATURE");
		assertNotNull(temperature);
		assertEquals(Integer.valueOf(0), temperature.getMin());
		assertEquals(Integer.valueOf(100), temperature.getMax());
	}
	public static class Config{
		
		@Bean
		@Qualifier("discover.mcastgroup")
		public String mcastGroup(){
			return "239.141.12.12";
		}
		@Bean
		IDiscoveryService discoveryService(){
			return new TcpDiscoveryService();
		}
		
		@Bean
		IEndpointFactory endpointFactory(){
			return mock(IEndpointFactory.class);
		}
		
		@Bean
		IEndpointRepository endpointRepository(){
			return mock(IEndpointRepository.class);
		}
		@Bean
		IEndpointWorkerFactory endpointWorkerFactory(){
			return mock(IEndpointWorkerFactory.class);
		}
		
	}
}
