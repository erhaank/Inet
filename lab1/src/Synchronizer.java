// package lab1;

import java.util.HashMap;
import java.util.Set;

/**
 * Will work as the "static" part of the program, keeping track on which clients are logged on as well as
 * being able to send messages to each client.
 */
public class Synchronizer {
	
	private HashMap<String, Connection> connections;
	
	public Synchronizer() {
		connections = new HashMap<String, Connection>();
	}
	
	public boolean hasUser(String name) {
		update();
		return connections.containsKey(name);
	}
	
	public void addClient(String name, Connection con) {
		connections.put(name, con);
	}
	
	public synchronized void distribute(Message msg) {
		if (!connections.containsKey(msg.getReceiver()))
			return;
		System.out.println("Distributing msg from "+msg.getSender() + " to " + msg.getReceiver());
		// System.out.println("In distribute");
		Connection receiver = connections.get(msg.getReceiver());
		// System.out.println(msg.getReceiver());
		receiver.addToBuffer(msg);
		// System.out.println("have done addToBuffer");
	}
	
	public String[] getUsers() {
		update();
		Set<String> users = connections.keySet();
		String[] ret;
		if (users.size() > 0)
			ret = (String[]) users.toArray(new String[users.size()]);
		else {
			ret = new String[]{};
		}
		return ret;
	}
	
	private void update() {
		for (String s : connections.keySet()) {
			Connection c = connections.get(s);
			if (!c.isAlive()) {
				System.out.println(s+ " has logged out");
				connections.remove(s);
			}
		}
	}
}
