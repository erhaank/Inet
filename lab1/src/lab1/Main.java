package lab1;

public class Main {

	public static void main(String[] args) {		
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
