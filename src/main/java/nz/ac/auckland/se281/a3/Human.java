package nz.ac.auckland.se281.a3;

import java.util.InputMismatchException;

/**
 * you cannot modify this class
 */
public class Human extends Player {

	public Human(String name) {
		super(name);
	}

	@Override
	public Action decideAction(Hand hand) {
		if (hand.countAces() > 0) {
			System.out.println("Remember that an ACE can have rank either 1 or 11");
		}
		System.out.println("Decide your action (hit or hold)");
		String res = Main.scanner.next();
		while (!res.equals("hit") && !res.equals("hold")) {
			System.out.println("please type either \"hit\" or \"hold\"");
			res = Main.scanner.next();
		}
		return res.equals("hit") ? Action.HIT : Action.HOLD;
	}

	@Override
	public int makeABet() {
		System.out.println("How much do you want to bet?");
		int bet = 0;
		do {
			try {
				System.out.println("Please enter a number > 0 and <= 100");
				bet = Integer.valueOf(Main.scanner.next());
			} catch (InputMismatchException | NumberFormatException nf) {
			}
		} while (bet <= 0 || bet > 100);
		return bet;
	}

}
