package com.doomsdaylabs.lrf.remote;

import com.doomsdaylabs.lrf.remote.beans.Endpoint;

public class EndpointWorker implements IEndpointWorker,Runnable{
	final Endpoint endpoint;
	final ProtocolHandler proto;
	final INetworkWorker networkWorker;
	public EndpointWorker(Endpoint endpoint, INetworkWorker netWorker) {
		super();
		this.endpoint = endpoint;
		this.networkWorker = netWorker;
		this.proto = new ProtocolHandler(endpoint);		
	}

	
	Thread thread;
	@Override
	public void start() {
		networkWorker.start();
		thread = new Thread(this);
		thread.start();
	}
	
	@Override
	public void stop() {
		networkWorker.stop();
		thread.interrupt();
	}
	
	@Override
	public void run() {
		while(!networkWorker.isConnected()){
			
		}
		
		System.out.println("Auth");
		networkWorker.write("CONNECT "+endpoint.getPinCode());		
		try{
			while(networkWorker.isConnected()){
				String in = networkWorker.read();
				if (in!=null){
					proto.processLine(in);
				}
			}
		} catch (IllegalStateException e){
			e.printStackTrace();
			networkWorker.stop();
		}
		endpoint.setState(Endpoint.State.STORED);
		
	}
	
	@Override
	public void send(String str) {
		networkWorker.write(str);
	}
}
