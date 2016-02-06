import java.util.HashMap;


public class OutputGenerator {

	//private HTMLParser parser;
	private HashMap<String, GuessStatus> users;
	
	public OutputGenerator() {
		users = new HashMap<String, GuessStatus>();
	}
	
	public String generateHTML(String cookie, int guess) {
		String ret = null;
		GuessStatus status = users.get(cookie);
		status.setGuess(guess);
		ret = status.success(); // if ret = 'INVALID' then no valid guess has yet been made, make sure
								// that it will return the start page.
		//Do stuff
		return ret;
	}
	
	public void addClient(String clientCookie) {
		GuessStatus newGuess = new GuessStatus();
		newGuess.reset();
		users.put(clientCookie, newGuess);
	}
}
