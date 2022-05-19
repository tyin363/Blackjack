package nz.ac.auckland.se281.a3;

import static nz.ac.auckland.se281.a3.Card.Rank.ACE;
import static nz.ac.auckland.se281.a3.Card.Rank.EIGHT;
import static nz.ac.auckland.se281.a3.Card.Rank.FIVE;
import static nz.ac.auckland.se281.a3.Card.Rank.FOUR;
import static nz.ac.auckland.se281.a3.Card.Rank.JACK;
import static nz.ac.auckland.se281.a3.Card.Rank.KING;
import static nz.ac.auckland.se281.a3.Card.Rank.NINE;
import static nz.ac.auckland.se281.a3.Card.Rank.QUEEN;
import static nz.ac.auckland.se281.a3.Card.Rank.SEVEN;
import static nz.ac.auckland.se281.a3.Card.Rank.SIX;
import static nz.ac.auckland.se281.a3.Card.Rank.TEN;
import static nz.ac.auckland.se281.a3.Card.Rank.THREE;
import static nz.ac.auckland.se281.a3.Card.Rank.TWO;
import static nz.ac.auckland.se281.a3.Card.Suit.CLUBS;
import static nz.ac.auckland.se281.a3.Card.Suit.DIAMONDS;
import static nz.ac.auckland.se281.a3.Card.Suit.HEARTS;
import static nz.ac.auckland.se281.a3.Card.Suit.SPADES;
import static nz.ac.auckland.se281.a3.Main.scanner;
import static nz.ac.auckland.se281.a3.Participant.Action.HIT;
import static nz.ac.auckland.se281.a3.Participant.Action.HOLD;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import nz.ac.auckland.se281.a3.bot.Bot;

@RunWith(Suite.class)
@SuiteClasses({ BlackJackTestSuite.Task1Test.class, //
		BlackJackTestSuite.Task2Test.class, //
		BlackJackTestSuite.Task3Test.class, //
// BlackJackTestSuite.YourTest.class //
})

public class BlackJackTestSuite {
	public static String nl = System.getProperty("line.separator");

	static class HandFactory {

		public static void addHand(Participant p, int bet, Card... cards) {
			Hand hand = p.createHand();
			for (Card card : cards) {
				hand.addCard(card);
			}
		}
	}

	public abstract static class TaskTest {

		private ByteArrayOutputStream captureOut;
		private ByteArrayOutputStream captureErr;
		private PrintStream origOut;
		private PrintStream origErr;
		private static String delimiterRun = "---<END RUN>---";

		// as mentioned in the handout each single test cannot run more than 10 seconds
		@Rule // comment if you want to run the debug
		public Timeout timeout = new Timeout(10, TimeUnit.SECONDS); // comment if you
		// want to run the debug

		@Before
		public void setUp() {
			origOut = System.out;
			origErr = System.err;
			captureOut = new ByteArrayOutputStream();
			System.setOut(new PrintStream(captureOut));
			captureErr = new ByteArrayOutputStream();
			System.setErr(new PrintStream(captureErr));
		}

		@After
		public void tearDown() {
			System.setOut(origOut);
			System.setErr(origErr);
			if (captureOut.toString().length() > 1) {
				System.out.println(System.lineSeparator() + "the System.out.print was :" + System.lineSeparator()
						+ captureOut.toString());
			}
			if (captureErr.toString().length() > 1) {
				System.out.println(System.lineSeparator() + "the System.err.print was :" + System.lineSeparator()
						+ captureErr.toString());
			}
		}

		void runCommands(BlackJack blackJack, String... commands) {

			StringBuilder sb = new StringBuilder();
			for (String cmdString : commands) {
				sb.append(cmdString);
				sb.append(nl);
			}
			sb.append("exit");
			scanner = new Scanner(sb.toString());
			blackJack.start();
			System.out.println(nl);
			System.out.println(delimiterRun);
		}

		void assertContains(String s) {

			if (!captureOut.toString().contains(s)) {
				fail("The test is expecting the following output in the console but was not there " + nl + s);
			}
		}

		void assertDoesNotContain(String s) {
			if (captureOut.toString().contains(s)) {
				fail("The test is expecting that the following output WAS NOT in the console but is was there " + nl
						+ s);
			}
		}

		private void checkRun(String s, int run) {
			if (run < 0 || !captureOut.toString().contains(delimiterRun)
					|| run > captureOut.toString().split(delimiterRun).length) {
				throw new RuntimeException("Something is wrong with the test case!");
			}
		}

		void assertContains(String s, int run) {
			checkRun(s, run);
			String cString = captureOut.toString();
			if (!cString.split(delimiterRun)[run].contains(s)) {
				fail("The test is expecting the following output in the console but was not there " + nl + s);
			}
		}

		void assertDoesNotContain(String s, int run) {
			checkRun(s, run);
			if (captureOut.toString().split(delimiterRun)[run].contains(s)) {
				fail("The test is expecting that the following output WAS NOT in the console but is was there " + nl
						+ s);
			}
		}

	}

	@FixMethodOrder(MethodSorters.NAME_ASCENDING)
	public static class Task1Test extends TaskTest {

		BlackJack blackJack;

		@Before
		public void setUp() {
			super.setUp();
			blackJack = new BlackJack();
		}

