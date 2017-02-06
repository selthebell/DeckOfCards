package com.cardcompany.card;

/**
 * Enum representing the Suit.
 * 
 * @author selvi

 *
 */
public enum SuitEnum {
	Clubs("C"), Diamonds("D"), Hearts("H"), Spades("S");

	private String code;

	private SuitEnum(String s) {
		this.code = s;
	}

	public String getCode() {
		return this.code;
	}
}
