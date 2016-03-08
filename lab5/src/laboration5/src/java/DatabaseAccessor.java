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
				o.setId(set.getString("id")); // Behöver vi ens id?
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

	public String removeOrder(String id) {
		String ret = "Order removed.";
		try {
			statement = connect.createStatement();
			statement.executeUpdate("delete from trade.orders where id = '" + id + "'");
		} catch (SQLException e) {
			e.printStackTrace();
			ret = "Remove order: Failed :(";
		}
		return ret;
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

	public String tryBuy(String uid) {
   		ArrayList<Order> orders = getOrders();
   		String message;
   		StringBuilder sb = new StringBuilder();
   		Order buyer = getCurrentOrder(uid, orders);
   		int initialAmount = buyer.getAmount();
   		Order seller = null;
   		for(Order o : orders) {
   			if(o.getName().equals(buyer.getName()) && o.getType().equals("S")
   				&& o.getPrice() == buyer.getPrice()) {
   				seller = o;
   				if(seller.getAmount() >= buyer.getAmount()) {
   					int amountLeft = seller.getAmount() - buyer.getAmount();
   					removeOrder(seller.getId());
   					if(amountLeft > 0) {
   						//Add the leftover of the sell order
   						addOrder(seller.getName(), seller.getType(), seller.getPrice(), amountLeft, 
   							seller.getUid());

   					}
   					removeOrder(buyer.getId());
   					//Buyers order was completely bought
   					addTrade(buyer.getName(), buyer.getUid(), seller.getUid(), 
   						buyer.getPrice(), buyer.getAmount());
   					message = getTradeMessage(buyer.getAmount(), buyer.getName(), buyer.getUid(),
   						seller.getUid());
   					return message;
   				} else {
   					//seller.getAmount < buyer.getAmount()
   					int amountLeft = buyer.getAmount() - seller.getAmount();
   					removeOrder(seller.getId());
   					buyer.setAmount(amountLeft);
   					//Sellers order was completely sold
   					addTrade(buyer.getName(), buyer.getUid(), seller.getUid(), 
   						buyer.getPrice(), seller.getAmount());
   					sb.append(getTradeMessage(seller.getAmount(), buyer.getName(), buyer.getUid(),
   						seller.getUid()));
   					sb.append("<br>");
   				}
   			}
   		}
   		if(initialAmount != buyer.getAmount()) {
	   		removeOrder(buyer.getId());
	   		//Add the leftover of the buy order
	   		addOrder(buyer.getName(), buyer.getType(), buyer.getPrice(), 
	   			buyer.getAmount(), buyer.getUid());
	   		return sb.toString();
   		}
   		return "";
   	}

	public String trySell(String uid) {
   		ArrayList<Order> orders = getOrders();
   		if(orders.isEmpty()) {
   			return "empty";
   		}
   		String message;
   		StringBuilder sb = new StringBuilder();
   		Order seller = getCurrentOrder(uid, orders);
   		int initialAmount = seller.getAmount();
   		Order buyer = null;
   		for(Order o : orders) {
   			if(o.getName().equals(seller.getName()) && o.getType().equals("B")
   				&& o.getPrice() == seller.getPrice()) {
   				buyer = o;
   				if(buyer.getAmount() >= seller.getAmount()) {
   					int amountLeft = buyer.getAmount() - seller.getAmount();
   					removeOrder(buyer.getId());
   					if(amountLeft > 0) {
   						//Add the leftover of the sell order
   						addOrder(buyer.getName(), buyer.getType(), buyer.getPrice(), amountLeft, 
   							buyer.getUid());

   					}
   					removeOrder(seller.getId());
   					//Sellers order was completely sold
   					message = addTrade(seller.getName(), buyer.getUid(), seller.getUid(), 
   						seller.getPrice(), seller.getAmount()) + 
   						getTradeMessage(seller.getAmount(), seller.getName(), buyer.getUid(),
   						seller.getUid());
   					return message;
   				} else {
   					//buyer.getAmount < seller.getAmount()
   					int amountLeft = seller.getAmount() - buyer.getAmount();
   					removeOrder(buyer.getId());
   					seller.setAmount(amountLeft);
   					//Buyers order was completely bought
   					addTrade(seller.getName(), buyer.getUid(), seller.getUid(), 
   						seller.getPrice(), buyer.getAmount());
   					sb.append(getTradeMessage(buyer.getAmount(), seller.getName(), buyer.getUid(),
   						seller.getUid()));
   					sb.append("<br>");
   				}
   			}
   		}
   		if(initialAmount != buyer.getAmount()) {
	   		removeOrder(buyer.getId());
	   		//Add the leftover of the buy order
	   		addOrder(buyer.getName(), buyer.getType(), buyer.getPrice(), 
	   			buyer.getAmount(), buyer.getUid());
	   		return sb.toString();
	   	}
	   	return "";
   	}

   	public String getTradeMessage(int amount, String security, String buyer, String seller) {
   		return "<br>A trade of " + amount + " " + security
   			+ " stocks from " + buyer + " to " + seller +" was made.<br>";
   	}

   	public Order getCurrentOrder(String uid, ArrayList<Order> orders) {
   		for(Order o : orders) {
   			if(o.getUid().equals(uid))
   				return o;
   		}
   		return null;
   	}
}