		private void sanityCheck() {
			// make sure that you don't have change the order of players in the list
			assertEquals("We should have three players, a human and two bots", 3, blackJack.getPlayers().size());
			assertTrue("the first player should be human", blackJack.getPlayers().get(0) instanceof Human);
			assertTrue("the second player should be a bot", blackJack.getPlayers().get(1) instanceof Bot);
			assertTrue("the third player should be a bot", blackJack.getPlayers().get(2) instanceof Bot);
		}

		@Test
		public void T1_01_not_always_hold_random() throws InterruptedException {
			scanner = new Scanner("R");
			blackJack.initBots();
			sanityCheck();
			Hand hand = new Hand();
			hand.addCard(new Card(TWO, DIAMONDS));
			hand.addCard(new Card(THREE, DIAMONDS));
			boolean foundHit = false;
			outer: for (int i = 0; i < 1000; i++) {
				for (int index : Arrays.asList(1, 2)) { // need to check list position 1 and 2
					if (blackJack.getPlayers().get(index).decideAction(hand).equals(HIT)) {
						foundHit = true;
						break outer;
					}
				}
			}
			assertTrue("the bot always hold, you need to implement the strategies!", foundHit);
		}

		@Test
		public void T1_02_random_bet_should_not_be_always_1() {
			scanner = new Scanner("R");
			blackJack.initBots();
			sanityCheck();
			Hand hand = new Hand();
			hand.addCard(new Card(TWO, DIAMONDS));
			hand.addCard(new Card(THREE, DIAMONDS));

			boolean found = false;
			outer: for (int i = 0; i < 1000; i++) {
				for (int index : Arrays.asList(1, 2)) { // need to check list position 1 and 2
					if (blackJack.getPlayers().get(index).makeABet() != 1) {
						found = true;
						break outer;
					}
				}
			}
			assertTrue("the bot currently always make a bet of 1, you need to change it ", found);
		}

		@Test
		public void T1_03_random_bet_range_1_100() {
			scanner = new Scanner("R");
			blackJack.initBots();
			sanityCheck();
			boolean found = false;

			for (int i = 0; i < 1000; i++) {
				for (int index : Arrays.asList(1, 2)) { // need to check list position 1 and 2
					int bet = blackJack.getPlayers().get(index).makeABet();
					if (blackJack.getPlayers().get(index).makeABet() != 1) {
						found = true;
					}
					if (bet < 1 || bet > 100) {
						fail("the bet should be between 1 and 100 inclusive");
					}
				}
			}
			assertTrue(found); // should be at least some bet != from 1
		}

		@Test
		public void T1_04_low_risk_correctly_hold() {
			scanner = new Scanner("LR");
			blackJack.initBots();
			sanityCheck();
			Hand hand = new Hand();
			hand.addCard(new Card(KING, DIAMONDS));
			hand.addCard(new Card(FOUR, SPADES));
			assertEquals(blackJack.getPlayers().get(1).decideAction(hand), HIT);
			assertEquals(blackJack.getPlayers().get(2).decideAction(hand), HIT);
			hand.addCard(new Card(FOUR, CLUBS));
			assertEquals(blackJack.getPlayers().get(1).decideAction(hand), HOLD);
			assertEquals(blackJack.getPlayers().get(2).decideAction(hand), HOLD);
		}

		@Test
		public void T1_05_lowrisk_bet_greater_equal_10() {
			scanner = new Scanner("LR");
			blackJack.initBots();
			sanityCheck();

			for (int i = 0; i < 1000; i++) {
				for (int index : Arrays.asList(1, 2)) { // need to check list position 1 and 2
					int bet = blackJack.getPlayers().get(index).makeABet();
					if (bet < 10) {
						fail("the bet should be greater than ten");
					}
				}
			}
		}

		@Test
		public void T1_06_high_risk_correctly_hold() {
			scanner = new Scanner("HR");
			blackJack.initBots();
			sanityCheck();
			Hand hand = new Hand();
			hand.addCard(new Card(TWO, DIAMONDS));
			hand.addCard(new Card(FOUR, SPADES));
			assertEquals(blackJack.getPlayers().get(1).decideAction(hand), HIT);
			assertEquals(blackJack.getPlayers().get(2).decideAction(hand), HIT);
			hand.addCard(new Card(TWO, DIAMONDS));
			hand.addCard(new Card(KING, SPADES));
			assertEquals(blackJack.getPlayers().get(1).decideAction(hand), HIT);
			assertEquals(blackJack.getPlayers().get(2).decideAction(hand), HIT);
			hand.addCard(new Card(TWO, CLUBS));
			assertEquals(blackJack.getPlayers().get(1).decideAction(hand), HOLD);
			assertEquals(blackJack.getPlayers().get(2).decideAction(hand), HOLD);
		}

		@Test
		public void T1_07_highrisk_bet_greater_equal_50() {
			scanner = new Scanner("HR");
			blackJack.initBots();
			sanityCheck();

			for (int i = 0; i < 1000; i++) {
				for (int index : Arrays.asList(1, 2)) { // need to check list position 1 and 2
					int bet = blackJack.getPlayers().get(index).makeABet();
					if (bet < 50) {
						fail("the bet should be greater than 50");
					}
				}
			}
		}
	}

