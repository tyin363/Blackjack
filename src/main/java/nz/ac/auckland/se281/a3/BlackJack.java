package nz.ac.auckland.se281.a3;

import java.util.ArrayList;
import java.util.List;

import nz.ac.auckland.se281.a3.bot.Bot;
import nz.ac.auckland.se281.a3.bot.StrategyFactory;
import nz.ac.auckland.se281.a3.dealer.Dealer;
import nz.ac.auckland.se281.a3.dealer.DealerStrategy;
import nz.ac.auckland.se281.a3.dealer.TargetHighestBidderStrategy;
import nz.ac.auckland.se281.a3.dealer.TargetTopWinnerStrategy;

/**
 * Unless it is specified in the JavaDoc, you cannot change any methods.
 * 
 * You can add new methods and/or new instance fields
 */
public class BlackJack {

	// Instance fields
	private List<Player> players;
	private Dealer dealer;
	private DealerStrategy[] dealerStrategies;
	private int currentDealerStrategy;
	private Deck deck;

	public BlackJack(Deck deck) {
		this.deck = deck;
		players = new ArrayList<>();
		players.add(new Human("Player1")); // add the Human player first.

	}

	/**
	 * This constructor is for testing reasons
	 * 
	 * @param cards
	 */
	protected BlackJack(Card... cards) {
		this(new Deck(cards));

	}

	public BlackJack() {
		this(new Deck());
	}

	public List<Player> getPlayers() {
		return players;
	}

	private String getBotStrategy() {
		System.out.println("Choose Bot strategy: random (R) - low risk (LR) - high risk (HR)");
		String result = Main.scanner.next();
		while (!result.equals("R") && !result.equals("LR") && !result.equals("HR") && !result.equals("A")) {
			System.out.println("please type \"R\", \"LR\",  \"HR\"");
			result = Main.scanner.next();
		}
		return result;
	}

	// do not change this method
	public void start() {
		initBots();
		initDealer();
		String res;
		int round = 0;
		do {
			round++;
			for (Participant p : players) {
				p.play(deck, round);
			}
			dealer.play(deck, round);
			printAndUpdateResults(round); // after each game print result and update scoreboard
			System.out.println("Do you want to play again?");
			res = Main.scanner.next();
			while (!res.equals("yes") && !res.equals("no")) {
				System.out.println("please type either \"yes\" or \"no\"");
				res = Main.scanner.next();
			}
		} while (res.equals("yes"));
		printGameStatistics(); // when the user terminates the game print the statistics
	}

	/**
	 * TODO This method initializes the Bots, you should change this method for
	 * Task1
	 */

	/**
	 * Initializes the bots for the BlackJack game by creating two new instances of
	 * Bot with its given name and strategy as its constructor parameters and adds
	 * them both to the player list.
	 */
	protected void initBots() {
		String botStrategyString = getBotStrategy();

		// Create and set bot strategies
		Bot bot1 = new Bot("Bot1", StrategyFactory.createStrategy(botStrategyString));
		Bot bot2 = new Bot("Bot2", StrategyFactory.createStrategy(botStrategyString));

		// Adding bots to the player list
		players.add(bot1);
		players.add(bot2);
	}

	/**
	 * TODO This method initializes the Dealer, you should change this method for
	 * Task2
	 */

	/**
	 * Initializes the dealer for the BlackJack game by creating an instance of
	 * Dealer with its given name and strategy (where its initial strategy being
	 * Target Highest Bidder Strategy)
	 */
	protected void initDealer() {
		// set the initial strategy using the Strategy pattern

		// Creating dealer strategies
		this.dealerStrategies = new DealerStrategy[2];
		this.currentDealerStrategy = 0;
		dealerStrategies[0] = new TargetHighestBidderStrategy(this);
		dealerStrategies[1] = new TargetTopWinnerStrategy(this);

		// Creating dealer
		dealer = new Dealer("Dealer", dealerStrategies[currentDealerStrategy]);

	}

