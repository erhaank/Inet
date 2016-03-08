package bean;

public class Order{
	private String security, type, uid;
	private double price;
	private int amount;

	public Order() {}

	public void setSecurity(String security) {
		this.security = security;
	}

	public void setId(String id) {
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

	public String getSecurity() {
		return security;
	}

	public String getId() {
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

	public String getPriceString() {
		return "" + price; 
	}

	public String getAmountString() {
		return "" + amount;
	}

	public int getAmount() {
		return amount;
	}
}