	@FixMethodOrder(MethodSorters.NAME_ASCENDING)
	public static class Task2Test extends TaskTest {

		@Test
		public void T2_01_highest_bidder_should_hit_and_loose() {
			BlackJack blackJack = new BlackJack(new Card(KING, CLUBS), new Card(JACK, CLUBS), // human
					new Card(ACE, CLUBS), new Card(JACK, HEARTS), // BOT blackjack - not relevant
					new Card(ACE, HEARTS), new Card(JACK, SPADES), // BOT blackjack - not relevant
					new Card(KING, SPADES), new Card(NINE, CLUBS), new Card(TEN, CLUBS) // dealer should hit and be
																						// busted
			);
			runCommands(blackJack, "LR", "100", "hold", " ", " ", " ", "no");
			assertContains("Round 1: Dealer's score is: 19");
			assertContains("Round 1: Dealer #1 HIT");
			assertDoesNotContain("Round 1: Dealer #1 HOLD");
		}

		@Test
		public void T2_02_highest_bidder_should_hit_and_win() {
			BlackJack blackJack = new BlackJack(new Card(KING, CLUBS), new Card(JACK, CLUBS), // human
					new Card(ACE, CLUBS), new Card(JACK, HEARTS), // BOT blackjack - not relevant
					new Card(ACE, HEARTS), new Card(JACK, SPADES), // BOT blackjack - not relevant
					new Card(KING, SPADES), new Card(NINE, CLUBS), new Card(TWO, CLUBS) // dealer should hit and win
			);
			runCommands(blackJack, "LR", "100", "hold", " ", " ", " ", "no");
			assertContains("Round 1: Dealer's score is: 19");
			assertContains("Round 1: Dealer #1 HIT");
			assertDoesNotContain("Round 1: Dealer #1 HOLD");
			assertContains("Round 1: Dealer's score is: 21");
		}

		@Test
		public void T2_03_highest_bidder_should_hold() {
			BlackJack blackJack = new BlackJack(new Card(KING, CLUBS), new Card(TWO, CLUBS), new Card(SEVEN, CLUBS), // human
					new Card(ACE, CLUBS), new Card(JACK, HEARTS), // BOT blackjack - not relevant
					new Card(ACE, HEARTS), new Card(JACK, SPADES), // BOT blackjack - not relevant
					new Card(KING, SPADES), new Card(FIVE, CLUBS), new Card(FIVE, HEARTS)// dealer should hit and hold
			);
			runCommands(blackJack, "LR", "100", "hit", "hold", " ", " ", " ", "no");
			assertContains("Round 1: Player1's score is: 19");
			assertContains("Round 1: Player1 #2 HOLD");
			assertContains("Round 1: Dealer #1 HIT");
			assertDoesNotContain("Round 1: Dealer #1 HOLD");
			assertContains("Round 1: Dealer #2 HOLD");
			assertDoesNotContain("Round 1: Dealer #2 HIT");
		}

		@Test
		public void T2_04_highest_bidder_should_hold_same_score() {
			BlackJack blackJack = new BlackJack(new Card(KING, CLUBS), new Card(TWO, CLUBS), new Card(SEVEN, CLUBS), // human
					new Card(ACE, CLUBS), new Card(JACK, HEARTS), // BOT blackjack - not relevant
					new Card(ACE, HEARTS), new Card(JACK, SPADES), // BOT blackjack - not relevant
					new Card(KING, SPADES), new Card(FOUR, CLUBS), new Card(FIVE, CLUBS) // dealer should hit and hold
			);
			runCommands(blackJack, "LR", "100", "hit", "hold", " ", " ", " ", "no");
			assertContains("Round 1: Player1's score is: 19");
			assertContains("Round 1: Player1 #2 HOLD");
			assertContains("Round 1: Dealer #1 HIT");
			assertDoesNotContain("Round 1: Dealer #1 HOLD");
			assertContains("Round 1: Dealer #2 HOLD");
			assertDoesNotContain("Round 1: Dealer #2 HIT");
			assertContains("Round 1: Dealer's score is: 19");
		}

		@Test
		public void T2_05_highest_bidder_black_jack_should_hold_on_17() {
			BlackJack blackJack = new BlackJack(new Card(KING, CLUBS), new Card(ACE, HEARTS), // human blackjack
					new Card(ACE, CLUBS), new Card(JACK, HEARTS), // BOT blackjack - not relevant
					new Card(ACE, HEARTS), new Card(JACK, SPADES), // BOT blackjack - not relevant
					new Card(KING, SPADES), new Card(TWO, CLUBS), new Card(TWO, CLUBS), new Card(TWO, CLUBS),
					new Card(TWO, CLUBS) // dealer should reach >=17
			);
			runCommands(blackJack, "LR", "100", "hit", "hold", " ", " ", " ", "no");
			assertContains("Round 1: Player1's score is: 21");
			assertContains("Black Jack! :D");
			assertContains("Round 1: Dealer's score is: 12");
			// dealer cannot beat blackjack so just hold on >=17
			assertContains("Round 1: Dealer #1 HIT");
			assertDoesNotContain("Round 1: Dealer #1 HOLD");
			assertContains("Round 1: Dealer's score is: 14");
			assertContains("Round 1: Dealer #2 HIT");
			assertDoesNotContain("Round 1: Dealer #2 HOLD");
			assertContains("Round 1: Dealer's score is: 16");
			assertContains("Round 1: Dealer #3 HIT");
			assertDoesNotContain("Round 1: Dealer #3 HOLD");
			assertContains("Round 1: Dealer's score is: 18");
			assertContains("Round 1: Dealer #4 HOLD");
			assertDoesNotContain("Round 1: Dealer #4 HIT");
		}