	/**
	 * TODO This method prints and updates the results (wins and losses) you should
	 * change this method for Task 2 and Task 3
	 */

	/**
	 * Prints and updates the results of every player after each BlackJack round and
	 * changes strategy of Dealer or not depending on if a player has net wins
	 * greater or equal than 2
	 * 
	 * @param round current round of the BlackJack game
	 */
	protected void printAndUpdateResults(int round) {
		updateRoundResults();
		changeDealerStrategy();
		printRoundResults(round);
	}

	/**
	 * TODO This method should print the statistic of the game when it ends
	 */

	/**
	 * Print the game statistics (i.e. total rounds each player lost and won) after
	 * the final BlackJack round (i.e. when the game ends)
	 * 
	 */
	protected void printGameStatistics() {
		for (Player player : players) {
			System.out.println(player.getName() + " won " + player.getRoundsWon() + " times and lost "
					+ player.getRoundsLost() + " times");
		}
	}

	/**
	 * Updates the amount of rounds won or lost by each player have after a
	 * BlackJack round has been played
	 */
	public void updateRoundResults() {

		// Cycle through each player and update their rounds won and lost
		for (Player player : players) {
			if (ifPlayerWon(player)) {
				player.increaseRoundsWon();
			} else {
				player.increaseRoundsLost();
			}
		}
	}

	/**
	 * Checks if the player has won the current round in the BlackJack game and
	 * returns the boolean variable that determines if the player won or not
	 * 
	 * @param player player of the BlackJack game
	 * @return ifPlayerWon boolean variable that determines if the player has won
	 */
	public boolean ifPlayerWon(Player player) {
		boolean ifPlayerWon = false;

		// Getting the player's hand
		Hand playerHand = player.getHand();

		// Determining if the player has won the round
		if (playerHand.isBlackJack() && !dealer.getHand().isBlackJack()) {
			ifPlayerWon = true;
		} else if ((playerHand.getScore() > dealer.getHand().getScore()) && !playerHand.isBust()) {
			ifPlayerWon = true;
		} else if (dealer.getHand().isBust() && !playerHand.isBust()) {
			ifPlayerWon = true;
		}
		return ifPlayerWon;
	}

	/**
	 * Checks if any player has two or more net wins to determine if the dealer
	 * should change its strategy and returns the boolean variable that determines
	 * whether or not the dealer should change its strategy
	 * 
	 * @return ifChangeDealerStrategy boolean variable that determines if the dealer
	 *         strategy should be changed
	 */
	public boolean ifChangeDealerStrategy() {
		boolean ifChangeDealerStrategy = false;

		// Cycle through each player and determine if a player has 2 or more net wins
		for (Player player : players) {
			if (player.getNetWins() >= 2) {
				ifChangeDealerStrategy = true;
			}
		}
		return ifChangeDealerStrategy;
	}

	/**
	 * Changes the dealer strategy to the Target Top Winner strategy if a player has
	 * two or more net wins or change to the Target Highest Bidder strategy
	 * otherwise
	 * 
	 */
	public void changeDealerStrategy() {

		// Change dealer strategy
		if (ifChangeDealerStrategy()) {
			this.currentDealerStrategy = 1;
		} else {
			this.currentDealerStrategy = 0;

		}
		dealer.setStrategy(dealerStrategies[currentDealerStrategy]);
	}

	/**
	 * Prints the current round result of the BlackJack game for each player showing
	 * if the player has won or lost and their corresponding bet
	 * 
	 * @param round current round of the BlackJack game
	 */
	public void printRoundResults(int round) {

		// Cycle through each player
		for (Player player : players) {
			String wonOrLost = "lost";
			if (ifPlayerWon(player)) {
				wonOrLost = "won";
			}

			// Print round results for player
			System.out.println("Round " + round + ": " + player.getName() + " " + wonOrLost + " "
					+ player.getHand().getBet() + " chips");
		}
	}

}
