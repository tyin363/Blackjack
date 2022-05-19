package nz.ac.auckland.se281.a3.bot;

import nz.ac.auckland.se281.a3.Hand;
import nz.ac.auckland.se281.a3.Participant.Action;

public interface BotStrategy {
	/**
	 * Decides the action of a bot in the current round of BlackJack and returns the
	 * action
	 * 
	 * @param hand hand of bot
	 * @return action of bot
	 */
	Action decideAction(Hand hand);

	/**
	 * Makes a bet for the bot in the current round of BlackJack and returns the bet
	 * size
	 * 
	 * @return bet size
	 */
	int makeABet();

}
