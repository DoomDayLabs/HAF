package com.doomsdaylabs.lrf.remote;

public interface INetworkWorker {
	public void start();
	public void stop();
	public boolean isConnected();
	public String read();
	public void write(String str);
}
