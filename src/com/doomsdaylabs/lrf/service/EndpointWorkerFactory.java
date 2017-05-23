package com.doomsdaylabs.lrf.service;

import org.springframework.stereotype.Component;

import com.doomsdaylabs.lrf.remote.EndpointWorker;
import com.doomsdaylabs.lrf.remote.IEndpointWorker;
import com.doomsdaylabs.lrf.remote.IEndpointWorkerFactory;
import com.doomsdaylabs.lrf.remote.INetworkWorker;
import com.doomsdaylabs.lrf.remote.beans.Endpoint;

@Component
public class EndpointWorkerFactory implements IEndpointWorkerFactory{

	
	@Override
	public IEndpointWorker buildWorker(Endpoint endpoint, INetworkWorker networkWorker) {	
		EndpointWorker worker = new EndpointWorker(endpoint, networkWorker);		
		return worker;
	}

}
