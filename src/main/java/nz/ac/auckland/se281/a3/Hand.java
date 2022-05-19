package nz.ac.auckland.se281.a3;

import java.util.ArrayList;
import java.util.List;

/**
 * you cannot modify this class
 *
 */
public class Hand {

	private List<Card> cards;
	private int bet;
	private int score;

	public Hand(int bet) {
		this.bet = bet;
		cards = new ArrayList<>();
		score = 0;
	}

	public Hand() {
		this(0);
	}

	public int getScore() {
		return score;
	}

	public List<Card> getCards() {
		return cards;
	}

	public int getBet() {
		return bet;
	}

	public void addCard(Card c) {
		cards.add(c);
		// every time I add a card I update the score
		score = score + c.getRank().value;
		checkIfAcesShouldBeOne();
	}

	public int countAces() {
		int count = 0;
		for (Card c : cards) {
			if (c.getRank() == Card.Rank.ACE) {
				count++;
			}
		}
		return count;
	}

	private void checkIfAcesShouldBeOne() {
		if (score > 21) {
			int numAces = countAces();
			// see if there are opportunities to make Aces to have score 1
			if (numAces > 0) {
				score = 0;
				// we need to recount from scratch because some ACEs might be counted as 11
				for (Card card : cards) {
					score = score + card.getRank().value;
				}
				while (score > 21 && numAces > 0) {
					// ACES can have score 11 or 1
					score = score - 10;
					numAces--;
				}
			}
		}
		// note that the score will be update after the method ends
	}

	public boolean isBust() {
		return getScore() > 21;
	}

	public boolean isBlackJack() {
		return is21() && cards.size() == 2;
	}

	public boolean is21() {
		return score == 21;
	}

	public void printOutcome() {
		if (is21()) {
			if (isBlackJack()) {
				System.out.println("Black Jack! :D ");
			} else {
				System.out.println("21! :) ");
			}
		} else if (isBust()) {
			System.out.println("Busted! :( ");
		}

	}

	private void addNumberWhiteSpace(StringBuilder sb, int num) {
		for (int i = 0; i < num; i++) {
			sb.append(" ");
		}
	}

	public void print() {
		System.out.println(this);
	}

	public String toString() {
		StringBuilder resultBuilder = new StringBuilder();
		List<StringBuilder> listSb = new ArrayList<>();
		// + 1 because we also want to print the name of the card
		for (int i = 0; i < Card.template[0].length + 1; i++) {
			listSb.add(new StringBuilder());
		}
		for (Card card : cards) {
			String textCard = card.getRank() + " " + card.getSuit();
			int numWhiteSpaces = (((textCard.length() - 7)) / 2) + 1;
			listSb.get(0).append(textCard);
			listSb.get(0).append("  -  ");
			int i = 1;
			for (char[] line : card.getCardToPrint()) {
				addNumberWhiteSpace(listSb.get(i), numWhiteSpaces);
				listSb.get(i).append(line);
				addNumberWhiteSpace(listSb.get(i), "  -  ".length() + numWhiteSpaces / 2);
				i++;
			}
		}
		for (int i = 0; i < Card.template[0].length + 1; i++) {
			resultBuilder.append(listSb.get(i).toString());
			resultBuilder.append(System.getProperty("line.separator"));
		}
		return resultBuilder.toString();
	}
}
