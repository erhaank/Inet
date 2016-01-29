import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * A connection is connected to a specific Client.
 */
public class Connection extends Thread {
	private static final String CLEAR_CLIENT = "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n";

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
	}

	public void run() {
		currentState = ConnectionState.OPTIONS;

		while(running) {
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
				terminateSession(ConnectionState.EXIT);
				break;
			case LOGOUT:
				terminateSession(ConnectionState.LOGOUT);
				break;
			default:
				//TODO
			}
		}
	}
	//------------------------------------------------------------------
	//------------------------- Options --------------------------------
	//------------------------------------------------------------------
	private void viewOptions() {
		String write = "Chat room (CHATROOM)\nLog out (LOGOUT)\nExit(EXIT)\n";
		try {
			writeToClient(CLEAR_CLIENT);
			writeToClient(write);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		String s = readFromClient();
		chooseOption(s);
	}

	private void chooseOption(String s) {
		switch (s) {
		case "CHATROOM":
			currentState = ConnectionState.CHATROOM;
			break;
		case "LOGOUT":
			currentState = ConnectionState.LOGOUT;
			break;
		case "EXIT":
			currentState = ConnectionState.EXIT;
			break;
		default:
			unvalidOption(s);
			break;
		}
	}

	//------------------------------------------------------------------
	//------------------------- Chatroom --------------------------------
	//------------------------------------------------------------------
	private void viewChatRoom()  {
		StringBuilder sb = new StringBuilder();
		String online = getOnlineUsers();
		String chatRoomMenu = "Choose who you want to chat with. Type BACK to go back to Options\n";
		sb.append(CLEAR_CLIENT);
		sb.append(chatRoomMenu);
		sb.append(online);
		writeToClient(sb.toString());
		sb.setLength(sb.length()-online.length());
		try {
			while (in.available() == 0) { //?
				Thread.sleep(200);
				String on2 = getOnlineUsers();
				if (!online.equals(on2)) {
					online = on2;
					//writeToClient("** Updates has been made **"); // Detta?
					sb.append(online);
					writeToClient(sb.toString());
					sb.setLength(sb.length()-online.length()); // eller detta?
				}
			}
		} catch (IOException e) {
			System.out.println("Couldn't check if there was available input");
			e.printStackTrace();
		} catch (InterruptedException e) {
			System.out.println("Thread couldn't sleep");
			e.printStackTrace();
		}

		String s = readFromClient();
		chooseChat(s);
	}

	/**
	 * Returns a string of all of the online users
	 */
	private String getOnlineUsers() {
		String[] users = sync.getUsers();
		StringBuilder sb = new StringBuilder();
		usersLogedIn = new HashSet<String>(); 
		for(String user : users) {
			if(user != this.clientName) {
				usersLogedIn.add(user);
				String username = getUsername(user);
				if(usersWithMessages.contains(user)) {
					sb.append(username + "*" + "\n");
				} else {
					sb.append(username + "\n");
				}
			}
		}
		return sb.toString();
	}



	private void chooseChat(String s) {
		if (s.equals("BACK")) {
			currentState = ConnectionState.OPTIONS;
			return;
		}
		String username = null;
		for (String user : usersLogedIn) {
			if (user.contains(s)) {
				username = user;
				if (user.equals(s))
					break;
			}
		}
		if (username != null) {
			chattingWith = username;
			currentState = ConnectionState.CHATTING;
		} else unvalidOption(s);
	}
	
	//------------------------------------------------------------------
	//------------------------- Chatting -------------------------------
	//------------------------------------------------------------------
	private void startChat(String user) {
		chatting = true;
		write = new ChatInputReader(user);
		write.start();
		printMessagesFrom(user);
	}

	private void printMessagesFrom(String sender) {
		writeToClient(CLEAR_CLIENT); 
		writeToClient("Chatting with "+getUsername(sender)+". Write BACK to go back to the chatroom.\n");
		while(chatting)
			if(usersWithMessages.contains(sender))
				writeMessagesFrom(sender);
	}

	private void writeMessagesFrom(String user) {

		for (int i = 0; i < messages.size(); i++) {
			Message msg = messages.get(i);
			if(user.equals(msg.getSender())) {
				try {
					writeToClient(getUsername(user)+": "+msg.getMessage());
					out.flush();
					messages.remove(i);
					i--;
				} catch (IOException e) {
					System.out.println("FAULT TODO osv");
				}
			}
		}
		usersWithMessages.remove(user);
	}

	private void endChat() {
		chatting = false;
		currentState = ConnectionState.CHATROOM;
	}

	private void terminateSession(ConnectionState state) {
		if (state == ConnectionState.LOGOUT)
			writeToClient("//LOGOUT");
		else writeToClient("//EXIT");
		try {
			out.close();
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		running = false;
	}


	//------------------------------------------------------------------
	//------------------------- Other --------------------------------
	//------------------------------------------------------------------

	public void addToBuffer(Message msg) {
		messages.add(msg);
		String sender = msg.getSender();
		usersWithMessages.add(sender);
	}

	private void unvalidOption(String s) {
		writeToClient(s + " is not a valid option.");
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

	private void writeToClient(String s) {
		try {
			out.writeUTF(s);
			out.flush();
		} catch (IOException e) {
			System.out.println("Couldn't write to client!");
			e.printStackTrace();
		}
	}

	private String readFromClient() {
		String s = "";
		try {
			s = in.readUTF();
		} catch (IOException e) {
			System.out.println("Couldn't read from client!");
			e.printStackTrace();
			running = false;
		}
		return s;
	}

	private String getUsername(String user) {
		return user.substring(user.indexOf(":")+1);
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
				String s = readFromClient();
				if(s.equals("BACK")) {
					chatting = false;
					endChat();
				} else {
					Message msg = new Message(clientName, receiver);
					msg.setMessage(s);
					if (!sync.distribute(msg))
						writeToClient("***"+receiver+ " has logged out***");
				}
			}
		}
	}
}
