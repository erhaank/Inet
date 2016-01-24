package lab1;

import java.io.IOException;
import java.net.Socket;

public class Main {

	public static void main(String[] args) {
		Socket socket = null;
		try {
			socket = new Socket("localhost", 8888);
    	} catch (IOException e) {
    		//TODO maybe they should be in 2 try catch instead...
    	}
    	Client client = new Client(socket);
		client.getUsername();
		client.startChat();
	}
}
