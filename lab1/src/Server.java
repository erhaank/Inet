// package lab1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	
	private Synchronizer sync;
	private int ID;

	public Server(Synchronizer sync) {
		this.sync = sync;
		ID = 0; //What is the purpose of ID?
	}
	
	public void listenToConnections() throws IOException {
		@SuppressWarnings("resource")
		ServerSocket socket = new ServerSocket(8888);
		System.out.println("Server running and listening");
		while (true) {
			ID++; 
			Socket client = socket.accept();
			DataOutputStream out = new DataOutputStream(client.getOutputStream());
            DataInputStream in = new DataInputStream(client.getInputStream());
            //Get username, client should handle this part
            String user = in.readUTF();
            boolean valid = validUsername(user);
            if (valid) {
            	out.writeInt(1);
            	user = ID+":"+user;
            	Connection c = new Connection(user, client, sync);
            	sync.addClient(user, c);
            	c.start();
            	System.out.println("New user added: "+user);
            }
            else {
            	out.writeInt(0);
            	client.close();
            	System.out.println(user + " was rejected.");
            }
		}
	}
	
	private boolean validUsername(String username) {
		if (username.contains(":"))
			return false;
		for (String user: sync.getUsers()) {
			user = user.substring(user.indexOf(":")+1);
			if (username.equals(user))
				return false;
		}
		return true;
	}
}
