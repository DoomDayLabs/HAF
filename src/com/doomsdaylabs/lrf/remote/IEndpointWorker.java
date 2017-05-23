package com.doomsdaylabs.lrf.remote;

public interface IEndpointWorker {
	void start();
	void stop();
	void send(String str);
	
}
