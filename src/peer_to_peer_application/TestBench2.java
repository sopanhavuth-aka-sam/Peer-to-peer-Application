package peer_to_peer_application;

public class TestBench2 {
	
	private static TokenManager manager;
	
	private static Worker[] workers;
	
	private static NetworkMonitor network;
	
	private final static int WORKER_NUM = 5, PORT_ONE = 1235, PORT_TWO = 8800;
	
	public static void main(String[] args) {
		//initialize manager and workers object
		manager = new TokenManager();
		workers = new Worker[WORKER_NUM];
		
		//create a token in manager1. ONLY ONE TOKEN CHOULD BE CREATED
		manager.createToken();
		
		//link workers to manager
		for(int i = 0; i < WORKER_NUM; i++ ) {
			workers[i] = new Worker(manager);
		}	
		
		//initialize NetworkMonitor
		network = new NetworkMonitor(manager, PORT_TWO);
		
		//initialize client networking components in TokenManager
		manager.initClientComponent(PORT_ONE);
		
		//start NetworkMonitor (have to start before TokenkManager(client))
		network.start();
		
		//start manager and workers thread
		manager.start();
		for(int i = 0; i < WORKER_NUM; i++ ) {
			workers[i].start();
		}
		
		//wait for workers to finish their jobs
		for(int i = 0; i < WORKER_NUM; i++) {
			try {
				workers[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		//wait for managers to finish their jobs
		try {
			manager.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
