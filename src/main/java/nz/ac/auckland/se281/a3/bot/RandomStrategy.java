package nz.ac.auckland.se281.a3.bot;

import java.util.Random;

import nz.ac.auckland.se281.a3.Hand;
import nz.ac.auckland.se281.a3.Participant.Action;

public class RandomStrategy implements BotStrategy {

	/**
	 * Decide the action of a bot in the current round of BlackJack when using
	 * Random strategy
	 * 
	 * @param hand BlackJack hand of bot
	 * @return action of bot
	 */
	@Override
	public Action decideAction(Hand hand) {
		Random random = new Random();

		// Generates a random number out of 0 or 1
		int numRandom = random.nextInt(2);

		// Sets action to hold or hit depending on the random number
		if (numRandom == 0) {
			return Action.HOLD;
		} else {
			return Action.HIT;
		}
	}

	/**
	 * Makes a bet for a bot in the current round of BlackJack when using Random
	 * strategy
	 * 
	 * @return bet size of bot
	 */
	@Override
	public int makeABet() {
		Random random = new Random();
		int bet = random.nextInt(1, 101);
		return bet;
	}

}
