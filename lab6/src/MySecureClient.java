

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.net.ssl.SSLSocket;

public class MySecureClient {

	private SSLSocket socket;
	private DataInputStream in;
	private DataOutputStream out;
	
	public MySecureClient() {
		MySecureSocketFactory sf = new MySecureSocketFactory();
		try {
			socket = sf.setupConnection("localhost", 8888);
			setupStreams();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		runClient();
	}
	
	private void setupStreams() throws IOException {
		in = new DataInputStream(socket.getInputStream());
		out = new DataOutputStream(socket.getOutputStream());
	}
	
	private void runClient() {
		while (true) {
			try {
				System.out.println(in.readUTF());
				Thread.sleep(500);
				out.writeUTF("Client: Polo");
			} catch (Exception e) {
				System.out.println("Closing down.");
				break;
			}
		}
	}
	
	public static void main(String[] args) {
		new MySecureClient();
	}

}
