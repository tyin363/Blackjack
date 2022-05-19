package nz.ac.auckland.se281.a3.bot;

public class StrategyFactory {

	/**
	 * Creates a certain instance of the bot strategy depending on the given
	 * strategy as a String parameter and returns the bot strategy
	 * 
	 * @param strategy strategy of bot in String format
	 * @return bot strategy
	 */
	public static BotStrategy createStrategy(String strategy) {
		switch (strategy) {

		// Creates and returns random strategy
		case "R":
			return new RandomStrategy();

		// Creates and returns low risk strategy
		case "LR":
			return new LowRiskStrategy();

		// Creates and returns high risk strategy
		case "HR":
			return new HighRiskStrategy();

		default:
			System.exit(0);
		}
		return null;
	}
}
