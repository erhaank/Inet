package lab1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {

	private DataInputStream in;
	private DataOutputStream out;

	public Client(Socket socket) {
		try {
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			//TODO
		}
	}

	public void getUsername() {
		/*System.out.println("Username:");
    	String username = scanner.next();
    	out.writeChars(username);*/
	}

	public void startChat() {

    	ClientReadThread readThread = new ClientReadThread(in);
    	readThread.run();

    	ClientWriteThread writeThread = new ClientWriteThread(out);
    	writeThread.run();
	}
	//---------------------------------------------------------------------------
	//----------------------Private Classes and stuff...-------------------------
	//---------------------------------------------------------------------------
	
	private class ClientWriteThread extends Thread {

		private DataOutputStream out;

		public ClientWriteThread(DataOutputStream out) {
			this.out = out;
		}
		public void run() {
			Scanner scanner = new Scanner(System.in);
			while(true) {
				String input = scanner.next();
				try {
					out.writeChars(input);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					scanner.close();
					e.printStackTrace();
				}
			}
		}
	}

	private class ClientReadThread extends Thread {

		private DataInputStream in;

		public ClientReadThread(DataInputStream in) {
			this.in = in;
		}
		public void run() {
			while(true) {
				String output = null;
				try {
					output = in.readUTF();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println(output);
			}
		}
	}
}
