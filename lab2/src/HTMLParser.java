import java.util.Scanner;
import java.nio.file.Paths;
import java.nio.file.Path;



public class HTMLParser {

	private Scanner scanner;

	public HTMLParser() {
		String fileName = "test.html";
        Path path = Paths.get(fileName);
        try {
        	scanner = new Scanner(path);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
	}

	public String generateHTML(String ret) {
		scanner.useDelimiter("\\Z"); //Makes the whole file a token
        String html = scanner.next();
        if(ret == "You made it!!!")
        	html = html.replaceAll("<form.*\\n*.*\\n*.*\\n*.*</form>", ""); //Remove the form
        	//Might be done with a nicer regex...
		if(ret != "INVALID")
        	html = html.replaceAll("(?<=<p>)(.+)(?=</p>)", ret); //Replace the content of <p></p> with ret
        return html;
	}
}
