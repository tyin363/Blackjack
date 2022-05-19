package nz.ac.auckland.se281.a3.bot;

import nz.ac.auckland.se281.a3.Hand;
import nz.ac.auckland.se281.a3.Player;

/**
 * you should change this class for TASK 1
 */
public class Bot extends Player {

	// Instance fields
	private BotStrategy strategy;

	/**
	 * Constructor of bot that creates a Bot instance with its given name and
	 * strategy
	 * 
	 * @param name     name of Bot
	 * @param strategy strategy of Bot
	 */

	public Bot(String name, BotStrategy strategy) {
		super(name);
		this.strategy = strategy;
	}

	@Override
	public Action decideAction(Hand hand) {
		return strategy.decideAction(hand);
	}

	@Override
	public int makeABet() {
		return strategy.makeABet();
	}

}
