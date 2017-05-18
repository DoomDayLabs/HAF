package com.doomsdaylabs.lrf.remote;

import java.util.Queue;

import com.doomsdaylabs.lrf.remote.beans.Endpoint;

public class ConnectionWorker implements Runnable{
	Endpoint endpoint;
	ProtocolHandler proto;	
	Queue<String> writeQueue;
	public ConnectionWorker(String localAddr,String endpointClass, String serial, String name) {
		
	}
	
	public void send(String str){
		
	}
	
	
	@Override
	public void run() {
							
	}
	
	
	
	
}
