package com.doomsdaylabs.lrf.service;

import com.doomsdaylabs.lrf.remote.beans.Endpoint;

public interface IEndpointFactory {
	Endpoint buildEndpoint(String localAddr, String endpointClass, String endpointSerial, String endpointName);

}