		@Test
		public void T2_06_dealer_should_change_strategy_should_hit_win() {
			BlackJack blackJack = new BlackJack(
					// first round
					new Card(KING, CLUBS), new Card(ACE, HEARTS), // human wins
					new Card(JACK, CLUBS), new Card(SIX, HEARTS), new Card(TEN, HEARTS), // BOT BUSTED
					new Card(ACE, HEARTS), new Card(JACK, SPADES), // BOT blackjack
					new Card(KING, SPADES), new Card(TWO, CLUBS), new Card(QUEEN, CLUBS), // dealer busted
					// second round
					new Card(KING, CLUBS), new Card(ACE, HEARTS), // human wins
					new Card(JACK, CLUBS), new Card(SIX, HEARTS), new Card(TEN, HEARTS), // BOT BUSTED
					new Card(JACK, CLUBS), new Card(SIX, HEARTS), new Card(TEN, HEARTS), // BOT BUSTED
					new Card(KING, SPADES), new Card(TWO, CLUBS), new Card(QUEEN, CLUBS), // dealer busted
					// third round
					new Card(KING, CLUBS), new Card(QUEEN, HEARTS), // 20
					new Card(JACK, CLUBS), new Card(EIGHT, HEARTS), // 18
					new Card(JACK, HEARTS), new Card(NINE, SPADES), // 19
					new Card(KING, SPADES), new Card(TWO, CLUBS), new Card(TWO, CLUBS), new Card(ACE, CLUBS),
					new Card(ACE, CLUBS), new Card(TWO, CLUBS), new Card(THREE, HEARTS));
			runCommands(blackJack, "LR",
					// first round
					"100", " ", " ", " ", "yes",
					// second round
					"100", " ", " ", " ", "yes",
					// third round dealer strategy should be changed
					// the human is not the top bidder, but should be still targeted by the dealer
					// because is net win is >=2
					"1", "hold", " ", " ", " ", "no");
			assertContains("Round 1: Dealer #1 HIT");
			assertDoesNotContain("Round 1: Dealer #1 HOLD");
			assertContains("Round 1: Dealer's score is: 22");
			assertContains("Round 2: Dealer #1 HIT");
			assertDoesNotContain("Round 2: Dealer #1 HOLD");
			assertContains("Round 2: Dealer's score is: 22");
			// the dealer should change strategy and target the human even if it is not the
			// highest bidder
			for (int i = 1; i <= 5; i++) {
				assertContains(String.format("Round 3: Dealer #%d HIT", i));
				assertDoesNotContain(String.format("Round 3: Dealer #%d HOLD", i));
			}
			assertContains("Round 3: Dealer's score is: 21");

		}

		@Test
		public void T2_07_dealer_should_change_strategy_should_hit_lost() {
			BlackJack blackJack = new BlackJack(
					// first round
					new Card(KING, CLUBS), new Card(ACE, HEARTS), // human wins
					new Card(JACK, CLUBS), new Card(SIX, HEARTS), new Card(TEN, HEARTS), // BOT BUSTED
					new Card(ACE, HEARTS), new Card(JACK, SPADES), // BOT blackjack
					new Card(KING, SPADES), new Card(TWO, CLUBS), new Card(QUEEN, CLUBS), // dealer busted
					// second round
					new Card(KING, CLUBS), new Card(ACE, HEARTS), // human wins
					new Card(JACK, CLUBS), new Card(SIX, HEARTS), new Card(TEN, HEARTS), // BOT BUSTED
					new Card(JACK, CLUBS), new Card(SIX, HEARTS), new Card(TEN, HEARTS), // BOT BUSTED
					new Card(KING, SPADES), new Card(TWO, CLUBS), new Card(QUEEN, CLUBS), // dealer busted
					// third round
					new Card(KING, CLUBS), new Card(QUEEN, HEARTS), // 20
					new Card(JACK, CLUBS), new Card(EIGHT, HEARTS), // 18
					new Card(JACK, HEARTS), new Card(NINE, SPADES), // 19
					new Card(KING, SPADES), new Card(TWO, CLUBS), new Card(TWO, CLUBS), new Card(ACE, CLUBS),
					new Card(ACE, CLUBS), new Card(TWO, CLUBS), new Card(TEN, HEARTS) // dealer busted
			);
			runCommands(blackJack, "LR",
					// first round
					"100", " ", " ", " ", "yes",
					// second round
					"100", " ", " ", " ", "yes",
					// third round dealer strategy should be changed
					// the human is not the top bidder, but should be still targeted by the dealer
					"1", "hold", " ", " ", " ", "no");
			assertContains("Round 1: Dealer #1 HIT");
			assertDoesNotContain("Round 1: Dealer #1 HOLD");
			assertContains("Round 1: Dealer's score is: 22");
			assertContains("Round 2: Dealer #1 HIT");
			assertDoesNotContain("Round 2: Dealer #1 HOLD");
			assertContains("Round 2: Dealer's score is: 22");
			// the dealer should change strategy and target the human even if it is not the
			// highest bidder
			for (int i = 1; i <= 5; i++) {
				assertContains(String.format("Round 3: Dealer #%d HIT", i));
				assertDoesNotContain(String.format("Round 3: Dealer #%d HOLD", i));
			}
			assertContains("Round 3: Dealer's score is: 28");
		}

