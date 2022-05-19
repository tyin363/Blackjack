package nz.ac.auckland.se281.a3.dealer;

import nz.ac.auckland.se281.a3.Hand;
import nz.ac.auckland.se281.a3.Participant;

/**
 * 
 * You should change this class for Task 2
 *
 */
public class Dealer extends Participant {

	// Instance fields
	private DealerStrategy strategy;

	/**
	 * Constructor of dealer that creates a Dealer instance with its given name and
	 * strategy
	 * 
	 * @param name     name of dealer
	 * @param strategy strategy of dealer
	 */
	public Dealer(String name, DealerStrategy strategy) {
		super(name);
		this.strategy = strategy;

	}

	public void setStrategy(DealerStrategy strategy) {
		this.strategy = strategy;
	}

	@Override
	public Action decideAction(Hand hand) {
		return strategy.decideAction(hand);
	}

}
