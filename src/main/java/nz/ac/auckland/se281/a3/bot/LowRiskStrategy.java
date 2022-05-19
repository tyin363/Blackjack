package nz.ac.auckland.se281.a3.bot;

import java.util.Random;

import nz.ac.auckland.se281.a3.Hand;
import nz.ac.auckland.se281.a3.Participant.Action;

public class LowRiskStrategy implements BotStrategy {

	/**
	 * Decides the action of a bot in the current round of BlackJack when using Low
	 * Risk strategy
	 * 
	 * @param hand BlackJack hand of bot
	 * @return action of bot
	 */
	@Override
	public Action decideAction(Hand hand) {
		if (hand.getScore() >= 17) {
			return Action.HOLD;
		} else {
			return Action.HIT;
		}
	}

	/**
	 * Makes a bet for a bot in the current round of BlackJack when using Low Risk
	 * strategy
	 * 
	 * @return bet size of bot
	 */
	@Override
	public int makeABet() {
		Random random = new Random();
		int bet = random.nextInt(10, 51);
		return bet;
	}

}
