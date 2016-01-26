package lab1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {

	private String username;
	private Socket socket;
	private DataInputStream in;
	private DataOutputStream out;

	public Client() {
		getUsername();
	}

	private void getUsername() {
		System.out.println("Username:");
		Scanner scanner = new Scanner(System.in);
		username = scanner.next();
		scanner.close();
	}

	public void startChat(Socket socket) {
		setupStreams(socket);
		logIn();

		new ClientReadThread().start();
		new ClientWriteThread().start();
	}

	private void setupStreams(Socket socket) {
		this.socket = socket;
		try {
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			//TODO
		}
	}

	private void logIn() {
		//First call should be to write the username to the Server
		int response = 0;
		try {
			out.writeUTF(username);
			response = in.readInt();
		} catch (IOException e) {
			//TODO
		}
		// Username taken. This can be done a lot cleaner, but whatever
		if (response == 0) {
			terminate("Username already taken, try another. Exiting chat");
		}
	}

	private void terminate(String message) {
		System.out.println(message);
		try {
			socket.close();
		} catch (IOException e) {}
		System.exit(0);
	}

	//---------------------------------------------------------------------------
	//----------------------Private Classes and stuff...-------------------------
	//---------------------------------------------------------------------------

	private class ClientWriteThread extends Thread {

		public void run() {
			Scanner scanner = new Scanner(System.in);
			while(true) {
				if (scanner.hasNext()) {
					String input = scanner.nextLine();
					try {
						out.writeUTF(input);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						scanner.close();
						e.printStackTrace();
					}
				}
			}
		}
	}

	private class ClientReadThread extends Thread {

		public void run() {
			while(true) {
				String output = null;
				try {
					out.writeUTF(""); 	// Write an 'update' string, asking the server if any changes has been made
					Thread.sleep(100);
					output = in.readUTF(); // Should be "0" if no changes has been made
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					//TODO
				}
				if (!output.equals("0"))
					System.out.println(output);
			}
		}
	}
}
