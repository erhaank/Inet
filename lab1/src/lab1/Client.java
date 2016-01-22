package lab1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {

	private static Socket socket;
	private DataInputStream in;
	private DataOutputStream out;

	private class ClientReadThread extends Thread {

		private Scanner scanner;
		private DataOutputStream out;

		public ClientReadThread(DataOutputStream out) {
			this.out = out;
		}
		public void run() {
			Scanner scanner = new Scanner(System.in);
			while(true) {
				String input = scanner.next();
				out.writeChars(input);
			}
	}

	private class ClientWriteThread extends Thread {

		private DataInputStream in;

		public ClientWriteThread(DataInputStream in) {
			this.in = in;
		}
		public void run() {
			while(true) {
				String output = in.readUTF();
				System.out.println(output);
			}
	}

	public Client(Socket socket) {
		this.socket = socket;
		try {
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream();
		} catch (IOException e) {
			//TODO
		}
	}

	public void getUsername() {
		System.out.println("Username:");
    	String username = scanner.next();
    	out.writeChars(username);
	}

	public void startChat() {
    	ClientWriteThread writeThread = new ClientWriteThread(in);
    	writeThread.run();

    	ClientReadThread readThread = new ClientReadThread(out);
    	readThread.run();
	}
}
