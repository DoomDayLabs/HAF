package socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;

public class NIOWorker{
	public static void main(String[] args){
		try {
			(new NIOWorker()).start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	void start() throws IOException{
		Selector selector = SelectorProvider.provider().openSelector();
		ServerSocketChannel ssc = ServerSocketChannel.open();
		ssc.configureBlocking(false);
		ssc.socket().bind(new InetSocketAddress("localhost", 27015));
		ssc.register(selector, ssc.validOps());
		
		while(selector.select()>-1){
			Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
			while(iterator.hasNext()){
				SelectionKey key = iterator.next();
				iterator.remove();
				if (key.isValid()){
					if (key.isAcceptable()){
						accept(key);
					}else
					if (key.isReadable()){
						read(key);
					}else
					if (key.isWritable()){
						write(key);
					}else
					if (key.isConnectable()){
						connect(key);
					}
				}
				
			}
		}
		
	}
	private void connect(SelectionKey key) {
		System.out.println("Connectable");
		
	}
	private void write(SelectionKey key) {
		System.out.println("Writable");
		key.interestOps(key.interestOps()^key.OP_WRITE);
	}
	
	private void read(SelectionKey key) throws IOException {
		System.out.println("Readable");		
		SocketChannel sc = (SocketChannel)key.channel();
		ByteBuffer buf = ByteBuffer.allocate(4096);
		int readed = sc.read(buf);
		if (readed<1){
			close(key);
		}
		
		String msg = new String(buf.array());
		System.out.println("Readed "+readed+",MSG:"+msg);				
	}
	private void close(SelectionKey key) throws IOException {
		key.cancel();
		key.channel().close();				
	}
	private void accept(SelectionKey key) throws IOException {
		SocketChannel chan = SocketChannel.open();
		chan.configureBlocking(false);
		chan.connect(new InetSocketAddress("localhost", 27015));
		SocketChannel sc = ((ServerSocketChannel)key.channel()).accept();
		System.out.println("COnnected "+sc.getRemoteAddress().toString());
		sc.configureBlocking(false);
		sc.register(key.selector(),SelectionKey.OP_READ|SelectionKey.OP_WRITE);		
	}
}
