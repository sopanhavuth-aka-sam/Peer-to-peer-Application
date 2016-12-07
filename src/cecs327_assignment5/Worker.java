package cecs327_assignment5;

import java.util.concurrent.atomic.AtomicBoolean;

public class Worker extends Thread{
	
	private TokenManager manager;
	
//	private boolean haveToken;
	
	private AtomicBoolean haveToken;
	
	private Token token;
	
	/**
	 * Constructor
	 * @param manager
	 */
	public Worker(TokenManager m) {
		this.manager = m;
//		this.haveToken = false;
		this.haveToken = new AtomicBoolean(false);
		this.token = null;
	}
	
	public void requestToken() {
		/* add request message to token manager
		 * 0: local request (Worker only make local request,
		 * remote request is handle by manager)
		 */
		long id = Thread.currentThread().getId();
		Message request = new Message(0, id); 
		manager.addRequest(request);
//		System.out.println("token send");
	}
	
	/**
	 * This method is called by thread manager when it want to give token to
	 * this Worker
	 * @param t
	 */
	public void receiveToken(Token t) {
		System.out.println("have token");
		this.token = t;
		this.haveToken.compareAndSet(false, true);
	}
	
	public synchronized void displayWorkerCounter(long id, int counter) {
		System.out.printf("Worker %d -> counter %d\n", id, counter);
	}
	
	public void run() {
		long id = Thread.currentThread().getId();
		System.out.printf("Worker %d started\n", id);
		for(int i = 0; i < 100; i++) {
			//request token
			requestToken();
			//loop until this worker have accessed to token (given by token manager)
			while(!haveToken.get());
			//use token: output counter value and increment value
//			System.out.printf("Worker %d -> counter %d\n", id, token.getCounter());
			displayWorkerCounter(id, token.incCounter());
			//token.incCounter();
			//return token to manager
			manager.returnToken(token);
			//sleep for 50ms
			try {
				//sleep is a static method. only this thread will be put to sleep
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
