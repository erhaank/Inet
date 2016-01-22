package lab1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

/**
 * A connection is connected to a specific Client.
 */
public class Connection extends Thread {
	
	private Synchronizer sync;
	private Socket client;
	private DataOutputStream out;
	private DataInputStream in;
	private LinkedList<String> messages;
	private Set<String> usersWithMessages;
	//TODO: Protocol, buffer and stuff

	public Connection(Socket client, DataOutputStream out, DataInputStream in, Synchronizer sync) {
		this.client = client;
		this.out = out;
		this.in = in;
		this.sync = sync;
		messages = new LinkedList<String>();
		usersWithMessages = new Set<String>();
	}
	
	public void addToBuffer(Message msg) {
		messages.push(msg);
		String sender = msg.getSender();
		if(!usersWithMessages.contains(sender)) {
			usersWithMessages.put(sender);
		}
	}

	public void viewChatRoom() {
		String[] users = sync.getUsers();
		for(String user : users) {
			if(usersWithMessages.contains(user)) {
				out.writeUTF(user + "*");
			} else {
				out.writeUTF(user);
			}
		}
	}
}
