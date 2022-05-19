package nz.ac.auckland.se281.a3.dealer;

import nz.ac.auckland.se281.a3.Hand;
import nz.ac.auckland.se281.a3.Participant.Action;

public interface DealerStrategy {

	/**
	 * Decides action of dealer in the current round of BlackJack and returns the
	 * action
	 * 
	 * @param hand of dealer
	 * @return action of dealer
	 */
	Action decideAction(Hand hand);

}
