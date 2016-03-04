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

	public void addSecurity(String name) {
		try {
			statement  = connect.createStatement();
			statement.executeQuery("insert into trade.securities(name) values('"+name+"')");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Security> getSecurities() {
		ArrayList<Security> securities = new ArrayList<Security>();
		try {
			statement  = connect.createStatement();
			ResultSet set = statement.executeQuery("select * from trade.securities");
			while(set.next()) {
				Security s = new Security();
				s.setName(set.getString("name"));
				//s.setId(set.getInt("id")); Visst behöver vi inte id?
				securities.add(s);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return securities;
	}

	public String[] getOrdersNames() {
		int amount = orderAmount();
		String[] res = new String[amount];
		try {
			statement  = connect.createStatement();
			ResultSet set = statement.executeQuery("select * from trade.orders");
			for (int i = 0; i < amount; i++) {
				if (!set.next())
					break;
				res[i] = set.getString("name");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	public int orderAmount() {
		int amount = 0;
		try {
			statement = connect.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * from trade.orders");
			ResultSetMetaData rsmd = rs.getMetaData();

			amount = rsmd.getColumnCount();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return amount;
	}
}