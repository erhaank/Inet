package lab1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

/**
 * A connection is connected to a specific Client.
 */
public class Connection extends Thread {
	
	private Synchronizer sync;
	private Socket client;
	private DataOutputStream out;
	private DataInputStream in;
	//TODO: Protocol, buffer and stuff

	public Connection(Socket client, DataOutputStream out, DataInputStream in, Synchronizer sync) {
		this.client = client;
		this.out = out;
		this.in = in;
		this.sync = sync;
	}
	
	//TODO: public void addToBuffer...

}
