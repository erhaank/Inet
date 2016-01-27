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
	private final String CLEAR_CLIENT = "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n";

	private volatile boolean running;
	private String clientName;
	private Synchronizer sync;
	private DataOutputStream out;
	private DataInputStream in;
	private ConnectionState currentState;
	private LinkedList<Message> messages;
	private Set<String> usersWithMessages;
	private Set<String> usersLogedIn;
	private ChatInputReader write;
	private volatile boolean chatting;
	private String chattingWith;

	public Connection(String clientName, Socket client, Synchronizer sync) {
		this.clientName = clientName;
		this.sync = sync;
		messages = new LinkedList<Message>();
		usersWithMessages = new HashSet<String>();
		usersLogedIn = new HashSet<String>();
		setupStreams(client);
		chatting = false;
		running = true;
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

		while(running) {
			System.out.println(clientName+ ": "+currentState);
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
				logout();
				break;
			case LOGOUT:
				logout();
				break;
			default:
				System.out.println("default");
			}
		}
	}

	//Should not be called buffer maybe?
	public void addToBuffer(Message msg) {
		System.out.println(clientName+": Adding msg from "+msg.getSender()+" to buffer.");
		messages.push(msg);
		String sender = msg.getSender();
		usersWithMessages.add(sender);
		// System.out.println(usersWithMessages.contains(sender) + "sender " + sender);
	}


	private void viewChatRoom() throws IOException {
		String online = getOnlineUsers();
		out.writeUTF("Choose who you want to chat with. Type 0 to go back to Options");
		out.writeUTF(online);
		out.flush();
		while (in.available() == 0) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String on2 = getOnlineUsers();
			if (!online.equals(on2)) {
				out.writeUTF("Updates has been made..."); // Hur  får man det här att funka på ett schysst sätt?
				online = on2;
			}
		}

		String s = in.readUTF();
		// System.out.println("read user:" + s);
		chooseChat(s);
	}

	private String getOnlineUsers() {
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
		return sb.toString();
	}

	private void viewOptions() throws IOException {
		String write = "Chat room (1)\nLog out (2)\nExit(3)\n";
		// System.out.println(write);
		try {
			out.writeUTF(write);
			out.flush();
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
			// System.out.println(s + "is in usersLogedIn");
			chattingWith = s;
			currentState = ConnectionState.CHATTING;
			// System.out.println("State is Chatting");
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
		out.flush();
		//currentState = ConnectionState.UNCHANGED;
	}

	private void startChat(String user) {
		// System.out.println("In startchat");
		chatting = true;
		write = new ChatInputReader(user);
		write.start();
		printMessagesFrom(user);
	}

	private void printMessagesFrom(String sender) {
		// System.out.println("In printMessagesFrom with sender " + sender);
		// System.out.println("what is chatting " + chatting);
		try {
			out.writeUTF("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
					"Chatting with "+sender+". Write EXIT to exit the chat.\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(chatting) {
			if(usersWithMessages.contains(sender)) {
				// System.out.println("Go to writeMessagesFrom");
				writeMessagesFrom(sender);
			}
		}
	}

	private void writeMessagesFrom(String user) {

		for (int i = 0; i < messages.size(); i++) {
			Message msg = messages.get(i);
			if(user.equals(msg.getSender())) {
				try {
					out.writeUTF(user+": "+msg.getMessage());
					out.flush();
					messages.remove(i);
					i--;
				} catch (IOException e) {
					// TODO
				}
			}
		}
		usersWithMessages.remove(user);
	}

	private void endChat() {
		//write.interrupt();
		chatting = false;
		currentState = ConnectionState.CHATROOM;
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

	private void logout() {
		try {
			out.writeUTF("//LOGOUT");
			out.close();
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		running = false;
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
			while(chatting) {
				//System.out.println("Is in run");
				String s = null;
				try {
					s = in.readUTF();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(s.equals("EXIT")) {
					System.out.println("Trying to exit chat...");
					chatting = false;
					endChat();
				} else {
					Message msg = new Message(clientName, receiver);
					msg.setMessage(s);
					// System.out.println("Ready to distribute");
					sync.distribute(msg);
				}
			}
		}
	}
}
