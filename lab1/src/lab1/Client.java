//package lab1;

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
		// scanner.close();
	}

	public void startChat(Socket socket) {
		setupStreams(socket);
		logIn();

		new ClientReadThread().start();
		System.out.println("Only one started");
		new ClientWriteThread().start();
		System.out.println("Both started");
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
			System.out.println(response);
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
			System.out.println("it is running at least");
			while(true) {
					String input = scanner.next();
					try {
						System.out.println("Before write");
						out.writeUTF(input);
						System.out.println("After write");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						scanner.close();
						e.printStackTrace();
					}
			}
		}
	}

	private class ClientReadThread extends Thread {

		public void run() {
			while(true) {
				String output = null;
				try {
					// out.writeUTF(""); 	// Write an 'update' string, asking the server if any changes has been made
					Thread.sleep(100);
					output = in.readUTF(); // Should be "0" if no changes has been made
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					//TODO
				}
				if (output != null && !output.equals("0"))
					System.out.println(output);
			}
		}
	}
}
