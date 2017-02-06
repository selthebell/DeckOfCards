package com.cardcompany;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.junit.Assert;

import com.cardcompany.card.CardImpl;
import com.cardcompany.card.ICard;
import com.cardcompany.card.RankEnum;
import com.cardcompany.card.SuitEnum;
import com.cardcompany.management.DeckMXBean;

/**
 * Represents an in-memory shuffled deck which returns a random card on request.
 * 
 * @author selvi

 *
 */
public class Deck implements DeckMXBean {
	// Max count of cards that is possible.
	private static final int COUNT_MAX = 52;
	private static final int TO_SECONDS = 1000;

	private List<ICard> masterListOfCards = new ArrayList<ICard>(COUNT_MAX);

	// Mbean related
	private static long shuffledTime;
	private MBeanServer mbs;
	private static ObjectName name;

	// Logger reference
	private static Logger logger;

	static {
		logger = Logger.getLogger(Deck.class.getName());
		try {
			name = new ObjectName("com.cardcompany.mbeans:type=Deck");
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Failed to create JMX Object", e);
		}
	}

	public Deck() {
		initJmx();
		initADeck();
		shuffle();
	}

	private void shuffle() {
		for (int i = 0; i < COUNT_MAX; i++) {
			int random = getRandomPosition();
			swap(i, random);
		}
		shuffledTime = getLocalTimeInMillis();
		Collections.unmodifiableList(this.masterListOfCards);
		logger.info("Shuffled deck " + this.masterListOfCards.toString());
	}

	private void swap(int originalIndex, int newPosition) {
		ICard card = this.masterListOfCards.remove(originalIndex);
		this.masterListOfCards.add(newPosition, card);
	}

	/**
	 * Returns a random card from the deck. After the deck runs out of cards,
	 * throws {@link IllegalStateException}
	 * 
	 * @return {@link ICard}
	 * @throws IllegalStateException
	 */
	public synchronized ICard dealOneCard() throws IllegalStateException {
		Assert.assertNotNull("Shuffled deck cannot be null.",
				this.masterListOfCards);
		if (this.masterListOfCards.size() > 0) {
			return this.masterListOfCards
					.remove(this.masterListOfCards.size() - 1);
		} else {
			throw new IllegalStateException("Deck is empty");
		}
	}

	private void initADeck() {
		for (RankEnum rank : RankEnum.values()) {
			for (SuitEnum suit : SuitEnum.values()) {
				CardImpl card = new CardImpl(suit, rank);
				this.masterListOfCards.add(card);
			}
		}
		logger.info("Init deck: " + this.masterListOfCards.toString());
	}

	private int getRandomPosition() {
		return getRandomPosition(COUNT_MAX);
	}

	private int getRandomPosition(int randomFactor) {
		return new Random().nextInt(randomFactor);
	}

	@Override
	public long timeSinceShuffledInSeconds() {
		long now = getLocalTimeInMillis();
		return (now - shuffledTime) / TO_SECONDS;
	}

	private long getLocalTimeInMillis() {
		return Calendar.getInstance().getTimeInMillis();
	}

	private void initJmx() {
		try {
			Assert.assertNotNull("Object name cannot be null.", name);
			registerDeckMbean();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Failed to register JMX Object", e);
		}

	}

	// Try registering an mbean. Since we do not have a destructor, we have to
	// unregister the mbean to get rid of the old one.
	private void registerDeckMbean() {
		try {
			mbs = ManagementFactory.getPlatformMBeanServer();
			mbs.registerMBean(this, name);
		} catch (InstanceAlreadyExistsException iae) {
			try {
				mbs.unregisterMBean(name);
				mbs.registerMBean(this, name);
			} catch (Exception e) {
				logger.log(Level.SEVERE, "Unable to register mbean.", e);
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Unable to register mbean.", e);
		}
	}

}
