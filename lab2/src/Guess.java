import java.util.Random;

/*
 * Basically a copy of guess.php, but in Java
 */
public class Guess {

	private int secretNumber, numberOfGuesses, guess, guessLow, guessHigh;
	
	public Guess() {
		guessLow = -1;
		guessHigh = 101;
	}

	public void reset() {
		Random r = new Random();
		secretNumber = r.nextInt(101);
		numberOfGuesses = 0;
		guessLow = -1;
		guessHigh = 101;
	}
	
	public void setGuess(int newGuess) {
		if (newGuess < guessHigh && newGuess > guessLow) {
			guess = newGuess;
			numberOfGuesses++;
			if (guess > secretNumber)
				guessHigh = guess;
			else if (guess < secretNumber)
				guessLow = guess;
		}
	}

	public String success() {
		String ret = "";
		if (secretNumber == guess)
			ret = "You made it!!!";
		else if (guessLow != -1 && guessHigh != 101)
			ret = "Nope, guess a number between "+guessLow+" and "+guessHigh;
		else if (secretNumber < guess)
			ret = "Nope, guess lower";
		else if (secretNumber > guess)
			ret = "Nope, guess higher";
		return ret;
	}

}
