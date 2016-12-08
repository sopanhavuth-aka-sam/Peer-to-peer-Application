package peer_to_peer_application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class TokenManager extends Thread{
	
	private Token token;
	
	//atomic boolean for this one, when implement peer-to-peer networking
	private AtomicBoolean tokenIsLocal;
	
	private Queue<Message> messageQueue;
	
	//client networking components
	private Socket clientSocket;
	private ObjectInputStream clientRead;
	private ObjectOutputStream clientWrite;
	
	/**
	 * Constructor
	 */
	public TokenManager() {
		this.messageQueue = new ConcurrentLinkedQueue<Message>();
		this.tokenIsLocal = new AtomicBoolean(false);
	}
	
	public void initClientComponent(int portNum) {
		try{
			//initialize client components
			System.out.print("Requesting Connection...");
			clientSocket = new Socket("localhost", portNum);
//			clientRead = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			clientWrite = new ObjectOutputStream(clientSocket.getOutputStream());
			clientRead = new ObjectInputStream(clientSocket.getInputStream());
//			clientWrite = new PrintStream(clientSocket.getOutputStream());
//			clientWrite = new ObjectOutputStream(clientSocket.getOutputStream());
//			clientWrite.flush();
			System.out.println("Connected.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Send token request to the other TokenManager via NetworkMonitor
	 */
	public void sendTokenRequest() {
		//create remote request message
		Message m = new Message(1, -1);
		//send Message through socket
//		clientWrite.print(m);
		try {
			clientWrite.writeObject(m);
			clientWrite.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Send token to the other TokenManager via NetworkMonitor
	 * and set AtomicBoolean to false
	 */
	public void sendToken() {
		this.tokenIsLocal.compareAndSet(true, false);
//		clientWrite.print(this.token);
		try {
			clientWrite.writeObject(this.token);
			clientWrite.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * This method create a new token object and put it in this token manager
	 * we dont create token in constructor, because only one token is needed between multiple
	 * token managers
	 */
	public void createToken() {
		this.token = new Token();
		this.tokenIsLocal.set(true);
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
					//give token to Worker
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
	public void workerReturnToken(Token t) {
		this.token = t;
	}
	
	/**
	 * This method is called by NetworkMonitor to return token back to manager
	 * that was send by the other TokenManager
	 * @param t
	 */
	public void tokenManagerReturnToken(Token t) {
		this.token = t;
		this.tokenIsLocal.compareAndSet(false, true);
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
				//if this TokenManager doesn't have token -> send request message and wait
				if(!tokenIsLocal.get()) {
					sendTokenRequest();
				}
				//wait for token
				while(!tokenIsLocal.get());
				//de-queue message
				Message m = messageQueue.poll();
				//if it is local request -> give token to local worker
				if(m.isLocal()) {
//					System.out.println("local");
					giveToken(m.getRequesterId());
				}
				//if it is remote request -> send token to other token manager
				else if(m.isRemote()) {
					//send token to remote token manager
					sendToken();
				}
				else {
					System.out.println("Error message type");
				}
			}
		}
	}//end run()
	
	
}
