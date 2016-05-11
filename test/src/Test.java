
public class Test {
	private int i;
	public Test() {
		i = 0;
		MyThread t1 = new MyThread();
		t1.start();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		t1.addToBuffer("Heeej");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		t1.addToBuffer("Yolo");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		t1.addToBuffer("Hehehehe");
	}

	public static void main(String[] args) {
		new Test();
	}
	
	public int foo() {
		return i++;
	}
}

