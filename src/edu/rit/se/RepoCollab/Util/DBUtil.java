package edu.rit.se.RepoCollab.Util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class DBUtil {

	private String user;
	private String password;
	private String url;
	
	private enum SQLTypes {
		STRING,
		INTEGER
	}
	
	public DBUtil() throws Exception{
		Properties props = new Properties(System.getProperties());
		props.load(new FileInputStream("system.properties"));
		
		this.user = props.getProperty("db.user");
		this.password = props.getProperty("db.password");
		this.url = props.getProperty("db.url");
	}
	
	private Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, user, password);
	}

	public boolean executeInsertSQL(String preparedStatement, Object[] values) throws SQLException {
		Connection conn = getConnection();
		PreparedStatement ps = null;
		ps = conn.prepareStatement(preparedStatement);
		//set the appropriate parameters using the apppropriate type; current types supported ints and varchars
		//Use class names to determine type
		for(int i = 0; i < values.length; i++){
			SQLTypes type = SQLTypes.valueOf(values[i].getClass().getName().toUpperCase());

			switch(type){
				case STRING: ps.setString(i, values[i].toString()); break;
				case INTEGER: ps.setInt(i, (Integer)values[i]); break;
			}
		}
		
		boolean result = ps.execute();
		ps.close();
		conn.close();
		return result;
	}
	
}
