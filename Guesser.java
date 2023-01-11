package wordleGuesser;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Guesser {

	ArrayList<String> solutionWords;
	ArrayList<String> allWords;
	String solutionPath = "C:\\Users\\socce\\eclipse-workspace\\PersonalProjects\\src\\wordleGuesser\\SolutionWords.txt";
	String allPath = "C:\\Users\\socce\\eclipse-workspace\\PersonalProjects\\src\\wordleGuesser\\AllWords.txt";

	public Guesser() {
		solutionWords = new ArrayList<>();
		putWordsInList(solutionPath, solutionWords);

		allWords = new ArrayList<>();
		putWordsInList(allPath, allWords);
	}

	/**
	 * @param filePath
	 * 
	 *                 reads the file at filePath and puts each line into a separate
	 *                 index in wordList
	 */
	public void putWordsInList(String filePath, ArrayList<String> list) {
		try {
			// open file and scan
			File file = new File(filePath);
			Scanner scanner = new Scanner(file);

			while (scanner.hasNextLine()) {
				list.add(scanner.nextLine());
			}
			scanner.close();
		} catch (Exception FileNotFoundException) {
			System.out.println(FileNotFoundException);
		}

	}

	/**
	 * @return
	 */
	public String getGuessWord() {
		Scanner scanner = new Scanner(System.in);
		String guess;

		// get guess in a String and make sure it is valid
		while (true) {
			guess = scanner.next().toLowerCase();

			if (guess.length() != 5)
				System.out.print("Word must be 5 letters, try again: ");
			
			else if (allWords.contains(guess))
				return guess;
			
			else
				System.out.print("Not a valid word, try again: ");
		}

	}

	/**
	 * @return
	 */
	public static String getGuessPattern() {
		Scanner scanner = new Scanner(System.in);
		String pattern = "";
		boolean valid = true;
		boolean length = true;
		while (valid || length) {
			valid = false;
			length = false;
			pattern = scanner.next();
			if (pattern.length() != 5) {
				System.out.println("Pattern must be 5 characters long, try again: ");
				length = true;
			} else {
				for (int i = 0; i < 5; i++) {
					char currentChar = pattern.charAt(i);
					if (currentChar != '-' && currentChar != 'Y' && currentChar != 'G') {
						System.out.println("Invalid pattern, try again: ");
						valid = true;
						break;
					}
				}
			}
		}
		return pattern;
	}

	/**
	 * @param letter
	 * @param index
	 */
	public void green(char letter, int index) {
		for (int i = 0; i < solutionWords.size(); i++) {
			if (solutionWords.get(i).charAt(index) != letter) {
				solutionWords.remove(i);
				i--;
			}
		}
	}

	/**
	 * @param letter
	 * @param index
	 */
	public void yellow(char letter, int index) {
		for (int i = 0; i < solutionWords.size(); i++) {
			if (solutionWords.get(i).charAt(index) == letter) {
				solutionWords.remove(i);
				i--;
			} else {
				for (int j = 0; j < 5; j++) {
					if (solutionWords.get(i).charAt(j) == letter) {
						break;
					} else {
						if (j == 4) {
							solutionWords.remove(i);
							i--;
						}
					}
				}
			}
		}
	}

	/**
	 * @param letter
	 */
	public void gray(char letter) {
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
	 * @param guess
	 * @param pattern
	 * @return
	 */
	public static String checkForDoubleLetters(String guess, String pattern) {
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
	 * 
	 */
	public void start() {
		for (int passes = 0; passes < 5; passes++) {
			System.out.print("\n" + "Enter your guessed word: ");
			String guess = getGuessWord();
			System.out.print("Enter the result (G for green, Y for yellow, '-' for gray): ");
			String pattern = getGuessPattern();

			guess = checkForDoubleLetters(guess, pattern);

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

			if (solutionWords.size() == 1) {
				System.out.println("The word is '" + solutionWords.get(0) + "'");
				break;
			}
			if (solutionWords.size() == 0) {
				System.out.println("Something was entered incorrectly, restart from the beginning");
				break;
			}
			System.out.println("All possible remaining words are listed below");
			System.out.println(solutionWords);
		}
	}
}
