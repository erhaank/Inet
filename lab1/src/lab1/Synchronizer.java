package lab1;

import java.util.HashMap;

/**
 * Will work as the "static" part of the program, keeping track on which clients are logged on as well as
 * being able to send messages to each client.
 */
// ÄR det här för fult? Vet inte hur jag ska göra annars?
public class Synchronizer {
	HashMap<String, Connection> connections;
	
	public Synchronizer() {
		connections = new HashMap<String, Connection>();
	}
	
	public boolean hasUser(String name) {
		return connections.containsKey(name);
	}
	
	public void addClient(String name, Connection con) {
		connections.put(name, con);
	}
	
	public synchronized void distribute(Message msg) {
		if (!connections.containsKey(msg.getReceiver()))
			return;
		//TODO
		//Connection receiver = connections.get(msg.getReceiver());
		//receiver.addToBuffer(msg);
	}
}