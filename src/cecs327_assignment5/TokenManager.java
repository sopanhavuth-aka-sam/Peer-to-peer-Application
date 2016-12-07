package cecs327_assignment5;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TokenManager extends Thread{
	
	private Token token;
	
	//atomic boolean for this one, when implement peer-to-peer networking
	private boolean TokenIsLocal;
	
	private Queue<Message> messageQueue;
	
	/**
	 * Constructor
	 */
	public TokenManager() {
		messageQueue = new ConcurrentLinkedQueue<Message>();
	}
	
	/**
	 * This method create a new token object and put it in this token manager
	 * we dont create token in constructor, because only one token is needed between multiple
	 * token managers
	 */
	public void createToken() {
		this.token = new Token();
		this.TokenIsLocal = true;
	}
	
	/**
	 * This method add request message(from Worker) to the queue
	 * @param m message to be added to the queue
	 */
	public void addRequest(Message m) {
//		System.out.println("request receive");
		boolean success = messageQueue.offer(m);
//		System.out.println("request finished? success: " + success);
//		System.out.println("queue size: " + messageQueue.size());
	}
	
	/**
	 * This method give token to Worker object using Worker's ThreadID
	 * @param workerID
	 */
	public void giveToken(long workerID) {
		for(Thread t: Thread.getAllStackTraces().keySet()) {
			if(t.getId() == workerID) {
				if(t instanceof Worker) {
					Worker w = (Worker) t;
					w.receiveToken(this.token);
				}
			}
		}
	}
	
	/**
	 * This method is called by Worker to return token back to manager,
	 * after the Worker complete its job
	 * @param t
	 */
	public void returnToken(Token t) {
		this.token = t;
	}
	
	public void run() {
		while(true) {
			if(messageQueue.isEmpty()) {
				System.out.println("Queue is empty");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			else {
				System.out.println("message dequeue");
				//de-queue message
				Message m = messageQueue.poll();
				//if it is local request -> give token to local worker
				if(m.isLocal()) {
					System.out.println("local");
					giveToken(m.getRequesterId());
				}
				//if it is remote request -> send token to other token manager
				else if(m.isRemote()) {
					//send token to remote token manager: not yet implemented
				}
				else {
					System.out.println("Error message type");
				}
			}
		}
	}
}
