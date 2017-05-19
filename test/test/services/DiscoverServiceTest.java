package test.services;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.doomsdaylabs.lrf.remote.IEndpointWorker;
import com.doomsdaylabs.lrf.remote.IEndpointWorkerFactory;
import com.doomsdaylabs.lrf.remote.beans.Endpoint;
import com.doomsdaylabs.lrf.remote.beans.Endpoint.State;
import com.doomsdaylabs.lrf.service.DiscoveryService;
import com.doomsdaylabs.lrf.service.IDiscoveryService;
import com.doomsdaylabs.lrf.service.IEndpointFactory;
import com.doomsdaylabs.lrf.service.IEndpointRepository;

import scriptdevice.ScriptDevice;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={DiscoverServiceTest.Config.class})
public class DiscoverServiceTest {

	
	
	@Autowired
	@Qualifier("discover.mcastgroup")
	String mcastGroup;
	
	ScriptDevice sd;
	@Before
	public void init(){
		sd = new ScriptDevice(mcastGroup, "TEST", "0001", "TEST1");		
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
	public void test1() throws InterruptedException{	
		Mockito.when(endpointWorkerFactory.buildWorker((Endpoint)Mockito.notNull())).thenReturn(Maker.make(()->{			
			return null;
		}));
		Mockito.when(endpointFactory.buildEndpoint("192.168.0.125", "TEST", "0001", "TEST1")).thenReturn(Maker.make(()->{
			Endpoint e = new Endpoint("192.168.0.125", "TEST1", "0001");
			e.setState(State.STORED);			
			return e;
		}));
				
		
		discoverService.startDiscovering();
		sd.start();		
		TimeUnit.SECONDS.sleep(3);
		sd.stop();
		Mockito.verify(endpointFactory,atLeastOnce()).buildEndpoint("192.168.0.125","TEST","0001","TEST1");
		//Mockito.verify(endpointWorkerFactory,only()).buildWorker((Endpoint)notNull());
		
	}
	

	//@ComponentScan("com.doomsdaylabs.lrf")
	@Import(com.doomsdaylabs.lrf.service.Config.class)
	public static class Config{
		@Bean
		public IDiscoveryService discoveryService(){
			return new DiscoveryService();
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