		@Test
		public void T2_08_dealer_should_change_strategy_target_bot() {
			BlackJack blackJack = new BlackJack(
					// first round
					new Card(KING, CLUBS), new Card(KING, HEARTS), new Card(KING, SPADES), // human busted
					new Card(THREE, HEARTS), new Card(JACK, SPADES), new Card(EIGHT, SPADES), // BOT 21
					new Card(ACE, HEARTS), new Card(JACK, SPADES), // BOT blackjack
					new Card(KING, SPADES), new Card(TWO, CLUBS), // dealer HOLD even if it is loosing from BOT1
					// second round
					new Card(KING, CLUBS), new Card(ACE, HEARTS), // human wins
					new Card(ACE, HEARTS), new Card(JACK, SPADES), // BOT blackjack
					new Card(JACK, CLUBS), new Card(FIVE, HEARTS), new Card(TEN, HEARTS), // BOT BUSTED
					new Card(KING, SPADES), new Card(TWO, CLUBS), new Card(QUEEN, CLUBS), // dealer busted
					// third round
					new Card(KING, CLUBS), new Card(QUEEN, HEARTS), // 20
					new Card(JACK, CLUBS), new Card(EIGHT, HEARTS), // 18
					new Card(JACK, HEARTS), new Card(NINE, SPADES), // 19
					new Card(KING, SPADES), new Card(TWO, CLUBS), new Card(TWO, CLUBS), new Card(ACE, CLUBS),
					new Card(ACE, CLUBS), new Card(TWO, CLUBS) // dealer busted
			);
			runCommands(blackJack, "LR",
					// first round
					"100", "hit", " ", " ", " ", "yes",
					// second round
					"100", " ", " ", " ", "yes",
					// the human is the highest bidder but dealer should target Bot1
					"100", "hold", " ", " ", " ", "no");
			assertContains("Round 1: Dealer #1 HOLD");
			assertDoesNotContain("Round 1: Dealer #1 HIT");
			// dealer try to beat the human
			assertContains("Round 2: Dealer #1 HIT");
			assertDoesNotContain("Round 2: Dealer #1 HOLD");
			assertContains("Round 2: Dealer's score is: 22");
			// the dealer should change strategy and target the Bot1 even if it is not the
			// highest bidder
			for (int i = 1; i <= 4; i++) {
				assertContains(String.format("Round 3: Dealer #%d HIT", i));
				assertDoesNotContain(String.format("Round 3: Dealer #%d HOLD", i));
			}
			// dealer should hold at 18 because is beating BOt1 which is the player with >2
			// net wins
			assertContains("Round 3: Dealer #5 HOLD");
			assertDoesNotContain("Round 3: Dealer #5 HIT");

		}

		@Test
		public void T2_09_dealer_should_change_strategy_fifth_rounds() {
			BlackJack blackJack = new BlackJack(
					// first round
					new Card(KING, CLUBS), new Card(QUEEN, HEARTS), // HOLD LOST
					new Card(JACK, CLUBS), new Card(EIGHT, HEARTS), // HOLD LOST
					new Card(JACK, HEARTS), new Card(NINE, SPADES), // HOLD LOST
					new Card(KING, SPADES), new Card(ACE, CLUBS), // dealer BLACKJACK
					// second round
					new Card(KING, CLUBS), new Card(TEN, HEARTS), // 20 HOLD LOST
					new Card(JACK, CLUBS), new Card(EIGHT, HEARTS), // 18 HOLD LOST
					new Card(JACK, HEARTS), new Card(ACE, SPADES), // BLACKJACK WINS
					new Card(KING, SPADES), new Card(NINE, CLUBS), new Card(ACE, CLUBS), // dealer HIT HOLD (target
																							// human)
					// third round
					new Card(KING, CLUBS), new Card(QUEEN, HEARTS), // 20 WINS
					new Card(JACK, CLUBS), new Card(EIGHT, HEARTS), // 18 WINS
					new Card(JACK, HEARTS), new Card(NINE, SPADES), // 19 WINS
					new Card(KING, SPADES), new Card(FIVE, CLUBS), new Card(KING, CLUBS), // dealer busted
					// fourth round
					new Card(KING, CLUBS), new Card(QUEEN, HEARTS), new Card(SEVEN, HEARTS), // BUSTED
					new Card(JACK, HEARTS), new Card(ACE, SPADES), // BLACKJACK WINS
					new Card(JACK, HEARTS), new Card(ACE, SPADES), // BLACKJACK WINS
					new Card(KING, SPADES), new Card(NINE, CLUBS), // dealer HOLD
					// fifth round
					new Card(KING, CLUBS), new Card(QUEEN, HEARTS), // 20 WINS
					new Card(JACK, CLUBS), new Card(EIGHT, HEARTS), // 18 WINS
					new Card(JACK, HEARTS), new Card(NINE, SPADES), // 19 WINS
					new Card(KING, SPADES), new Card(SEVEN, CLUBS), new Card(ACE, CLUBS), new Card(ACE, HEARTS) // dealer
																												// should
																												// target
																												// Bot2
			// hold with 19
			);
			runCommands(blackJack, "LR",
					// first round
					"100", "hold", " ", " ", " ", "yes",
					// second round
					"100", "hold", " ", " ", " ", "yes",
					// third round
					"100", "hold", " ", " ", " ", "yes",
					// fourth round
					"100", "hit", " ", " ", " ", "yes",
					// fifth round
					"100", "hold", " ", " ", " ", "no");
			assertContains("Round 2: Dealer #1 HIT");
			assertDoesNotContain("Round 2: Dealer #1 HOLD");
			assertContains("Round 2: Dealer #2 HOLD");
			assertDoesNotContain("Round 2: Dealer #2 HIT");
			assertContains("Round 3: Dealer #1 HIT");
			assertDoesNotContain("Round 3: Dealer #1 HOLD");
			assertContains("Round 4: Dealer #1 HOLD");
			assertDoesNotContain("Round 4: Dealer #1 HIT");
			// now the strategy of the dealer has to change it needs to target Bot2 so it
			// holds at 19
			assertContains("Round 5: Dealer #1 HIT");
			assertDoesNotContain("Round 5: Dealer #1 HOLD");
			assertContains("Round 5: Dealer #2 HIT");
			assertDoesNotContain("Round 5: Dealer #2 HOLD");
			assertContains("Round 5: Dealer #3 HOLD");
			assertDoesNotContain("Round 5: Dealer #3 HIT");
		}

