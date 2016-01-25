


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
	private ChatInputReader write;
	private ChatMessagePrinter read;

	public Connection(String clientName, Socket client, Synchronizer sync) {
		this.clientName = clientName;
		this.sync = sync;
		protocol = new Protocol();
		messages = new LinkedList<Message>();
		usersWithMessages = new HashSet<String>();
		setupStreams(client);
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
				//startChat(); TODO
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
		String write = "Chat room (1)\nLog out (2)\nExit(3)\n";
		out.writeUTF(write);
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

	private void startChat(String user) {
		read = new ChatMessagePrinter(user);
		read.run();
		write = new ChatInputReader(user);
		write.run();
		protocol.setState(ConnectionState.UNCHANGED);
	}

	private void writeMessagesFrom(String user) {
		Iterator<Message> i = messages.descendingIterator();
		while(i.hasNext()) {
			Message msg = i.next();
			if(user == msg.getSender()) {
				try {
					out.writeUTF(user+": "+msg.getMessage());
				} catch (IOException e) {
					// TODO
				}
			}
		}
		usersWithMessages.remove(user);
	}

	private void endChat() {
		read.interrupt();
		write.interrupt();
		protocol.setState(ConnectionState.OPTIONS);
	}
	
	private void setupStreams(Socket socket) {
		try {
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//---------------------------------------------------------------------------
	//----------------------Private Classes and stuff...-------------------------
	//---------------------------------------------------------------------------

	private class ChatInputReader extends Thread {
		private String receiver;

		public ChatInputReader(String receiver) {
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

	private class ChatMessagePrinter extends Thread {
		private String sender;

		public ChatMessagePrinter(String sender) {
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
