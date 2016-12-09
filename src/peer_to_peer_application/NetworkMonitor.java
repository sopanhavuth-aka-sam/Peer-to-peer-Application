package peer_to_peer_application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class NetworkMonitor extends Thread{
	
	private TokenManager manager;
	
	//Peer-to-peer components (This class can be both a server and a client)
	//server networking components
	private ServerSocket server;
	private Socket serverSocket;
//	private BufferedReader serverRead;
	private ObjectInputStream serverRead;
	private ObjectOutputStream serverWrite;
	//client networking components
//	private Socket clientSocket;
//	private BufferedReader clientRead;
//	private PrintStream clientWrite;
	
	/**
	 * Constructor
	 */
	public NetworkMonitor(TokenManager m, int portNum) {
		this.manager = m;
		try {
			//initialize client components
//			System.out.print("Requesting Connection... ");
//			clientSocket = new Socket("localhost", clientPort);
//			clientRead = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//			clientWrite = new PrintStream(clientSocket.getOutputStream());
//			System.out.println("Connected.");
			
			//initialize server components
			server = new ServerSocket(portNum);
			System.out.print("Waiting...");
			serverSocket = server.accept();
//			serverRead = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
			serverWrite = new ObjectOutputStream(serverSocket.getOutputStream());
			serverRead = new ObjectInputStream(serverSocket.getInputStream());
			System.out.println("Connected.");
			//we're not using serverWrite??
//			serverWrite = new PrintStream(serverSocket.getOutputStream());
//			serverWrite = new ObjectOutputStream(serverSocket.getOutputStream());
			System.out.println("Connected.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * wait for request from other NetworkMonitor
	 */
	public void run() {
		while(true) {
			try {
				//read object that was sent
				Object obj = serverRead.readObject();
				
				//if obj is message
				if(obj instanceof Message) {
					System.out.println("message received");
					//add message to TokenManager's queue
					manager.addRequest((Message) obj);
				}
				//if obj is token
				else if(obj instanceof Token) {
					System.out.println("token received");
					//give token to TokenManager
					manager.tokenManagerReturnToken((Token) obj);
				}
				//if unexpected obj appear
				else {
					System.out.println("Wrong type of object received");
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}//end run()
}
