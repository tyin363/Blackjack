package nz.ac.auckland.se281.a3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * you cannot modify this class
 */
public class Deck {

	private final List<Card> deck;
	private int position;

	public Deck() {
		deck = new ArrayList<>(52);
		create();
		newDeck();
	}

	/**
	 * this is for testing purposes
	 */
	public Deck(Card... cards) {
		deck = Arrays.asList(cards);
		position = 0;
	}

	private void shuffle() {
		Collections.shuffle(deck);
	}

	public Card draw() {
		if (deck.size() == position) {
			// we finished the card let's create a new deck
			newDeck();
		}
		return deck.get(position++);
	}

	private void create() {
		for (Card.Suit suit : Card.Suit.values()) {
			for (Card.Rank rank : Card.Rank.values()) {
				deck.add(new Card(rank, suit));
			}
		}
	}

	public void newDeck() {
		position = 0;
		shuffle();
	}

}
