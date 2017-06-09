package test.services;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.doomsdaylabs.lrf.remote.EndpointWorker;
import com.doomsdaylabs.lrf.remote.IEndpointWorkerFactory;
import com.doomsdaylabs.lrf.remote.beans.Endpoint;
import com.doomsdaylabs.lrf.service.IDiscoveryService;
import com.doomsdaylabs.lrf.service.IEndpointFactory;
import com.doomsdaylabs.lrf.service.IEndpointRepository;
import com.doomsdaylabs.lrf.service.TcpDiscoveryService;

import simulator.DeviceSimulator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={DiscoverServiceTest.Config.class})
public class DiscoverServiceTest {

	
	
	@Autowired
	@Qualifier("discover.mcastgroup")
	String mcastGroup;
	
	DeviceSimulator simulator;
	@Before
	public void init(){
		simulator = new DeviceSimulator(mcastGroup, "TEST", "0001", "TEST1","PINCODE");			
	}
	
	@Autowired
	IDiscoveryService discoverService;
	
	@Autowired
	IEndpointFactory endpointFactory;
	
	@Autowired
	IEndpointWorkerFactory endpointWorkerFactory;
		
	public static class Maker{		
		public static <T> T make(Supplier<T> supplier){
			return supplier.get();
		}
	}
	
	@Test
	public void discoveryTest() throws InterruptedException{	
		when(endpointFactory.buildEndpoint(anyString(), anyString(), anyString(), anyString()))
		.thenReturn(Maker.make(()->{
			Endpoint ep = new Endpoint("127.0.0.1", "TEST1", "0001");
			ep.setState(Endpoint.State.STORED);			
			return ep;
		}));
		
		when(endpointWorkerFactory.buildWorker(any(), any()))
		.thenReturn(Maker.make(()->{			
			return new EndpointWorker(null, null);
		}));
		
		discoverService.startDiscovering();
		simulator.start();
		TimeUnit.SECONDS.sleep(3);
		
		simulator.stop();
		verify(endpointWorkerFactory,atLeastOnce()).buildWorker(any(), any());
						
	}
	


	@Import(com.doomsdaylabs.lrf.service.Config.class)
	public static class Config{
		@Bean
		public IDiscoveryService discoveryService(){
			return new TcpDiscoveryService();
		}
		
		@Bean
		public IEndpointWorkerFactory endpointWorkerFactory(){
			return Mockito.mock(IEndpointWorkerFactory.class);
		}
		
		@Bean
		public IEndpointRepository endpointRepo(){
			return Mockito.mock(IEndpointRepository.class);
		}
		@Bean
		public IEndpointFactory endpointFactory(){
			return Mockito.mock(IEndpointFactory.class);
		}
	}
}
