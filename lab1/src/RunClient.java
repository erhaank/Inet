//package lab1;

import java.io.IOException;
import java.net.Socket;

public class RunClient {

	public static void main(String[] args) {
		while (true) {
			Client client = new Client();
			Socket socket = null;
			try {
				socket = new Socket("localhost", 8888);
				// socket = new Socket("130.237.223.175", 8888);
			} catch (IOException e) {
				//TODO maybe they should be in 2 try catch instead...
			}
			client.startChat(socket);
		}
	}
}
