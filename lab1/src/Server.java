

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
		while (true) {
			Socket client = socket.accept();
			DataOutputStream out = new DataOutputStream(client.getOutputStream());
            DataInputStream in = new DataInputStream(client.getInputStream());
            //Get username, client should handle this part
            String user = in.readUTF();
            if (!sync.hasUser(user)) {
            	out.writeInt(1);
            	sync.addClient(user, new Connection(user, client, sync));
            	System.out.println("New user added: "+user);
            }
            else {
            	out.writeInt(0);
            	client.close();
            	System.out.println(user + " was rejected.");
            }
            in.close();
            out.close();
		}
	}
}
