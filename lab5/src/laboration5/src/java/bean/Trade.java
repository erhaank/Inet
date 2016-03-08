package bean;

import java.sql.Date;

public class Trade {
	private String name, seller, buyer;
	private double price;
	private int id, amount;
	private Date date;

	public Trade() {}

	public void setSecurity(String name) {
		this.name = name;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}

	public void setSeller(String seller) {
		this.seller = seller;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	public String getBuyer() {
		return buyer;
	}

	public String getSeller() {
		return seller;
	}

	public double getPrice() {
		return price;
	}

	public int getAmount() {
		return amount;
	}

	public Date getDate() {
		return date;
	}
}
