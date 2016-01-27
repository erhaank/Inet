// package lab1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	
	private Synchronizer sync;

	public Server(Synchronizer sync) {
		this.sync = sync;
	}
	
	public void listenToConnections() throws IOException {
		@SuppressWarnings("resource")
		ServerSocket socket = new ServerSocket(8888);
		System.out.println("Server running and listening");
		while (true) {
                  System.out.println("yo");
			Socket client = socket.accept();
			System.out.println("Client accepted!");
			DataOutputStream out = new DataOutputStream(client.getOutputStream());
            DataInputStream in = new DataInputStream(client.getInputStream());
            //Get username, client should handle this part
            String user = in.readUTF();
            System.out.println(user);
            if (!sync.hasUser(user)) {
                  System.out.println("heluu");
            	out.writeInt(1);
            	Connection c = new Connection(user, client, sync);
            	sync.addClient(user, c);
                  System.out.println("heluu2");
            	c.start();
            	System.out.println("New user added: "+user);
            }
            else {
            	out.writeInt(0);
            	client.close();
            	System.out.println(user + " was rejected.");
            }
            // in.close();
            // out.close();
		}
	}
}