		@Test
		public void T2_10_dealer_should_not_change_strategy() {
			BlackJack blackJack = new BlackJack(
					// first round
					new Card(KING, CLUBS), new Card(QUEEN, HEARTS), // HOLD LOST
					new Card(JACK, CLUBS), new Card(TEN, HEARTS), // HOLD LOST
					new Card(JACK, HEARTS), new Card(NINE, SPADES), // HOLD LOST
					new Card(KING, SPADES), new Card(ACE, CLUBS), // dealer BLACKJACK
					// second round
					new Card(KING, CLUBS), new Card(TEN, HEARTS), // 20 HOLD LOST
					new Card(JACK, CLUBS), new Card(NINE, HEARTS), // 18 HOLD LOST
					new Card(JACK, HEARTS), new Card(ACE, SPADES), // BLACKJACK WINS
					new Card(KING, SPADES), new Card(NINE, CLUBS), new Card(ACE, CLUBS), // dealer HIT HOLD (target
																							// human)
					// third round
					new Card(KING, CLUBS), new Card(QUEEN, HEARTS), // 20 LOST
					new Card(JACK, CLUBS), new Card(NINE, HEARTS), // 18 LOST
					new Card(JACK, HEARTS), new Card(NINE, SPADES), // 19 LOST
					new Card(KING, SPADES), new Card(ACE, SPADES), // BLACKJACK
					// fourth round
					new Card(KING, CLUBS), new Card(QUEEN, HEARTS), new Card(SEVEN, HEARTS), // BUSTED
					new Card(JACK, HEARTS), new Card(ACE, SPADES), // BLACKJACK WINS
					new Card(JACK, HEARTS), new Card(ACE, SPADES), // BLACKJACK WINS
					new Card(KING, SPADES), new Card(NINE, CLUBS), // dealer HOLD
					// fifth round
					new Card(KING, CLUBS), new Card(QUEEN, HEARTS), // 20 WINS
					new Card(JACK, CLUBS), new Card(NINE, HEARTS), // 18 WINS
					new Card(JACK, HEARTS), new Card(NINE, SPADES), // 19 WINS
					new Card(KING, SPADES), new Card(SEVEN, CLUBS), new Card(ACE, CLUBS), new Card(ACE, HEARTS),
					new Card(ACE, HEARTS) // dealer
			// should
			// target
			// human
			// hold with 19
			);
			runCommands(blackJack, "HR",
					// first round
					"100", "hold", " ", " ", " ", "yes",
					// second round
					"100", "hold", " ", " ", " ", "yes",
					// third round
					"100", "hold", " ", " ", " ", "yes",
					// fourth round
					"100", "hit", " ", " ", " ", "yes",
					// fifth round
					"100", "hold", " ", " ", " ", "no");
			assertContains("Round 2: Dealer #1 HIT");
			assertDoesNotContain("Round 2: Dealer #1 HOLD");
			assertContains("Round 2: Dealer #2 HOLD");
			assertDoesNotContain("Round 2: Dealer #2 HIT");
			assertContains("Round 4: Dealer #1 HOLD");
			assertDoesNotContain("Round 4: Dealer #1 HIT");
			// now the strategy of the dealer has to change it needs to target Bot2 so it
			// holds at 19
			assertContains("Round 5: Dealer #1 HIT");
			assertDoesNotContain("Round 5: Dealer #1 HOLD");
			assertContains("Round 5: Dealer #2 HIT");
			assertDoesNotContain("Round 5: Dealer #2 HOLD");
			assertContains("Round 5: Dealer #3 HIT");
			assertDoesNotContain("Round 5: Dealer #3 HOLD");
			assertContains("Round 5: Dealer #4 HOLD");
			assertDoesNotContain("Round 5: Dealer #4 HIT");
		}

	}

