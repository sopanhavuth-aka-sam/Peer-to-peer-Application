package peer_to_peer_application;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class Token implements Serializable{
	
	private static AtomicInteger counter;
	
	/**
	 * Constructor
	 */
	public Token() {
		counter = new AtomicInteger(0);
	}
	
	/**
	 * This method increase the counter by 1
	 */
	public int incCounter() {
		return counter.incrementAndGet();
	}
	
	/**
	 * This method return the current value of counter
	 * @return counter record the number of worker that have accessed this token
	 */
	public int getCounter() {
		return counter.get();
	}
	
    private void writeObject(ObjectOutputStream oos)
            throws IOException 
        {
            oos.defaultWriteObject();
            oos.writeObject(new AtomicInteger(counter.get()));
        }

        private void readObject(ObjectInputStream ois)
        throws ClassNotFoundException, IOException 
        {
            ois.defaultReadObject();
            counter = (AtomicInteger)ois.readObject();
        }
}
