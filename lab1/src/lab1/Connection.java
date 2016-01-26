package lab1;




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
	private ConnectionState currentState;
	private LinkedList<Message> messages;
	private Set<String> usersWithMessages;
	private Set<String> usersLogedIn;
	private ChatInputReader write;
	private boolean chatting;
	private String chattingWith;

	public Connection(String clientName, Socket client, Synchronizer sync) {
		this.clientName = clientName;
		this.sync = sync;
		messages = new LinkedList<Message>();
		usersWithMessages = new HashSet<String>();
		usersLogedIn = new HashSet<String>();
		setupStreams(client);
		chatting = true;
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
		currentState = ConnectionState.OPTIONS;
		
		while(true) {
			switch(getCurrentState()) {
			case OPTIONS:
				viewOptions();
				break;
			case CHATROOM:
				viewChatRoom();
				break;
			case CHATTING:
				startChat(chattingWith);
				break;
			case EXIT:
				//TODO
				break;
			default:
				//TODO
			}
		}
	}
	
	//Should not be called buffer maybe?
	public void addToBuffer(Message msg) {
		messages.push(msg);
		String sender = msg.getSender();
		usersWithMessages.add(sender);
	}
	
	public void setState(ConnectionState state) {
		currentState = state;
	}

	public ConnectionState getCurrentState() {
		return currentState;
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
		String s = in.readUTF();
		chooseChat(s);
	}

	private void viewOptions() throws IOException {
		String write = "Chat room (1)\nLog out (2)\nExit(3)\n";
		out.writeUTF(write);
		String s = in.readUTF();
		chooseOption(s);
	}
	
	private void chooseOption(String s) throws IOException {
		switch (s) {
		case "1":
			setState(ConnectionState.CHATROOM);
			break;
		case "2":
			setState(ConnectionState.LOGOUT);
			break;
		case "3":
			setState(ConnectionState.EXIT);
			break;
		default:
			unvalidOption(s);
			break;
		}
	}
	
	private void chooseChat(String s) throws IOException {
		if(usersLogedIn.contains(s)) {
			chattingWith = s;
			currentState = ConnectionState.CHATTING;
		} else {
			unvalidOption(s);
		}
	}
	
	private void unvalidOption(String s) throws IOException {
		out.writeUTF(s + " is not a valid option.");
		setState(ConnectionState.UNCHANGED);
	}

	private void startChat(String user) {
		write = new ChatInputReader(user);
		write.run();
		printMessagesFrom(user);
	}
	
	private void printMessagesFrom(String sender) {
		while(chatting) {
			if(usersWithMessages.contains(sender)) {
				writeMessagesFrom(sender);
			}
		}
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
		write.interrupt();
		chatting = false;
		setState(ConnectionState.OPTIONS);
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
}
