import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSetMetaData;
import java.sql.Date;
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
				s.setSecurity(set.getString("name"));
				s.setId(set.getInt("id")); // Behöver vi ens id?
				securities.add(s);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return securities;
	}

	public String addOrder(Order o) {
		String ret = "Add order was successful!";
		try {
			statement = connect.createStatement();
			statement.executeUpdate("insert into trade.orders(name, type, price, amount, uid) "
				+"values('"+o.getSecurity()+"','"+o.getType()+"', "+o.getPrice()+","+o.getAmount()+",'"+o.getUid()+"')");
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
				o.setSecurity(set.getString("name"));
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
			statement.executeUpdate("insert into trade.trades(name, price, amount, dt, buyer, seller) values('"+securityName+"',"+price+","+amount+",'"+dt.toString()+"','"+buyer+"','"+seller+"')");
		} catch (SQLException e) {
			e.printStackTrace();
			ret = "Add trade: Failed Emoji frown" + dt.toString();
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
				t.setSecurity(set.getString("name"));
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

	/**
	* Assumes that decrease <= order.amount
	* If decrease == order.amount, removes the order
	*/
	public String updateOrderAmount(Order order, int decrease) {
		String message = "";
		order.setAmount(order.getAmount()-decrease);
		if (order.getAmount() == 0) {
			removeOrder(order.getId());
			return message;
		}
		String query = "update trade.orders set amount = "+order.getAmount()+" where id = "+order.getId();
		try {
			statement = connect.createStatement();
			statement.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
			message = "fail";
		}
		return message;
	}

	public String tryTrade(String uid, String sellOrBuy) {
   		ArrayList<Order> orders = getOrders();
   		String message = "Attempted trade." + "<br>";
   		StringBuilder sb = new StringBuilder(message);
   		Order currentOrder = getCurrentOrder(uid, orders);
   		String wantedType = "S";
   		if(sellOrBuy.equals("S")) {
   			wantedType = "B";
   		}
   		for(Order o : orders) {
   			if(o.getSecurity().equals(currentOrder.getSecurity()) && o.getType().equals(wantedType)
   				&& o.getPrice() == currentOrder.getPrice()) {
   				if(o.getAmount() >= currentOrder.getAmount()) {
   					removeOrder(currentOrder.getId());
   					sb.append(updateOrderAmount(o, currentOrder.getAmount()));
   					sb.append(makeTrade(currentOrder, o, sellOrBuy, currentOrder.getAmount()));
   					break;
   				} else {
   					removeOrder(o.getId());
   					sb.append(updateOrderAmount(currentOrder, o.getAmount()));
   					sb.append(makeTrade(currentOrder, o, sellOrBuy, o.getAmount()));
   				}
   			}
   		}
   		return sb.toString();
   	}

   	public String makeTrade(Order currentOrder, Order previousOrder, String sellOrBuy, int tradeAmount) {
   		Order buyer = null;
   		Order seller = null;
   		if(sellOrBuy.equals("B")) {
   			buyer = currentOrder;
   			seller = previousOrder;
   		}
   		if(sellOrBuy.equals("S")) {
   			buyer = previousOrder;
   			seller = currentOrder;
   		}
   		addTrade(buyer.getSecurity(), buyer.getUid(), seller.getUid(), buyer.getPrice(),
   			tradeAmount);
		String message = getTradeMessage(tradeAmount, buyer.getSecurity(), buyer.getUid(),
   			seller.getUid());
   		return message;
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