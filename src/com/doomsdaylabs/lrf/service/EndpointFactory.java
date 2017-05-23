package com.doomsdaylabs.lrf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.doomsdaylabs.lrf.remote.beans.Endpoint;
import com.doomsdaylabs.lrf.remote.beans.Endpoint.State;

@Component
public class EndpointFactory implements IEndpointFactory{

	@Autowired
	IEndpointRepository endpointRepo;
	@Override
	public Endpoint buildEndpoint(String localAddr, String endpointClass, String endpointSerial, String endpointName) {
		String id = endpointClass+"-"+endpointSerial;
		Endpoint endpoint = endpointRepo.get(id);
		if (endpoint==null){
			endpoint = new Endpoint(localAddr, endpointClass, endpointSerial);
			endpoint.getState();
			endpoint.setState(State.DISCOVERED);			
		}
		return endpoint;
	}

}
