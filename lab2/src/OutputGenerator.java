import java.util.HashMap;


public class OutputGenerator {

	private HTMLParser parser;
	private HashMap<String, GuessStatus> users;
	
	public OutputGenerator() {
		users = new HashMap<String, GuessStatus>();
		parser = new HTMLParser();
	}
	
	public String generateHTML(String cookie, int guess) {
		String ret = null;
		if (!users.containsKey(cookie))
			addClient(cookie);
		GuessStatus status = users.get(cookie);
		status.setGuess(guess);
		String html = parser.generateHTML(status);
		return html;
	}
	
	public void addClient(String clientCookie) {
		GuessStatus newGuess = new GuessStatus();
		newGuess.reset();
		users.put(clientCookie, newGuess);
	}
}
