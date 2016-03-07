import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSetMetaData;
import java.util.Date;
import java.util.ArrayList;
import bean.*;

public class DatabaseAccessor {
	private Connection connect = null;
	private Statement statement = null;

	public DatabaseAccessor() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager
	          .getConnection("jdbc:mysql://localhost/trade?"
	              + "user=default_user&password=password");
     	} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String addSecurity(String name) {
		String ret = "Add security: Success!";
		try {
			statement = connect.createStatement();
			statement.executeUpdate("insert into trade.securities(name) values('"+name+"')");
		} catch (SQLException e) {
			e.printStackTrace();
			ret = "Add security: Fail :(";
		}
		return ret;
	}

	public ArrayList<Security> getSecurities() {
		ArrayList<Security> securities = new ArrayList<Security>();
		try {
			statement  = connect.createStatement();
			ResultSet set = statement.executeQuery("select * from trade.securities");
			while(set.next()) {
				Security s = new Security();
				s.setName(set.getString("name"));
				s.setId(set.getInt("id")); // Behöver vi ens id?
				securities.add(s);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return securities;
	}

	public String addOrder(String securityName, String type, double price, int amount, String uid) {
		String ret = "Add order was successful!";
		try {
			statement = connect.createStatement();
			statement.executeUpdate("insert into trade.orders(name, type, price, amount, uid) "
				+"values('"+securityName+"','"+type+"', "+price+","+amount+",'"+uid+"')");
		} catch (SQLException e) {
			e.printStackTrace();
			ret = "Add order: Failed :(";
		}
		return ret;
	}

	public ArrayList<Order> getOrders() {
		ArrayList<Order> orders = new ArrayList<Order>();
		try {
			statement  = connect.createStatement();
			ResultSet set = statement.executeQuery("select * from trade.orders");
			while(set.next()) {
				Order o = new Order();
				o.setName(set.getString("name"));
				o.setId(set.getInt("id")); // Behöver vi ens id?
				o.setType(set.getString("type"));
				o.setUid(set.getString("uid"));
				o.setPrice(set.getDouble("price"));
				o.setAmount(set.getInt("amount"));
				orders.add(o);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return orders;
	}

	public String addTrade(String securityName, String buyer, String seller, double price, int amount) {
		String ret = "Add trade was successful!";
		Date dt = new Date(System.currentTimeMillis());
		try {
			statement = connect.createStatement();
			statement.executeUpdate("insert into trade.trades(name, price, amount, dt, buyer, seller) "
				+"values('"+securityName+"',"+price+","+amount+","+dt.toString()+",'"+buyer+"','"+seller+"')");
		} catch (SQLException e) {
			e.printStackTrace();
			ret = "Add trade: Failed :(";
		}
		return ret;
	}

	public ArrayList<Trade> getTrades() {
		ArrayList<Trade> trades = new ArrayList<Trade>();
		try {
			statement  = connect.createStatement();
			ResultSet set = statement.executeQuery("select * from trade.trades");
			while(set.next()) {
				Trade t = new Trade();
				t.setName(set.getString("name"));
				t.setId(set.getInt("id")); // Behöver vi ens id?
				t.setBuyer(set.getString("buyer"));
				t.setSeller(set.getString("seller"));
				t.setPrice(set.getDouble("price"));
				t.setAmount(set.getInt("amount"));
				t.setDate(set.getDate("dt"));
				trades.add(t);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return trades;
	}
}