	@FixMethodOrder(MethodSorters.NAME_ASCENDING)
	public static class Task3Test extends TaskTest {

		@Test
		public void T3_01_all_player_wins() {
			BlackJack blackJack = new BlackJack(new Card(ACE, HEARTS), new Card(KING, HEARTS), // blackjack
					new Card(ACE, CLUBS), new Card(JACK, HEARTS), // BOT blackjack
					new Card(ACE, HEARTS), new Card(JACK, SPADES), // BOT blackjack
					new Card(KING, SPADES), new Card(NINE, CLUBS) // dealer can't win because all players have blackjack
			);
			runCommands(blackJack, "LR", "33", "hold", " ", " ", " ", "no");

			StringBuilder sBuilder = new StringBuilder();
			sBuilder.append("Round 1: Player1 won 33 chips");
			sBuilder.append(nl);
			sBuilder.append("Round 1: Bot1 won " + blackJack.getPlayers().get(1).getHand().getBet() + " chips");
			sBuilder.append(nl);
			sBuilder.append("Round 1: Bot2 won " + blackJack.getPlayers().get(2).getHand().getBet() + " chips");
			sBuilder.append(nl);
			sBuilder.append("Do you want to play again?");
			assertContains(sBuilder.toString());
		}

		@Test
		public void T3_02_all_player_lost() {
			BlackJack blackJack = new BlackJack(new Card(ACE, HEARTS), new Card(KING, HEARTS), // blackjack
					new Card(TEN, CLUBS), new Card(JACK, HEARTS), // BOT blackjack
					new Card(TEN, HEARTS), new Card(JACK, SPADES), // BOT blackjack
					new Card(KING, SPADES), new Card(ACE, CLUBS) // dealer can't win because all players have blackjack
			);
			runCommands(blackJack, "LR", "70", "hold", " ", " ", " ", "no");

			StringBuilder sBuilder = new StringBuilder();
			sBuilder.append("Round 1: Player1 lost 70 chips"); // because also dealer has balcjack
			sBuilder.append(nl);
			sBuilder.append("Round 1: Bot1 lost " + blackJack.getPlayers().get(1).getHand().getBet() + " chips");
			sBuilder.append(nl);
			sBuilder.append("Round 1: Bot2 lost " + blackJack.getPlayers().get(2).getHand().getBet() + " chips");
			sBuilder.append(nl);
			sBuilder.append("Do you want to play again?");
			assertContains(sBuilder.toString());
		}

		@Test
		public void T3_03_bot_wins() {
			BlackJack blackJack = new BlackJack(new Card(TEN, HEARTS), new Card(KING, HEARTS), // blackjack
					new Card(TEN, CLUBS), new Card(JACK, HEARTS), // BOT blackjack
					new Card(ACE, HEARTS), new Card(JACK, SPADES), // BOT blackjack
					new Card(TEN, SPADES), new Card(ACE, CLUBS));
			runCommands(blackJack, "LR", "100", "hold", " ", " ", " ", "no");

			StringBuilder sBuilder = new StringBuilder();
			sBuilder.append("Round 1: Player1 lost 100 chips"); // because also dealer has balcjack
			sBuilder.append(nl);
			sBuilder.append("Round 1: Bot1 lost " + blackJack.getPlayers().get(1).getHand().getBet() + " chips");
			sBuilder.append(nl);
			sBuilder.append("Round 1: Bot2 lost " + blackJack.getPlayers().get(2).getHand().getBet() + " chips");
			sBuilder.append(nl);
			sBuilder.append("Do you want to play again?");
			assertContains(sBuilder.toString());
		}

		@Test
		public void T3_04_three_turn_correct() {
			BlackJack blackJack = new BlackJack(
					// first round
					new Card(KING, CLUBS), new Card(ACE, HEARTS), // human wins
					new Card(ACE, CLUBS), new Card(QUEEN, HEARTS), // BOT blackjack
					new Card(ACE, HEARTS), new Card(JACK, SPADES), // BOT blackjack
					new Card(KING, SPADES), new Card(TEN, CLUBS), // dealer cannot win
					// second round
					new Card(KING, CLUBS), new Card(SEVEN, HEARTS), // human lost
					new Card(ACE, CLUBS), new Card(QUEEN, HEARTS), // BOT blackjack
					new Card(ACE, HEARTS), new Card(JACK, SPADES), // BOT blackjack
					new Card(KING, SPADES), new Card(TEN, CLUBS), // dealer wins human

					new Card(KING, CLUBS), new Card(ACE, HEARTS), // human wins
					new Card(ACE, CLUBS), new Card(QUEEN, HEARTS), // BOT blackjack
					new Card(ACE, HEARTS), new Card(JACK, SPADES), // BOT blackjack
					new Card(KING, SPADES), new Card(TEN, CLUBS) // dealer cannot win
			);
			runCommands(blackJack, "LR",
					// first round
					"100", " ", " ", " ", "yes",
					// second round
					"100", "hold", " ", " ", " ", "yes",
					// third round dealer strategy should be changed
					// the human is not the top bidder, but should be still targeted by the dealer
					"11", " ", " ", " ", "no");
			assertContains("Round 1: Player1 won 100 chips"); // because also dealer has blackjack
			assertContains("Round 1: Bot1 won ");
			assertContains("Round 1: Bot2 won ");
			assertDoesNotContain("Round 1: Bot1 lost");

			assertContains("Round 2: Player1 lost 100 chips");
			assertContains("Round 2: Bot1 won ");
			assertContains("Round 2: Bot2 won ");
			assertDoesNotContain("Round 2: Bot1 lost ");

			assertContains("Round 3: Player1 won 11 chips");
			assertContains("Round 2: Bot1 won ");
			assertContains("Round 2: Bot2 won ");
			assertDoesNotContain("Round 2: Player1 lost 11 chips");
		}

