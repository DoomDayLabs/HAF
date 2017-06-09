package com.doomsdaylabs.lrf.remote;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Stream;

import javax.sound.midi.Soundbank;

public class TcpNetworkWorker implements INetworkWorker, Runnable{
	private Thread thread;
	private boolean connected = false;	
	private final String addr;
	private final Queue<String> readBuffer = new ConcurrentLinkedQueue<>();
	private final Queue<String> writeBuffer = new ConcurrentLinkedQueue<>();
	
	public TcpNetworkWorker(String localAddr) {
		addr = localAddr;
	}

	@Override
	public void run() {
		try{				
			Selector selector = SelectorProvider.provider().openSelector();
			SocketChannel chan = SocketChannel.open(new InetSocketAddress(addr, 27015));
			chan.configureBlocking(false);
			chan.register(selector, chan.validOps());	
			connected = true;
			while(selector.select()>-1){
				Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
				
				while(iterator.hasNext()){
					
					SelectionKey key = iterator.next();
					iterator.remove();
					try{
						if (key.isValid()){
							if (key.isReadable()){
								readChan(key);
							} else 
							if (key.isWritable()){
								writeChan(key);
							}
						}
					} catch (IOException e){
						e.printStackTrace();
						closeChan(key);						
						return;
					}
				}
				
			}
			
		} catch (IOException e){
			e.printStackTrace();
			connected = false;
		}
		
		
	}

	
	private void closeChan(SelectionKey key) throws IOException {
		//System.out.println("Close chan");
		connected = false;
		SocketChannel chan = (SocketChannel) key.channel();
		chan.socket().close();
		
	}

	private void readChan(SelectionKey key) throws IOException {
		//System.out.println("CAN READ");
		SocketChannel chan = (SocketChannel) key.channel();
		ByteBuffer buf = ByteBuffer.allocate(4096);
		int readed = chan.read(buf);
		if (readed>0){
			String payload = new String(buf.array());
			System.out.println(payload);
			Stream.of(payload.trim().split("\n"))
			.map(String::trim)
			.forEach(readBuffer::add);			
		} else {
			closeChan(key);
		}
		
	}

	private void writeChan(SelectionKey key) throws IOException {
		//System.out.println("CAN WRITE");
		if (writeBuffer.isEmpty()){
			return;
		}
		String payload = writeBuffer.poll();
		byte[] bytes = payload.getBytes();
		ByteBuffer buff = ByteBuffer.allocate(bytes.length);
		buff.clear();
		buff.put(bytes);
		buff.flip();
		SocketChannel chan = (SocketChannel) key.channel();
		chan.write(buff);						
	}

	@Override
	public boolean isConnected() {
		return connected;
	}

	@Override
	public String read() {
		if (readBuffer.isEmpty())
			return null;
		else
			return readBuffer.poll();
	}

	@Override
	public void write(String str) {
		writeBuffer.add(str+"\r\n");		
	}


	@Override
	public void start() {
		thread = new Thread(this);
		thread.start();
	}
	
	@Override
	public void stop() {
		if (thread!=null){
			thread.interrupt();
		}		
	}

}
