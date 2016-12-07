package cecs327_assignment5;

public class Message {
	
	private int type;
	
	private final int LOCAL_REQUEST = 0, REMOTE_REQUEST = 1;
	
	private long requesterId;
	
	/**
	 * Constructor
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