		@Test
		public void T3_05_final_result_1() {
			BlackJack blackJack = new BlackJack(
					// first round
					new Card(KING, CLUBS), new Card(ACE, HEARTS), // human wins
					new Card(ACE, CLUBS), new Card(QUEEN, HEARTS), // BOT blackjack
					new Card(ACE, HEARTS), new Card(JACK, SPADES), // BOT blackjack
					new Card(KING, SPADES), new Card(TEN, CLUBS), // dealer cannot win
					// second round
					new Card(KING, CLUBS), new Card(SEVEN, HEARTS), // human lost
					new Card(ACE, CLUBS), new Card(QUEEN, HEARTS), // BOT blackjack
					new Card(ACE, HEARTS), new Card(JACK, SPADES), // BOT blackjack
					new Card(KING, SPADES), new Card(TEN, CLUBS), // dealer wins human

					new Card(KING, CLUBS), new Card(ACE, HEARTS), // human wins
					new Card(ACE, CLUBS), new Card(QUEEN, HEARTS), // BOT blackjack
					new Card(ACE, HEARTS), new Card(JACK, SPADES), // BOT blackjack
					new Card(KING, SPADES), new Card(TEN, CLUBS) // dealer cannot win
			);
			runCommands(blackJack, "LR",
					// first round
					"100", " ", " ", " ", "yes",
					// second round
					"100", "hold", " ", " ", " ", "yes",
					// third round dealer strategy should be changed
					// the human is not the top bidder, but should be still targeted by the dealer
					"11", " ", " ", " ", "no");
			assertContains("Player1 won 2 times and lost 1 times");
			assertContains("Bot1 won 3 times and lost 0 times");
			assertContains("Bot2 won 3 times and lost 0 times");
			for (Integer i : Arrays.asList(0, 1, 3)) {
				for (Integer j : Arrays.asList(0, 1, 2)) {
					for (Integer k : Arrays.asList(0, 1, 2)) {
						assertDoesNotContain("Player1 won " + i + " times and lost " + j + " times");
						assertDoesNotContain("Bot1 won " + j + " times and lost " + i);
						assertDoesNotContain("Bot2 won " + k + " times and lost " + j);
					}
				}
			}
		}

		@Test
		public void T3_06_final_result_2() {
			BlackJack blackJack = new BlackJack(
					// first round
					new Card(KING, CLUBS), new Card(ACE, HEARTS), // human lost
					new Card(TEN, CLUBS), new Card(QUEEN, HEARTS), // BOT blackjack
					new Card(ACE, HEARTS), new Card(JACK, SPADES), // BOT blackjack
					new Card(ACE, SPADES), new Card(TEN, CLUBS), // dealer cannot win
					// second round
					new Card(KING, CLUBS), new Card(KING, HEARTS), new Card(NINE, CLUBS), // human lost
					new Card(ACE, CLUBS), new Card(QUEEN, HEARTS), // BOT blackjack
					new Card(ACE, HEARTS), new Card(JACK, SPADES), // BOT blackjack
					new Card(KING, SPADES), new Card(SIX, CLUBS), // dealer wins human

					new Card(KING, CLUBS), new Card(ACE, HEARTS), // human wins
					new Card(TEN, CLUBS), new Card(QUEEN, HEARTS), // BOT blackjack
					new Card(ACE, HEARTS), new Card(JACK, SPADES), // BOT blackjack
					new Card(KING, SPADES), new Card(TEN, CLUBS) // dealer cannot win
			);
			runCommands(blackJack, "LR",
					// first round
					"100", " ", " ", " ", "yes",
					// second round
					"100", "hit", " ", " ", " ", "yes",
					// third round dealer strategy should be changed
					// the human is not the top bidder, but should be still targeted by the dealer
					"100", " ", " ", " ", "no");
			assertContains("Player1 won 1 times and lost 2 times");
			assertContains("Bot1 won 1 times and lost 2 times");
			assertContains("Bot2 won 2 times and lost 1 times");

			for (Integer i : Arrays.asList(0, 2, 3)) {
				for (Integer j : Arrays.asList(0, 2, 3)) {
					for (Integer k : Arrays.asList(0, 1, 3)) {
						assertDoesNotContain("Player1 won " + i + " times and lost " + j + " times");
						assertDoesNotContain("Bot1 won " + j + " times and lost " + i);
						assertDoesNotContain("Bot2 won " + k + " times and lost " + j);
					}
				}
			}

		}

	}

	@FixMethodOrder(MethodSorters.NAME_ASCENDING)
	public static class YourTest extends TaskTest {

	}

}
