package peer_to_peer_application;

import java.util.concurrent.atomic.AtomicBoolean;

public class Worker extends Thread{
	
	private TokenManager manager;
	
	private AtomicBoolean haveToken;
	
	private Token token;
	
	/**
	 * Constructor
	 * @param manager
	 */
	public Worker(TokenManager m, String threadName) {
		this.manager = m;
		this.haveToken = new AtomicBoolean(false);
		this.token = null;
		this.setName(threadName);
	}
	
	public void requestToken() {
		/* add request message to token manager
		 * 0: local request (Worker only make local request,
		 * remote request is handle by manager)
		 */
//		long id = Thread.currentThread().getId();
		String threadName = Thread.currentThread().getName();
		Message request = new Message(0, threadName); 
		manager.addRequest(request);
//		System.out.println("token send");
	}
	
	/**
	 * This method is called by thread manager when it want to give token to
	 * this Worker
	 * @param t
	 */
	public void receiveToken(Token t) {
		this.token = t;
		this.haveToken.set(true);
		System.out.println(this.getName() + " has received token");
	}
	
	public synchronized void displayWorkerCounter(String name, int counter) {
		System.out.printf("%s -> counter %d\n", name, counter);
	}
	
	public void run() {
		String workerName = Thread.currentThread().getName();
		System.out.printf("Worker %s started\n", workerName);
		for(int i = 0; i < 100; i++) {
			//request token
			requestToken();
			//loop until this worker have accessed to token (given by token manager)
			while(!haveToken.get() || this.token == null);
			//try sleep
			try {
				this.sleep(5);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//use token: output counter value and increment value
			displayWorkerCounter(workerName, token.incCounter());
			//return token to manager
			manager.workerReturnToken(token);
			//set Token to null and haveToken to false
			this.token = null;
			this.haveToken.set(false);
			//sleep for 50ms
			try {
				//sleep is a static method. only this thread will be put to sleep
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}//end run()
}
