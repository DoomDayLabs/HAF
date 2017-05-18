package com.doomsdaylabs.lrf.remote;

import com.doomsdaylabs.lrf.remote.beans.Endpoint;

public interface EndpointWorkerFactory {

	EndpointWorker buildWorker(Endpoint endpoint);

}
