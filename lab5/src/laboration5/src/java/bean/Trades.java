package bean;

import java.util.ArrayList;

public class Trades {
	private ArrayList<Trade> list;

	public Trades() {
		list = new ArrayList<Trade>();
	}

	public ArrayList<Trade> getList() {
		return list;
	}

	public void addTrade(Trade t) {
		list.add(t);
	}
}