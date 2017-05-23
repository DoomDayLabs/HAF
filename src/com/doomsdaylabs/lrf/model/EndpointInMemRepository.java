package com.doomsdaylabs.lrf.model;

import java.util.HashMap;

import org.springframework.stereotype.Repository;

import com.doomsdaylabs.lrf.remote.beans.Endpoint;
import com.doomsdaylabs.lrf.service.IEndpointRepository;

@Repository
public class EndpointInMemRepository extends HashMap<String, Endpoint>implements IEndpointRepository{

	private static final long serialVersionUID = -6145561904175224349L;

	@Override
	public void appendEndpoint(Endpoint endpoint) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Endpoint get(String id) {
		return super.get(id);
	}
	
	

}
