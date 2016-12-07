package cecs327_assignment5;

//TestBench for one host
public class TestBench {
	
	private static TokenManager manager;
	
	private static Worker[] workers;
	
	private final static int WORKER_NUM = 5;
	
	public static void main(String[] args) {
		//initialize manager and workers object
		manager = new TokenManager();
		workers = new Worker[WORKER_NUM];
		//create a token in manager
		manager.createToken();
		//link workers to manager
		for(int i = 0; i < WORKER_NUM; i++ ) {
			workers[i] = new Worker(manager);
		}
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
		//wait for manager to finish their jobs
		try {
			manager.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
