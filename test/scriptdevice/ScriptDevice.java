package scriptdevice;

public class ScriptDevice implements Runnable{
	public static void main(String[] args){
		(new ScriptDevice("","","","")).run(); 
	}

	private String mcastGroup;
	private String endpointClass;
	private String serial;
	private String name;
	

	public ScriptDevice(String mcastGroup, String endpointClass, String serial, String name) {
		super();
		this.mcastGroup = mcastGroup;
		this.endpointClass = endpointClass;
		this.serial = serial;
		this.name = name;
	}


	public void run() {
		System.out.println("Start simulation");
		TcpWorker worker = new TcpWorker(mcastGroup,endpointClass, serial, name);
		Thread t = new Thread(worker);
		t.start();		
		while (true){
			String in = worker.read();
			if (in!=null){
				
				worker.write("ME:"+in);
			}
			loop();
		}
	}

	
	private void setup(){
		
	}
	
	private void loop() {
		
	}

	Thread t = null;
	public void stop(){
		t.interrupt();		
	}

	public void start() {
		t = new Thread(this);
		t.start();		
	}
}
