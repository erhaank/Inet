package lab1;

public class Client {

	private Socket socket;
	private Boolean running;

	private ClientReadThread extends Thread {

		private Scanner scanner;
		private DataOutputStream out;

		public ClientThread(DataOutputStream out) {
			this.out = out;
		}
		public void run() {
			Scanner scanner = new Scanner(System.in);
			while(true) {
				String input = scanner.next();
				out.writeChars(input);
			}
	}

	private ClientWriteThread extends Thread {

		private DataOutputStream in;

		public ClientThread(DataOutputStream in) {
			this.in = in;
		}
		public void run() {
			while(true) {
				String output = in.readChars();
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
