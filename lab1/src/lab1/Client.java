package lab1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {

	private static Socket socket;
	private Boolean running;

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

	public static void main(String[] args) {		
		try {
			socket = new Socket("localhost", 8888);
    	} catch (IOException e) {
    		//TODO maybe they should be in 2 try catch instead...
    	}
    	DataInputStream in = new DataInputStream(socket.getInputStream());
		DataOutputStream out = new DataOutputStream(socket.getOutputStream();

		System.out.println("Username:");
    	String username = scanner.next();
    	out.writeChars(username);

    	ClientWriteThread writeThread = new ClientWriteThread(in);
    	writeThread.run();

    	ClientReadThread readThread = new ClientReadThread(out);
    	readThread.run();
	}

}
