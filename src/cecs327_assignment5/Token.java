package cecs327_assignment5;

import java.util.concurrent.atomic.AtomicInteger;

public class Token {
	
	private AtomicInteger counter;
	
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
}
