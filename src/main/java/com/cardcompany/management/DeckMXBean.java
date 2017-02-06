package com.cardcompany.management;

/**
 * Expose the last shuffled time through JMX.
 * 
 * @author selvi

 *
 */
public interface DeckMXBean {

	public long timeSinceShuffledInSeconds();

}
