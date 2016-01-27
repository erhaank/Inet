//package lab1;




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
 * 
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
		chatting = false;
		// System.out.println("Connection constructor");
	}

	public void run() {
		try {
			// System.out.println("connection run");
			go();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//TODO: Better name
	//TODO: fix all IOExceptions, where try/catch
	private void go() throws IOException { 
		currentState = ConnectionState.OPTIONS;
		// System.out.println(currentState);
		
		while(true) {
			switch(currentState) {
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
				System.out.println("default");
			}
		}
	}
	
	//Should not be called buffer maybe?
	public void addToBuffer(Message msg) {
		messages.push(msg);
		String sender = msg.getSender();
		usersWithMessages.add(sender);
		System.out.println(usersWithMessages.contains(sender) + "sender " + sender);
	}
	

	private void viewChatRoom() throws IOException {
		String[] users = sync.getUsers();
		StringBuilder sb = new StringBuilder();
		usersLogedIn = new HashSet<String>(); 
		for(String user : users) {
			if(user != this.clientName) {
				usersLogedIn.add(user);
				if(usersWithMessages.contains(user)) {
					sb.append(user + "*" + "\n");
				} else {
					sb.append(user + "\n");
				}
			}
		}
		out.writeUTF("Choose who you want to chat with. Type 0 to go back to Options");
		out.writeUTF(sb.toString());
		String s = in.readUTF();
		System.out.println("read user:" + s);
		chooseChat(s);
	}

	private void viewOptions() throws IOException {
		String write = "Chat room (1)\nLog out (2)\nExit(3)\n";
		// System.out.println(write);
		try {
		out.writeUTF(write);
	} catch (Exception e) {
		e.printStackTrace();
	} 
		// System.out.println("gjl");
		String s = in.readUTF();
		chooseOption(s);
	}
	
	private void chooseOption(String s) throws IOException {
		switch (s) {
		case "1":
			currentState = ConnectionState.CHATROOM;
			break;
		case "2":
			currentState = ConnectionState.LOGOUT;
			break;
		case "3":
			currentState = ConnectionState.EXIT;
			break;
		default:
			unvalidOption(s);
			break;
		}
	}
	
	private void chooseChat(String s) throws IOException {
		if(usersLogedIn.contains(s)) {
			System.out.println(s + "is in usersLogedIn");
			chattingWith = s;
			currentState = ConnectionState.CHATTING;
			System.out.println("State is Chatting");
		} else {
			if(s.equals("0")) {
				currentState = ConnectionState.OPTIONS;
			} else {
				unvalidOption(s);
			}
		}
	}
	
	private void unvalidOption(String s) throws IOException {
		out.writeUTF(s + " is not a valid option.");
		currentState = ConnectionState.UNCHANGED;
	}

	private void startChat(String user) {
		System.out.println("In startchat");
		chatting = true;
		write = new ChatInputReader(user);
		write.start();
		printMessagesFrom(user);
	}
	
	private void printMessagesFrom(String sender) {
		System.out.println("In printMessagesFrom with sender " + sender);
		System.out.println("what is chatting " + chatting);
		while(chatting) {
			if(usersWithMessages.contains(sender)) {
				System.out.println("Go to writeMessagesFrom");
				writeMessagesFrom(sender);
			}
		}
	}

	private void writeMessagesFrom(String user) {
		Iterator<Message> i = messages.descendingIterator();
		while(i.hasNext()) {
			Message msg = i.next();
			if(user.equals(msg.getSender())) {
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
		currentState = ConnectionState.OPTIONS;
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
				System.out.println("Is in run");
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
					System.out.println("Ready to distribute");
					sync.distribute(msg);
				}
			}
		}
	}
}
