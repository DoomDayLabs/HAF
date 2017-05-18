package com.doomsdaylabs.lrf.remote;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import com.doomsdaylabs.lrf.remote.beans.Endpoint;

public class DiscoveryService implements Runnable{
	
	private EndpointFactory endpointFactory;
	private EndpointRepository endpointRepository;
	private EndpointWorkerFactory endpointWorkerFactory;

	@Override
	public void run() {
		
		try {
			InetAddress mcastAddr = InetAddress.getByName("235.19.19.240");
			
			@SuppressWarnings("resource")
			MulticastSocket mcastSock = new MulticastSocket(27015);
			mcastSock.joinGroup(mcastAddr);
			while (true){
				byte[] buff = new byte[1024];
				DatagramPacket dp = new DatagramPacket(buff, buff.length);
				mcastSock.receive(dp);
				parsePacket(dp);
			}
						
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void parsePacket(DatagramPacket dp) {
		String localAddr = dp.getAddress().getHostAddress();
		String[] discoveryString = new String(dp.getData()).trim().split(" ");
		if (discoveryString.length!=3){
			return;
		}
		String endpointClass = discoveryString[0];
		String endpointSerial = discoveryString[1];
		String endpointName = discoveryString[2];
		
		Endpoint endpoint = endpointFactory.buildEndpoint(localAddr,endpointClass,endpointSerial,endpointName);
		endpointRepository.appendEndpoint(endpoint);
		
		if (endpoint.getState().equals(Endpoint.State.STORED)){
			endpointWorkerFactory.buildWorker(endpoint);
		}
		
	}
	
}
