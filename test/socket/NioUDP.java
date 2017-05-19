package socket;

import java.io.IOException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.Selector;
import java.nio.channels.spi.SelectorProvider;

public class NioUDP {
	void test() throws IOException{
		Selector selector = SelectorProvider.provider().openSelector();				
		DatagramChannel dc = DatagramChannel.open();
		dc.register(selector, dc.validOps());
		
	}
}
