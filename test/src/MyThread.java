import java.util.LinkedList;


public class MyThread extends Thread {
	
	private LinkedList<String> buffer;
	
	public MyThread() { 
		buffer = new LinkedList<String>();
	}
	
	public void addToBuffer(String s) {
		buffer.add(s);
	}
	
	public void run() {
		while(true) {
			String print = "";
			for (String s : buffer)
				print += s+", ";
			System.out.println(print);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
