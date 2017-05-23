package com.doomsdaylabs.lrf.service;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

import javax.sound.midi.Soundbank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.doomsdaylabs.lrf.remote.IEndpointWorker;
import com.doomsdaylabs.lrf.remote.IEndpointWorkerFactory;
import com.doomsdaylabs.lrf.remote.INetworkWorker;
import com.doomsdaylabs.lrf.remote.TcpNetworkWorker;
import com.doomsdaylabs.lrf.remote.beans.Endpoint;

@Component
public class TcpDiscoveryService implements Runnable, IDiscoveryService{
	
	@Autowired
	@Qualifier("discover.mcastgroup")
	String mcastGroup;
	
	@Autowired
	private IEndpointFactory endpointFactory;
	@Autowired
	private IEndpointRepository endpointRepository;
	@Autowired
	private IEndpointWorkerFactory endpointWorkerFactory;

		
	@Override
	public void run() {		
		try {
			InetAddress mcastAddr = InetAddress.getByName(mcastGroup);
			
			@SuppressWarnings("resource")
			MulticastSocket mcastSock = new MulticastSocket(27015);
			mcastSock.joinGroup(mcastAddr);		
			System.out.println("Discovery started");
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
		System.out.println("Recieved "+new String(dp.getData())+" from "+localAddr);
		if (discoveryString.length!=3){
			return;
		}
		String endpointClass = discoveryString[0];
		String endpointSerial = discoveryString[1];
		String endpointName = discoveryString[2];
		
		Endpoint endpoint = endpointFactory.buildEndpoint(localAddr,endpointClass,endpointSerial,endpointName);
		endpointRepository.appendEndpoint(endpoint);
		
		if (Endpoint.State.STORED.equals(endpoint.getState())){
			INetworkWorker networkWorker = new TcpNetworkWorker(endpoint.getLocalAddr());
			IEndpointWorker endpointWorker = endpointWorkerFactory.buildWorker(endpoint,networkWorker);
			endpointWorker.start();
		}
		
	}

	@Override
	public void startDiscovering() {
		Thread t = new Thread(this);
		t.start();		
	}
	
}
