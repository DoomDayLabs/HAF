package simulator;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Stream;

public class TcpWorker implements Runnable{

	private Queue<String> readQueue = new ConcurrentLinkedQueue<>();
	private Queue<String> writeQueue = new ConcurrentLinkedQueue<>();
	
	private SelectionKey connectionKey = null;
	
	private SelectionKey discoveryKey = null;
	private DatagramChannel discoveryChannel = null;
	private SocketAddress discoveryAddress;	
	private SelectionKey serverKey;
	boolean canSendDiscovery = false;
	
	
	
	public TcpWorker(String mcastAddr,String endpointClass, String endpointSerial, String endpointName) {
		super();
		discoveryAddress = new InetSocketAddress(mcastAddr, 27015);
		this.endpointClass = endpointClass;
		this.endpointSerial = endpointSerial;
		this.endpointName = endpointName;
	}

	private String endpointClass;
	private String endpointSerial;
	private String endpointName;
	
	@Override
	public void run() {
		try {
			loop();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	void loop() throws IOException{
		Selector selector = SelectorProvider.provider().openSelector();
		ServerSocketChannel ssc = ServerSocketChannel.open();
		ssc.configureBlocking(false);
		ssc.socket().bind(new InetSocketAddress("localhost", 27015));
		serverKey = ssc.register(selector, ssc.validOps());
							
		discoveryChannel = DatagramChannel.open();
		discoveryChannel.configureBlocking(false);
		discoveryKey = discoveryChannel.register(selector, SelectionKey.OP_WRITE);
		Timer t = new Timer();
		t.schedule(new TimerTask() {			
			@Override
			public void run() {
				if (connectionKey==null)
					canSendDiscovery = true;

				
			}
		}, 1000L, 1000L);
		
		
			
		while(selector.select()>-1){
			Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();			
			while(iterator.hasNext()){
				SelectionKey key = iterator.next();
				iterator.remove();			
				try{
				if (key.isValid()){
					if (key.isAcceptable()){
						accept(key);
					}else
					if (key.isReadable()){
						read(key);
					}else
					if (key.isWritable()){
						if (key.equals(discoveryKey)&&canSendDiscovery){
							writeDiscovery(key);
						} else
						if (key.equals(connectionKey)){
							write(key);
						}												
					}
				}	
				} catch (IOException e){
					close(key);
				}
			}
		}
		
	}

	private void writeDiscovery(SelectionKey key) throws IOException {
		System.out.println("SIM: Send discovery packet");
		canSendDiscovery = false;
		
		byte[] discoveryStringBytes = (endpointClass+" "+endpointSerial+" "+endpointName).getBytes();
		ByteBuffer discoveryMessage = ByteBuffer.allocate(discoveryStringBytes.length);
		discoveryMessage.clear();
		discoveryMessage.put(discoveryStringBytes);	
		discoveryMessage.flip();	

		discoveryChannel.send(discoveryMessage, discoveryAddress);
		

		
		
	}

	private void write(SelectionKey key) throws IOException {
		
		if (writeQueue.size()>0){
			
			String str = writeQueue.poll()+"\r\n";
			System.out.println("SIM: Writing "+str);
			byte[] b = str.getBytes();
			ByteBuffer buff = ByteBuffer.allocate(b.length);
			buff.clear();
			buff.put(b);
			buff.flip();
			SocketChannel chan = (SocketChannel) key.channel();
			chan.write(buff);		
			System.out.println("Writed "+str);
		}		
	}


	private void read(SelectionKey key) throws IOException {	
		System.out.println("READ MSG");
		SocketChannel sc = (SocketChannel)key.channel();
		ByteBuffer buf = ByteBuffer.allocate(4096);
		int readed = sc.read(buf);
		if (readed<1){
			close(key);
		} else {		
			String msg = new String(buf.array()).trim();
			String[] lines = msg.split("\r\n");
			Stream.of(lines).forEach(System.out::println);
			Stream.of(lines).forEach(readQueue::add);			
		}
	}


	private void accept(SelectionKey key) throws IOException {
		if (connectionKey==null){
			System.out.println("SIM: Accept connection");
			SocketChannel socketChannel = ((ServerSocketChannel)key.channel()).accept();
			socketChannel.configureBlocking(false);
			connectionKey = socketChannel.register(key.selector(), SelectionKey.OP_READ|SelectionKey.OP_WRITE);
		} else {
			System.out.println("SIM: Deny connection");
			((ServerSocketChannel)key.channel()).accept().close();
		}
	}
	
	private void close(SelectionKey key) throws IOException {
		System.out.println("Close");
		key.cancel();
		key.channel().close();
		connectionKey = null;
	}

	public void write(String t) {
		writeQueue.add(t);		
		
	}

	public String read() {
		if (readQueue.size()>0){
			return readQueue.poll();
		} 
		return null;		
	}

	Thread t;
	public void start(){
		t = new Thread(this);
		t.start();
	}
	public void stop(){
		try {
			serverKey.channel().close();			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (connectionKey!=null){
			try {
				close(connectionKey);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (t!=null){
			t.interrupt();
		}
	}

	public void resetConnection() {
		if (connectionKey!=null){
			try {
				close(connectionKey);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	public boolean isConnected() {		
		return connectionKey!=null;
	}
			
}
