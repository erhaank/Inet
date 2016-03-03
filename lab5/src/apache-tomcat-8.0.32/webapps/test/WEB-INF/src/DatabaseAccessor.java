package com.quote.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSetMetaData;
import java.util.Date;

public class DatabaseAccessor {
	private Connection connect = null;

	public DatabaseAccessor() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager
	          .getConnection("jdbc:mysql://localhost/quote?"
	              + "user=default_user&password=password");
     	} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String[] getQuotes() {
		String[] res = {};
		try {
			Statement st  = connect.createStatement();
			ResultSet set = st.executeQuery("select * from quote.quotes");
			res = (String[]) set.getArray("quote").getArray();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	public int quoteAmount() {
		int amount = 0;
		try {
			Statement st = connect.createStatement();
			ResultSet rs = st.executeQuery("SELECT * from quote.quotes");
			ResultSetMetaData rsmd = rs.getMetaData();

			amount = rsmd.getColumnCount();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return amount;
	}
}