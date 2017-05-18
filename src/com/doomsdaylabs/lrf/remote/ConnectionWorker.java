package com.doomsdaylabs.lrf.remote;

import java.util.Queue;

import com.doomsdaylabs.lrf.remote.beans.Endpoint;
import com.doomsdaylabs.lrf.remote.socket.Reader;
import com.doomsdaylabs.lrf.remote.socket.Writer;

public class ConnectionWorker implements Runnable{
	Endpoint endpoint;
	ProtocolHandler proto;
	Reader reader;
	Writer writer;
	Queue<String> writeQueue;
	
	@Override
	public void run() {
					
		while(true){
			String line = reader.read();
			if (line!=null){
				proto.processLine(line);
			}
			if (!writeQueue.isEmpty()){
				writer.write(writeQueue.poll());
			}
		}
	}
	
}
