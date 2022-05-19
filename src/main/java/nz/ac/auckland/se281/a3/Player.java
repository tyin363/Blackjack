package nz.ac.auckland.se281.a3;

/**
 * 
 * You can (and should) add new fields and/or methods
 *
 */
public abstract class Player extends Participant {

	// Instance fields
	private int roundsWon;
	private int roundsLost;

	/**
	 * Constructor of a player with its given name and initializes both their rounds
	 * won and lost to 0
	 * 
	 * @param name name of the player
	 */
	public Player(String name) {
		super(name);
		roundsWon = 0;
		roundsLost = 0;
	}

	public abstract int makeABet();

	/**
	 * Increase the amount of rounds won of the player of the BlackJack game by 1
	 */

	public void increaseRoundsWon() {
		this.roundsWon++;
	}

	/**
	 * Increase the amount of rounds lost of the player of the BlackJack game by 1
	 */
	public void increaseRoundsLost() {
		this.roundsLost++;
	}

	/**
	 * Calculates the net wins of the player of the BlackJack game and returns the
	 * net wins of the player
	 * 
	 * @return net wins of the player
	 */
	public int getNetWins() {
		return roundsWon - roundsLost;
	}

	public int getRoundsWon() {
		return roundsWon;
	}

	public int getRoundsLost() {
		return roundsLost;
	}

}
