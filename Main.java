package WordleGuesser;

import java.util.Scanner;

/**
 * This class prints instructions to the console for the user on how to operate
 * the guesser. It then starts the guesser when the user is ready. 
 * 
 * @author Sean McGuirk
 * @version January 11, 2023
 *
 */
public class Main {

	public static void main(String args[]) {

		Guesser guess = new Guesser();

		// instructions for use
		System.out.println("Welcome to Wordle Guesser!");
		System.out.println("\n" + "How to Use:");
		System.out.println("1. Enter the word you guessed");
		System.out.println("2. Enter the colors wordle returned for your guess");
		System.out.println("	a. G is for Green, Y is for Yellow, and '-' is for gray");
		System.out.println("	b. Examples are GG--Y, --YG-, Y----, etc.");
		System.out.println("3. Wordle Guesser will return a list of all possible remaining words");
		System.out.println("4. Repeat these steps with your next guess");
		System.out.print("\n" + "Type 'ready' to start: ");

		// begin the game when the user types ready
		Scanner scan = new Scanner(System.in);
		while (true) {
			if (scan.next().equals("ready"))
				break;
		}
		guess.start();

	}
}
