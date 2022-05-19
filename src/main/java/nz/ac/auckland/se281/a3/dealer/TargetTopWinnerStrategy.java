package nz.ac.auckland.se281.a3.dealer;

import java.util.ArrayList;
import java.util.List;

import nz.ac.auckland.se281.a3.BlackJack;
import nz.ac.auckland.se281.a3.Hand;
import nz.ac.auckland.se281.a3.Participant.Action;
import nz.ac.auckland.se281.a3.Player;

public class TargetTopWinnerStrategy implements DealerStrategy {

	// Instance fields
	private BlackJack game;

	/**
	 * Constructor of the Target Top Winner strategy that creates an instance of
	 * TargetTopWinnerStrategy
	 * 
	 * @param game BlackJack game
	 */
	public TargetTopWinnerStrategy(BlackJack game) {
		this.game = game;
	}

	/**
	 * Decides the action of the dealer in the current round of BlackJack when using
	 * Target Top Winner strategy and returns the action
	 * 
	 * @param hand hand of dealer
	 * @return dealerAction action of dealer
	 */
	@Override
	public Action decideAction(Hand hand) {
		Player topWinner = getTopWinner();

		// Gets the top winner's hand
		Hand topWinnerHand = topWinner.getHand();
		Action dealerAction = null;

		// If player busts, dealer holds
		if (topWinnerHand.isBust()) {
			dealerAction = Action.HOLD;
		}

		// Determines dealer action when highest bidder has score of less than 21 and
		// does not have blackjack
		else if (topWinnerHand.getScore() <= 21 && !topWinnerHand.isBlackJack()) {
			if (hand.getScore() < topWinnerHand.getScore()) {
				dealerAction = Action.HIT;
			} else {
				dealerAction = Action.HOLD;
			}
		}
		// Determines dealer action when highest bidder has blackjack and dealer does
		// not have blackjack
		else if (topWinnerHand.isBlackJack()) {
			if (hand.getScore() >= 17) {
				dealerAction = Action.HOLD;
			} else {
				dealerAction = Action.HIT;
			}
		}

		return dealerAction;
	}

	/**
	 * Determines who the top winner in the current round of BlackJack is and
	 * returns that player
	 * 
	 * @return currentTopWinner the player with the highest bid
	 */
	private Player getTopWinner() {
		List<Player> reversePlayers = reverseArrayList(game.getPlayers());
		Player currentTopWinner = null;
		int currentTopWins = Integer.MIN_VALUE;

		// Cycle through each player in the reversed player list
		for (Player player : reversePlayers) {

			// Determines who is the top winner
			if (player.getNetWins() >= currentTopWins) {
				currentTopWins = player.getNetWins();
				currentTopWinner = player;
			}
		}
		return currentTopWinner;

	}

	// Code adapted from https://www.geeksforgeeks.org/reverse-an-arraylist-in-java/

	/**
	 * Takes in the list of players in the BlackJack game and reverses the list and
	 * returns the reversed list
	 * 
	 * @param alist player list
	 * @return revArrayList reversed player list
	 */
	// https://www.geeksforgeeks.org/reverse-an-arraylist-in-java/
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
