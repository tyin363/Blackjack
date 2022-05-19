package nz.ac.auckland.se281.a3;

/**
 * You cannot change this class
 */
public class Card {

	public enum Rank {
		ACE(11), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9), TEN(10), JACK(10), QUEEN(10),
		KING(10);

		public final int value;

		Rank(int value) {
			this.value = value;
		}

	}

	public enum Suit {
		HEARTS, DIAMONDS, SPADES, CLUBS
	}

	private Rank rank;
	private Suit suit;

	public Card(Rank face, Suit suit) {
		this.rank = face;
		this.suit = suit;
	}

	public Rank getRank() {
		return rank;
	}

	public Suit getSuit() {
		return suit;
	}

	public static final char[][] template = { "\u250c\u2500\u2500\u2500\u2500\u2500\u2510".toCharArray(),
			"\u2502?    \u2502".toCharArray(), "\u2502  *  \u2502".toCharArray(), "\u2502    ?\u2502".toCharArray(),
			"\u2514\u2500\u2500\u2500\u2500\u2500\u2518".toCharArray() };

	private char convertIntToChar(int number) {
		// adding plus 0 is a trick to get the char value out of an integer
		return (char) (number + '0');
	}

	public char[][] getCardToPrint() {
		// lets reuse template, do not create additional objects
		// the 10 card is a special case because 10 has 2 digits
		if (rank == Rank.TEN) {
			// fill the template
			template[1][1] = convertIntToChar(1);
			template[1][2] = convertIntToChar(0);
			template[3][4] = convertIntToChar(1);
			template[3][5] = convertIntToChar(0);
		} else {
			// lets clean the template in case we just printed a number 10
			template[1][2] = ' ';
			template[3][4] = ' ';
			// fill the template
			template[1][1] = this.getPrintableRank();
			template[3][5] = this.getPrintableRank();
		}
		template[2][3] = this.getPrintableSuit();
		return template;
	}

	private char getPrintableSuit() {
		switch (suit) {
		case HEARTS:
			return '\u2665';
		case DIAMONDS:
			return '\u2666';
		case SPADES:
			return '\u2660';
		case CLUBS:
			return '\u2663';
		default:
			// execute here should be impossible since SUIT has only those 4 possible values
			throw new RuntimeException("something is incredibly wrong");
		}
	}

	private char getPrintableRank() {
		switch (rank) {
		case ACE:
			return 'A';
		case JACK:
			return 'J';
		case QUEEN:
			return 'Q';
		case KING:
			return 'K';
		default:
			return convertIntToChar(rank.value);
		}
	}

}
