package lab1;

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
            if (!sync.hasUser(user))
            	sync.addClient(user, new Connection(client, out, in, sync));
            else
            	client.close(); //TODO: Maybe do this in a better way... Although the client could maybe fix this?
		}
	}
}
