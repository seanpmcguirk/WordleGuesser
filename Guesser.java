package WordleGuesser;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Wordle (https://www.nytimes.com/games/wordle/index.html) contains two
 * separate word lists. One list contains over 12,000 words which it accepts as
 * valid guesses. The second list contains just over 2,000 words which it uses
 * as solutions. This class uses these two lists to allow the user to narrow
 * down and find the correct solution to the daily Wordle puzzle.
 * 
 * @author Sean McGuirk
 * @version January 11, 2023
 *
 */
public class Guesser {

	ArrayList<String> solutionWords;
	ArrayList<String> allWords;
	String solutionPath = "src\\wordleGuesser\\SolutionWords.txt";
	String allPath = "src\\wordleGuesser\\AllWords.txt";

	public Guesser() {
		solutionWords = new ArrayList<>();
		putWordsInList(solutionPath, solutionWords);

		allWords = new ArrayList<>();
		putWordsInList(allPath, allWords);
	}

	/**
	 * Reads the file at filePath and puts each line into a separate index in the
	 * list.
	 * 
	 * @param filePath
	 */
	private void putWordsInList(String filePath, ArrayList<String> list) {
		try {
			// open file and scan
			File file = new File(filePath);
			Scanner scan = new Scanner(file);

			// add each line to list
			while (scan.hasNextLine()) {
				list.add(scan.nextLine());
			}
			scan.close();

		} catch (Exception e) {
			System.out.println(e);
		}

	}

	/**
	 * Gets a valid 5 letter word from the user. A valid word is any of the words
	 * contained in allWords.
	 * 
	 * @return guess
	 */
	private String getGuessWord() {
		Scanner scan = new Scanner(System.in);
		String guess;

		// get guess in a String and check that it is valid
		while (true) {
			guess = scan.next().toLowerCase();
			if (guess.length() != 5)
				System.out.print("Word must be 5 letters, try again: ");

			else if (allWords.contains(guess))
				return guess;

			else
				System.out.print("Not a valid word, try again: ");
		}

	}

	/**
	 * Gets a valid color pattern entered by the user from the Wordle game online. A
	 * valid pattern consists of only 'Y', 'G', or '-' and has a length of 5
	 * 
	 * @return pattern
	 */
	private static String getGuessPattern() {
		Scanner scan = new Scanner(System.in);
		String pattern = "";
		boolean correctChars = true;
		boolean correctLength = true;

		// take in input from the user until the pattern is valid
		while (correctChars || correctLength) {
			correctChars = false;
			correctLength = false;
			pattern = scan.next();
			// check length
			if (pattern.length() != 5) {
				System.out.println("Pattern must be 5 characters long, try again: ");
				correctLength = true;
			} else {
				// check characters
				for (int i = 0; i < 5; i++) {
					char currentChar = pattern.charAt(i);
					if (currentChar != '-' && currentChar != 'Y' && currentChar != 'G') {
						System.out.println("Invalid pattern, try again: ");
						correctChars = true;
						break;
					}
				}
			}
		}
		return pattern;
	}

	/**
	 * Removes every word in solutionWords that does not have the letter at the
	 * index.
	 * 
	 * @param letter - green letter in a guess word
	 * @param index  - location of the green letter
	 */
	private void green(char letter, int index) {
		for (int i = 0; i < solutionWords.size(); i++) {
			if (solutionWords.get(i).charAt(index) != letter) {
				solutionWords.remove(i);
				i--;
			}
		}
	}

	/**
	 * Removes every word in solutionWords that has the letter at the index. Also
	 * removes every word in solutionWords that does not contain the letter
	 * somewhere in the word.
	 * 
	 * @param letter
	 * @param index
	 */
	private void yellow(char letter, int index) {
		for (int i = 0; i < solutionWords.size(); i++) {
			// remove words with letter at index
			if (solutionWords.get(i).charAt(index) == letter) {
				solutionWords.remove(i);
				i--;

				// remove words that do not contain letter somewhere else
			} else {
				for (int j = 0; j < 5; j++) {
					if (solutionWords.get(i).charAt(j) == letter)
						break;
					else if (j == 4) {
						solutionWords.remove(i);
						i--;
					}
				}
			}
		}
	}

	/**
	 * Removes every word in solutionWords that contains letter
	 * 
	 * @param letter
	 */
	private void gray(char letter) {
		for (int i = 0; i < solutionWords.size(); i++) {
			for (int j = 0; j < 5; j++) {
				if (solutionWords.get(i).charAt(j) == letter) {
					solutionWords.remove(i);
					i--;
					break;
				}
			}
		}
	}

	/**
	 * Guesses with double letters can create problems when removing words from
	 * solutionWords. If the pattern is one that will create problems then at least
	 * one of the double letters in guess will be change to '?' to avoid problems
	 * when removing from solutionWords.
	 * 
	 * @param guess
	 * @param pattern
	 * @return
	 */
	private static String checkForDoubleLetters(String guess, String pattern) {
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if ((pattern.charAt(i) == 'G' || pattern.charAt(i) == 'Y') && guess.charAt(i) == guess.charAt(j)
						&& i != j && pattern.charAt(j) != 'G' && pattern.charAt(j) != 'Y') {
					guess = guess.substring(0, j) + '?' + guess.substring(j + 1);
				}
			}
		}
		return guess;
	}

	/**
	 * allows the user to input up to five guesses. Each guess/pattern is checked
	 * for double letters then calls the green, yellow, and gray methods to
	 * eliminate words from solutionWords. Prints solutionsWords after every guess
	 * until five guesses are used or the solution is found.
	 */
	public void start() {

		for (int passes = 0; passes < 5; passes++) {
			// get the guess and pattern from the user
			System.out.print("\n" + "Enter your guessed word: ");
			String guess = getGuessWord();
			System.out.print("Enter the result (G for green, Y for yellow, '-' for gray): ");
			String pattern = getGuessPattern();

			// eliminate double letters
			guess = checkForDoubleLetters(guess, pattern);

			// call either green, yellow, or gray for each character in guess
			for (int i = 0; i < 5; i++) {
				char currentPatternChar = pattern.charAt(i);
				char currentGuessChar = guess.charAt(i);
				if (currentPatternChar == 'G' && currentGuessChar != '?') {
					green(currentGuessChar, i);
				}
				if (currentPatternChar == 'Y' && currentGuessChar != '?') {
					yellow(currentGuessChar, i);
				}
				if (currentPatternChar == '-' && currentGuessChar != '?') {
					gray(currentGuessChar);
				}
			}

			// one word is left so something it is the answer
			if (solutionWords.size() == 1) {
				System.out.println("The word is '" + solutionWords.get(0) + "'");
				break;
			}
			// zero words are left so something was entered wrong
			if (solutionWords.size() == 0) {
				System.out.println("Something was entered incorrectly, restart from the beginning");
				break;
			}
			// print possible solutions
			System.out.println("All possible remaining words are listed below");
			System.out.println(solutionWords);
			if (passes == 4) {
				System.out.println("Last chance, Good Luck");
			}
		}
	}
}
