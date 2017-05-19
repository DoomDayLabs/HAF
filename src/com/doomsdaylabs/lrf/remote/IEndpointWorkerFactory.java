package com.doomsdaylabs.lrf.remote;

import com.doomsdaylabs.lrf.remote.beans.Endpoint;

public interface IEndpointWorkerFactory {

	IEndpointWorker buildWorker(Endpoint endpoint);

}
