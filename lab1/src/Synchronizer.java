// package lab1;

import java.util.HashMap;
import java.util.Iterator;
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
	
	public boolean hasUser(String name) { //Method is not used!
		update();
		return connections.containsKey(name);
	}
	
	public void addClient(String name, Connection con) {
		connections.put(name, con);
	}
	
	public synchronized boolean distribute(Message msg) {
		if (!connections.containsKey(msg.getReceiver()))
			return false;
		Connection receiver = connections.get(msg.getReceiver());
		receiver.addToBuffer(msg);
		return true;
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
		Iterator<String> it = connections.keySet().iterator();
		while (it.hasNext()) {
			Connection c = connections.get(it.next());
			if (!c.isAlive()) {
				//User has logged out
				it.remove();
			}
		}
	}
	
	public void shutdown() {
		Iterator<Connection> it = connections.values().iterator();
		while (it.hasNext()) {
			Connection c = it.next();
			c.interrupt();
			it.remove();
		}
	}
}
