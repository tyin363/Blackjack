package nz.ac.auckland.se281.a3.dealer;

import java.util.ArrayList;
import java.util.List;

import nz.ac.auckland.se281.a3.BlackJack;
import nz.ac.auckland.se281.a3.Hand;
import nz.ac.auckland.se281.a3.Participant.Action;
import nz.ac.auckland.se281.a3.Player;

public class TargetHighestBidderStrategy implements DealerStrategy {

	// Instance fields
	private BlackJack game;

	/**
	 * Constructor of the Target Highest Bidder strategy that creates an instance of
	 * TargetHighestBidderStrategy
	 * 
	 * @param game BlackJack game
	 */
	public TargetHighestBidderStrategy(BlackJack game) {
		this.game = game;
	}

	/**
	 * Decides the action of the dealer in the current round of BlackJack when using
	 * Target Highest Bidder strategy and returns the action
	 * 
	 * @param hand hand of dealer
	 * @return dealerAction action of dealer
	 */

	@Override
	public Action decideAction(Hand hand) {
		Player highestBidder = getHighestBidder();

		// Gets the highest bidder's hand
		Hand highestBidderHand = highestBidder.getHand();
		Action dealerAction = null;

		// If player busts, dealer holds
		if (highestBidderHand.isBust()) {
			dealerAction = Action.HOLD;
		}

		// Determines dealer action when highest bidder has score of less than 21 and
		// does not have blackjack
		else if (highestBidderHand.getScore() <= 21 && !highestBidderHand.isBlackJack()) {
			if (hand.getScore() < highestBidderHand.getScore()) {
				dealerAction = Action.HIT;
			} else {
				dealerAction = Action.HOLD;
			}
		}
		// Determines dealer action when highest bidder has blackjack and dealer does
		// not have blackjack
		else if (highestBidderHand.isBlackJack()) {
			if (hand.getScore() >= 17) {
				dealerAction = Action.HOLD;
			} else {
				dealerAction = Action.HIT;
			}
		}

		return dealerAction;
	}

	/**
	 * Determines who the highest bidder in the current round of BlackJack is and
	 * returns that player
	 * 
	 * @return currentHighestBidder the player with the highest bid
	 */
	public Player getHighestBidder() {
		List<Player> reversePlayers = reverseArrayList(game.getPlayers());
		Player currentHighestBidder = null;
		int currentHighestBid = 0;

		// Cycles through each player in the reversed player list
		for (Player player : reversePlayers) {

			// Determines who is the highest bidder
			if (player.getHand().getBet() >= currentHighestBid) {
				currentHighestBid = player.getHand().getBet();
				currentHighestBidder = player;
			}
		}
		return currentHighestBidder;

	}

	// Code adapted from https://www.geeksforgeeks.org/reverse-an-arraylist-in-java/

	/**
	 * Takes in the list of players in the BlackJack game and reverses the list and
	 * returns the reversed list
	 * 
	 * @param alist player list
	 * @return revArrayList reversed player list
	 */
	public List<Player> reverseArrayList(List<Player> alist) {
		// List for storing reversed elements
		List<Player> revArrayList = new ArrayList<Player>();
		for (int i = alist.size() - 1; i >= 0; i--) {

			// Append the elements in reverse order
			revArrayList.add(alist.get(i));
		}

		// Return the reversed list
		return revArrayList;
	}

}
