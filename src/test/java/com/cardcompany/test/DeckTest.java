package com.cardcompany.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.cardcompany.Deck;
import com.cardcompany.card.ICard;

/**
 * Test Suite to test the deck.
 * 
 * @author selvi

 *
 */
public class DeckTest {
	
	Deck myDeck;
	Deck otherDeck;
	
	@Before
	public void setUp(){
		myDeck = new Deck();
		otherDeck = new Deck();
	}

	@Test
	public void testIfDeckRandomized() {
		ICard cardFromFirstDeck = myDeck.dealOneCard();
		ICard cardFromSecondDeck = otherDeck.dealOneCard();

		Assert.assertNotEquals(cardFromFirstDeck, cardFromSecondDeck);
	}
	
	@Test(expected= IllegalStateException.class)
	public void testUpperBoundaryCondition() {
		for (int i=0; i< 53; myDeck.dealOneCard(),i++);
	}

}
