package test.services;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.doomsdaylabs.lrf.service.IDiscoveryService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={ServerUpTest.Config.class})
public class ServerUpTest {

	@Autowired
	List<IDiscoveryService> discoveryServices;
	
	@Test
	public void upTest(){
		discoveryServices.forEach(s->s.startDiscovering());
	}
	
	@ComponentScan("com.doomsdaylabs.lrf")
	public static class Config{
		
	}
}
