


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

/**
 * A connection is connected to a specific Client.
 */
public class Connection extends Thread {

	private String clientName;
	private Synchronizer sync;
	private DataOutputStream out;
	private DataInputStream in;
	private Protocol protocol;
	private LinkedList<Message> messages;
	private Set<String> usersWithMessages;
	private ConnectionWriteThread write;
	private ConnectionReadThread read;
	//TODO it is not a buffer anymore, but still it is called addtobuffer...

	public Connection(String clientName, Socket client, DataOutputStream out, DataInputStream in, Synchronizer sync) {
		this.clientName = clientName;
		this.out = out;
		this.in = in;
		this.sync = sync;
		protocol = new Protocol();
		messages = new LinkedList<Message>();
		usersWithMessages = new HashSet<String>();
	}

	public void run() {
		try {
			go();
		} catch (IOException e) {
			// TODO
		}
	}

	//TODO: Better name
	private void go() throws IOException {
		while(true) {
			switch(protocol.getState()) {
			case OPTIONS:
				viewOptions();
				break;
			case CHATROOM:
				viewChatRoom();
				break;
			case CHATTING:
				//startChat();
				break;
			case EXIT:
				//TODO
				break;
			default:
				//TODO
			}
		}
	}

	public void addToBuffer(Message msg) {
		messages.push(msg);
		String sender = msg.getSender();
		usersWithMessages.add(sender);
	}

	private void viewChatRoom() throws IOException {
		String[] users = sync.getUsers();
		StringBuilder sb = new StringBuilder();
		for(String user : users) {
			if(usersWithMessages.contains(user)) {
				sb.append(user + "*");
			} else {
				sb.append(user);
			}
		}

		out.writeUTF(sb.toString());
	}

	private void viewOptions() throws IOException {
		out.writeUTF("Chat room (1)\n");
		out.writeUTF("Log out (2)\n");
		out.writeUTF("Exit (3)\n");
		String s = in.readUTF();
		switch (s) {
		case "1":
			protocol.setState(ConnectionState.CHATROOM);
			break;
		case "2":
			protocol.setState(ConnectionState.LOGOUT);
			break;
		case "3":
			protocol.setState(ConnectionState.EXIT);
			break;
		default:
			out.writeUTF(s + " is not a valid option.");
			protocol.setState(ConnectionState.UNCHANGED);
			break;
		}
	}

	public void startChat(String user) {
		read = new ConnectionReadThread(user);
		read.run();
		write = new ConnectionWriteThread(user);
		write.run();
		protocol.setState(ConnectionState.UNCHANGED);
	}

	public void writeMessagesFrom(String user) {
		Iterator<Message> i = messages.descendingIterator();
		while(i.hasNext()) {
			Message msg = i.next();
			if(user == msg.getSender()) {
				try {
					out.writeUTF(msg.getMessage());
				} catch (IOException e) {
					// TODO
				}
			}
		}
		if(usersWithMessages.contains(user)) {
			usersWithMessages.remove(user);
		}
	}

	public void endChat() {
		read.interrupt();
		write.interrupt();
		protocol.setState(ConnectionState.OPTIONS);
	}

	//---------------------------------------------------------------------------
	//----------------------Private Classes and stuff...-------------------------
	//---------------------------------------------------------------------------

	private enum ConnectionState {
		OPTIONS, CHATROOM, CHATTING, LOGOUT, EXIT, UNCHANGED
	}

	private class Protocol {
		private ConnectionState currentState;

		public Protocol() {
			currentState = ConnectionState.OPTIONS;
		}

		public void setState(ConnectionState state) {
			currentState = state;
		}

		public ConnectionState getState() {
			return currentState;
		}
	}

	private class ConnectionWriteThread extends Thread {
		private String receiver;

		public ConnectionWriteThread(String receiver) {
			this.receiver = receiver;
		}
		public void run() {
			while(!this.isInterrupted()) {
				String s = null;
				try {
					s = in.readUTF();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
		private String sender;

		public ConnectionReadThread(String sender) {
			this.sender = sender;
		}
		public void run() {
			while(!this.isInterrupted()) {
				if(usersWithMessages.contains(sender)) {
					writeMessagesFrom(sender);
				}
			}
		}
	}
}
