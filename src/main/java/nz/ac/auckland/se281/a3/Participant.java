package nz.ac.auckland.se281.a3;

/**
 * you cannot modify this class
 */
public abstract class Participant {

	public enum Action {
		HIT, HOLD
	}

	private String name;
	private Hand hand;

	public Participant(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public Hand getHand() {
		return hand;
	}

	protected Hand createHand() {
		if (this instanceof Player) {
			int bet = ((Player) this).makeABet();
			System.out.println(name + " bet " + bet);
			return new Hand(bet);
		}
		return new Hand();
	}

	/**
	 * do not change this method
	 *
	 * @param deck
	 * @param round
	 */
	public void play(Deck deck, int round) {
		System.out.println("==================");
		System.out.println("Round " + round + ": " + name + " is playing");
		hand = createHand();
		hand.addCard(deck.draw());
		hand.addCard(deck.draw());
		System.out.println("the initial two cards are");
		hand.print();
		System.out.println("Round " + round + ": " + name + "'s score is: " + hand.getScore());
		if (!hand.isBlackJack()) { // no point to ask for hit or hold if is BlackJack
			int i = 1;
			Participant.Action decision = decideAction(hand);
			System.out.println("Round " + round + ": " + name + " #" + i + " " + decision.name());
			while (decision == Participant.Action.HIT) {
				i++;
				hand.addCard(deck.draw());
				hand.print();
				System.out.println("Round " + round + ": " + name + "'s score is: " + hand.getScore());
				if (hand.is21() || hand.isBust()) {
					break;
				}
				decision = decideAction(hand);
				System.out.println("Round " + round + ": " + name + " #" + i + " " + decision.name());
			}
		}
		hand.printOutcome();
		pressEnterKeyToContinue();
	}

	private void pressEnterKeyToContinue() {
		System.out.println("Press Enter key to continue...");
		Main.scanner.nextLine();
	}

	public abstract Action decideAction(Hand hand);

}
