package lab1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

/**
 * A connection is connected to a specific Client.
 */
public class Connection extends Thread {

	private String clientName;
	private Synchronizer sync;
	private Socket client;
	private DataOutputStream out;
	private DataInputStream in;
	private Protocol protocol;
	private LinkedList<Message> messages;
	private Set<String> usersWithMessages;
	private ConnectionWriteThread write;
	private ConnectionReadThread read;
	//TODO it is not a buffer anymore, but still it is called addtobuffer...

	private class Protocol {
	
		private enum State {
			OPTIONS, CHATROOM, CHATTING, LOGOUT, EXIT, UNCHANGED
		}

		private State currentState;

		public Protocol() {
			currentState = OPTIONS;
		}

		public void setState(State state) {
			currentState = state;
		}

		public State getState() {
			return currentState;
		}
	}

	private class ConnectionWriteThread extends Thread {
		private User user;

		public ConnectionWriteThread(User receiver) {
			this.user = user;
		}
		public void run() {
			while(!this.isInterrupted()) {
				String s = in.readUTF();
				if(s.equals("EXIT")) {
					endChat();
				} else {
					Message msg = new Message(clientName, receiver);
					msg.setMessage(s);
					sync.distribute(msg);
				}
			}
		}
	}

	private class ConnectionReadThread extends Thread {
		private User user;

		public ConnectionReadThread(User sender) {
			this.user = user;
		}
		public void run() {
			while(!this.isInterrupted()) {
				if(usersWithMessages.contains(sender)) {
					writeMessagesFrom(sender);
				}
			}
		}
	}

	public Connection(String clientName, Socket client, DataOutputStream out, DataInputStream in, Synchronizer sync) {
		this.clientName = clientName;
		this.client = client;
		this.out = out;
		this.in = in;
		this.sync = sync;
		protocol = new Protocol();
		messages = new LinkedList<String>();
		usersWithMessages = new Set<String>();

	}

	public void run() {
		while(true) {
			switch(protocol.getState()) {
				case OPTIONS:
					viewOptions();
					break;
				case CHATROOM:
					viewChatRoom();
					break;
				case CHATTING:
					startChat();
					break;
				case EXIT:
					//TODO
					break;
			}
		}
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

	public void viewOptions() {
		out.writeUTF("Chat room (1)\n");
		out.writeUTF("Log out (2)\n");
		out.writeUTF("Exit (3)\n");
		String s = in.readUTF();
		switch (s) {
			case "1":
				protocol.setState(CHATROOM);
				break;
			case "2":
				protocol.setState(LOGOUT);
				break;
			case "3":
				protocol.setState(EXIT);
				break;
			default:
				out.writeUTF(s + " is not a valid option.");
				protocol.setState(UNCHANGED);
				break;
		}
	}

	public void startChat(String user) {
		read = new ConnectionReadThread(user);
		read.run();
		write = new ConnectionWriteThread(user);
		write.run();
		protocol.setState(UNCHANGED);
	}

	public void writeMessagesFrom(User user) {
		Iterator<Message> i = messages.descendingIterator();
		while(i.hasNext) {
			Message msg = i.next();
			if(user == msg.getSender()) {
				out.writeUTF(msg.getMessage());
			}
		}
		if(usersWithMessages.contains(user)) {
			usersWithMessages.remove(user);
		}
	}

	public void endChat() {
		read = interrrupt();
		write = interrrupt();
		protocol.setState(OPTIONS);
	}
}
