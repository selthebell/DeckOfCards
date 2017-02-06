package com.cardcompany.card;

/**
 * Immutable object which is thread safe.
 * 
 * @param clubs
 * @param aRank
 */
public class CardImpl implements ICard {

	private SuitEnum suit;
	private RankEnum rank;

	public CardImpl(SuitEnum clubs, RankEnum aRank) {
		this.suit = clubs;
		this.rank = aRank;
	}

	@Override
	public SuitEnum getSuit() {
		return this.suit;
	}

	@Override
	public RankEnum getRank() {
		return this.rank;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Card [suit=");
		builder.append(suit);
		builder.append(", rank=");
		builder.append(rank);
		builder.append("]");
		return builder.toString();
	}

}
