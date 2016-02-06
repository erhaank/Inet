

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
	private BufferedReader request;

	/**
	 * Sets up a ServerSocket
	 */
	public HttpServer() {
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
			String requestedDoc = handleRequest();
			respond(requestedDoc);
			tearDownSocket();
		}
	}

	/**
	 * Sets up the current socket and the BufferedReader
	 */
	private void setupSocket() {
		try {
			s = ss.accept();
			request = new BufferedReader(new InputStreamReader(s.getInputStream()));
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
		request = null;
	}

	/**
	 * Handles the incoming request by printing the http header and gets the 
	 * parameter of the request and returns it.
	 */
	private String handleRequest() {
		String requestedDocument = "";
		try {
			String str = request.readLine();
			System.out.println(str);
			StringTokenizer tokens =
					new StringTokenizer(str," ?");
			tokens.nextToken();
			requestedDocument = tokens.nextToken();
			while( (str = request.readLine()) != null && str.length() > 0){
				System.out.println(str);
			}
			s.shutdownInput();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("*ERROR* Fault in handleRequest");
		}
		return requestedDocument;
	}

	/**
	 * Takes the request parameter and uses the OutputGenerator to generate a response to 
	 * the client, which it then sends.
	 */
	private void respond(String requestedDoc) {
		try {
			PrintStream response =
					new PrintStream(s.getOutputStream());
			response.println("HTTP/1.0 200 OK");
			response.println("Server : Slask 0.1 Beta");
			String contentType = null;
			if(requestedDoc.indexOf(".html") != -1) // Varför inte reqDoc.endsWith(".html") ?
				contentType = "Content-Type: text/html";
			else if(requestedDoc.indexOf(".gif") != -1) // Samma här
				contentType = "Content-Type: image/gif";

			if (contentType != null) {
				response.println(contentType);
				response.println("Set-Cookie: clientId=1; expires=Wednesday,31-Dec-2017 21:00:00 GMT");

				response.println();
				File f = new File("."+requestedDoc);
				FileInputStream infil = new FileInputStream(f);
				byte[] b = new byte[1024];
				while( infil.available() > 0){
					response.write(b,0,infil.read(b));
				}
				infil.close();
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

