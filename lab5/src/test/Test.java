package bean;

public class Test {
	private String name;
	private boolean newCon;

	public Test() {
		newCon = true;
		name = "Martin";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean getNewCon() {
		return newCon;
	}

	public void setNewCon(boolean con) {
		newCon = con;
	}
}