

import java.io.IOException;
import java.net.Socket;

public class Main {

	public static void main(String[] args) {
		Client client = new Client();
		Socket socket = null;
		try {
			socket = new Socket("localhost", 8888);
    	} catch (IOException e) {
    		//TODO maybe they should be in 2 try catch instead...
    	}
		client.startChat(socket);
	}
}
