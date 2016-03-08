package bean;

import java.util.ArrayList;

public class Securities {
	private ArrayList<Security> list;

	public Securities() {
		list = new ArrayList<Security>();
	}

	public ArrayList<Security> getList() {
		return list;
	}

	public void addSecurity(Security s) {
		list.add(s);
	}
}