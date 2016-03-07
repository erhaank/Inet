package bean;

public class Order{
	private String name, type, uid;
	private double price;
	private int id, amount;

	public Order() {}

	public void setName(String name) {
		this.name = name;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	public String getUid() {
		return uid;
	}

	public double getPrice() {
		return price;
	}

	public int getAmount() {
		return amount;
	}
}
