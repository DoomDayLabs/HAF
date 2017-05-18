package com.doomsdaylabs.lrf.remote;

import com.doomsdaylabs.lrf.remote.beans.Endpoint;

public interface EndpointFactory {
	Endpoint buildEndpoint(String localAddr, String endpointClass, String endpointSerial, String endpointName);

}
