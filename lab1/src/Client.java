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
	private volatile boolean running;

	public Client() {
		getUsername();
	}

	private void getUsername() {
		System.out.println("Username:");
		Scanner scanner = new Scanner(System.in);
		username = scanner.next();
		running = true;
	}

	public void startChat(Socket socket) {
		setupStreams(socket);
		logIn();

		ClientReadThread read = new ClientReadThread();
		read.start();
		ClientWriteThread write = new ClientWriteThread();
		write.start();
		try {
			read.join();
			write.interrupt();
		} catch (InterruptedException e) {
			System.out.println("Read won't work");
			e.printStackTrace();
		}
		
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
			terminate("Username already taken, or contains ':'. Exiting chat");
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
			while(running) {
					String input = scanner.nextLine();
					if(input.equals("LOGOUT")) {
						running = false;
					}
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

	private class ClientReadThread extends Thread {

		public void run() {
			while(running) {
				
				String output = null;
				try {
					Thread.sleep(100); //?
					output = in.readUTF();
				} catch (InterruptedException e) {
					System.out.println("Thread couldn't sleep");
					e.printStackTrace();
				} catch (IOException e) {
					System.out.println("Couln't read input from server");
					e.printStackTrace();
				}
				
				if (output.equals("//EXIT")) {
					running = false;
					terminate("Logging out! See you later ;)");
				} else if (output.equals("//LOGOUT")) //So this is not needed anymore
					running = false;
				else if (output != null)
					System.out.println(output);
			}
		}
	}
}
