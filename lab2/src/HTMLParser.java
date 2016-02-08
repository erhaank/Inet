import java.util.Scanner;
import java.nio.file.Paths;
import java.nio.file.Path;



public class HTMLParser {

	private Scanner scanner;

	public HTMLParser() {
		String fileName = "test.html";
        Path path = Paths.get(fileName);
        try {
        	Scanner scanner = new Scanner(path);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
	}

	public String generateHTML(String ret) {
		scanner.useDelimiter("\\Z"); //Makes the whole file a token
        String html = scanner.next();
        if(ret != "INVALID") {
        	html.replaceAll("<p>(.*?)</p>", ret);
		}
        return html;
	}
	
}
