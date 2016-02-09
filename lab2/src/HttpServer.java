

import java.io.*;
import java.net.*;
import java.util.StringTokenizer;
/**
 * Basic HTTP Server that assumes that the client requests an html document.
 * Uses the OutputGenerator to generate a html.
 */
public class HttpServer {

	private ServerSocket ss;
	private Socket s;
	private BufferedReader reader;
	private int ID;
	private OutputGenerator generator;

	/**
	 * Sets up a ServerSocket
	 */
	public HttpServer() {
		ID = 0;
		generator = new OutputGenerator();
		try {
			ss = new ServerSocket(4711);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Main loop of program.
	 */
	public void run() {
		while (true) {
			setupSocket();
			HttpRequest request = handleRequest();
			respond(request);
			tearDownSocket();
		}
	}

	/**
	 * Sets up the current socket and the BufferedReader
	 */
	private void setupSocket() {
		try {
			s = ss.accept();
			reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("ERROR* Fault in setupSocket");
		}

	}

	/**
	 * Closes the current socket and nullifies s and request
	 */
	private void tearDownSocket() {
		try {
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("*ERROR* Fault in setupSocket");
		}
		s = null;
		reader = null;
	}

	/**
	 * Handles the incoming request by printing the http header and gets the 
	 * parameter of the request and returns it.
	 */
	private HttpRequest handleRequest() {
		HttpRequest request = new HttpRequest();
		try {
			String str = reader.readLine();
			System.out.println("* New *");
			System.out.println(str);
			request.guess = getGuess(str);
			StringTokenizer tokens =
					new StringTokenizer(str," ?");
			System.out.println(tokens.nextToken());
			request.document = tokens.nextToken();
			while( (str = reader.readLine()) != null && str.length() > 0){
				System.out.println(str);
				if (str.startsWith("Cookie:"))
					request.cookie = str.substring(7);
			}
			s.shutdownInput();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("*ERROR* Fault in handleRequest");
		}
		System.out.println("\n\n\n\n\n");
		return request;
	}

	/**
	 * Gets the integer from a guess. 
	 * If there has been a guess, request will be something like "GET /test.html?guess=25 HTTP/1.1"
	 * In this case we want to get the "25" part of it and return it as an integer.
	 * If the guess isn't a number, or if it simply isn't a guess request, we return -1
	 */
	private int getGuess(String request) {
		int ret = -1;
		String[] s = request.split("\\?");
		if (s.length == 2 && s[1].startsWith("guess=")) {
			String guess = s[1].substring(6); // remove the 'guess=' part
			guess = guess.split(" ")[0]; // remove the ' HTTP/1.1' part
			try {
				ret = Integer.parseInt(guess);
			} catch (NumberFormatException e) {
				//Do nothing, ret is already -1
			}
		}
		return ret;
	}

	/**
	 * Takes the request parameter and uses the OutputGenerator to generate a response to 
	 * the client, which it then sends.
	 */
	private void respond(HttpRequest request) {
		try {
			PrintStream response =
					new PrintStream(s.getOutputStream());
			response.println("HTTP/1.0 200 OK");
			response.println("Server : Slask 0.1 Beta");
			String contentType = null;
			if(request.document.indexOf(".html") != -1)
				contentType = "Content-Type: text/html";
			else if(request.document.indexOf(".ico") != -1)
				contentType = "Content-Type: image/gif";

			if (contentType != null) {
				response.println(contentType);
				if (request.cookie == null) {
					String cookie = "clientId="+ID;
					ID++;
					response.println("Set-Cookie: "+cookie+"; expires=Wednesday,31-Dec-2017 21:00:00 GMT");
					generator.addClient(cookie);
				}

				response.println();

				String html = generator.generateHTML(request.cookie, request.guess);
				byte[] b = html.getBytes();
				response.write(b);
			}
			s.shutdownOutput();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("*ERROR* Fault in respons");
		}
	}

	public static void main(String[] args) throws IOException{
		HttpServer server = new HttpServer();
		server.run();
	}

}

