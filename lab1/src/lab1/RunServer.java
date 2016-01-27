// package lab1;

import java.io.IOException;

public class RunServer {
	public static void main(String[] args) {
		Synchronizer sync = new Synchronizer();
		Server server = new Server(sync);
		try {
			server.listenToConnections();
		} catch (IOException e) {
			// Server cannot listen to new connections, TODO
		}
	}
}
