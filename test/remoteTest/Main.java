package remoteTest;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Main {
	public static void main(String[] args) throws Exception{		
		Selector selector = SelectorProvider.provider().openSelector();
		ServerSocketChannel ssc = ServerSocketChannel.open();
		ssc.configureBlocking(false);
		ssc.socket().bind(new InetSocketAddress("localhost", 27015));
		ssc.register(selector, ssc.validOps());
		
		while(selector.select() > -1){
			Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
			
			Iterable<SelectionKey> it = () -> iterator;
			StreamSupport.stream(it.spliterator(), false)
			.forEach(sk->{
				
			});
		}
		
	}
	
	
	
	
	
}
