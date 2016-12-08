package peer_to_peer_application;

import java.io.Serializable;

public class Message implements Serializable {
	
	private int type;
	
	private final int LOCAL_REQUEST = 0, REMOTE_REQUEST = 1;
	
	private long requesterId;
	
	/**
	 * Constructor
	 * id should b -1 if this is a remote request
	 * id should be Worker's thread id if this is a local request
	 */
	public Message(int type, long id) {
		if(type == LOCAL_REQUEST || type == REMOTE_REQUEST) {
			this.type = type;
		}
		else {
			System.out.println("Wrong type");
		}
		this.requesterId = id;
	}
	
	public boolean isLocal() {
		if(this.type == LOCAL_REQUEST) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean isRemote() {
		if(this.type == REMOTE_REQUEST) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public long getRequesterId() {
		return this.requesterId;
	}
}
