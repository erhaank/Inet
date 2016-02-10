import java.util.Scanner;
import java.nio.file.Paths;
import java.nio.file.Path;



public class HTMLParser {

	private Scanner scanner;
    private GuessStatus guess;

	public String generateHTML(GuessStatus guess) {
        String ret = guess.success();
		String fileName = "test.html";
		Path path = Paths.get(fileName);
		try {
        	scanner = new Scanner(path);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
		scanner.useDelimiter("\\Z"); //Makes the whole file a token
        String html = scanner.next();
        if(ret == "You made it!!!")
        	html = html.replaceAll("<form.*\\n*.*\\n*.*\\n*.*</form>", ""); //Remove the form
        	//Might be done with a nicer regex...
		if(ret != "INVALID") {
            int guesses = guess.getNumberOfGuesses();
            StringBuilder sb = new StringBuilder(ret);
            sb.append("<br>");
            sb.append("Number of guesses: " + guesses);
            ret = sb.toString();
        	html = html.replaceAll("(?<=<p>)(.+)(?=</p>)", ret); //Replace the content of <p></p> with ret
        }
        return html;
	}
}
