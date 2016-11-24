package no.ntnu.imt3281.ludo.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
	private final static String dbURL = "jdbc:derby:prosjektDB;create=true";
	/**
	 * Connects to the created derby database
	 * @return 
	 */
	public void DatabaseConnect(){
			try {
			
				//connect to embedded driver in memory
				Connection conn = DriverManager.getConnection(dbURL);
				if (conn != null) {
					System.out.println("Connected to database");
				}
			} catch (SQLException ex) {
		        ex.printStackTrace();
		}
	}
	/**
	 * Creates the player table
	 * has username as primary key
	 */
	public void CreatePlayerTable(){
			try{
				Connection conn = DriverManager.getConnection(dbURL);
				//create a statement
				Statement stmt = conn.createStatement();
				stmt.execute("CREATE TABLE players ("
						+ "username varchar(128) NOT NULL, "
			            + "hashpassword varchar(128) NOT NULL, "
			            + "PRIMARY KEY  (username))");
				conn.close();
				System.out.println("Table 'players' created");
			} catch (SQLException sqle) {
				sqle.printStackTrace();
		}
	}
	/** 
	 * Adds a new user to the database
	 * @param username
	 * @param password
	 */
	public void addUser(String username, String password)
			throws SQLException
			{
				String query = "INSERT INTO players "
						       +"(username, hashpassword)"
						       +"VALUES(?, ?)";
			
			try{
				Connection conn = DriverManager.getConnection(dbURL);
				PreparedStatement stmt = conn.prepareStatement(query);
				stmt.setString(1, username);
				password = hash(password, getNextSalt());
				stmt.setString(2, password);
				
				stmt.executeUpdate();
				stmt.close();
				conn.close();
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		}
	/**
	 * Checks if username exists in database
	 * @param username
	 */
	public void checkUsername(String uname){
	try{
		Connection conn = DriverManager.getConnection(dbURL);
		Statement stmt = conn.createStatement();
		String query = "SELECT * FROM players WHERE username="+
		"\""+uname+"\""+";";
		ResultSet rs = stmt.executeQuery(query);
		String checkUser = rs.getString(1);
		if(checkUser.equals(uname)){
			System.out.println("User already exists");
			}
		else
			System.out.println("Username is free");
	}catch(SQLException e){
		e.printStackTrace();
	}
	}
	/**
	 * Checks if username and password exists in database
	 * @param uname
	 * @param pword
	 */
	public boolean checkLogin(String uname, String pword){
	try{
		Connection conn = DriverManager.getConnection(dbURL);
		Statement stmt = conn.createStatement();
		String query = "SELECT * FROM players WHERE username="+
		"\""+uname+"\""+";";
		ResultSet rsu = stmt.executeQuery(query);
		String checkUser = rsu.getString(1);
		
		if (checkUser.equals(uname)){
			query = "SELECT * FROM players WHERE hashpassword="+
			"\""+pword+"\""+";";
			ResultSet rsp = stmt.executeQuery(query);
			String checkPassword = rsp.getString(2);
			if(checkPassword.equals(pword))
				return true;
			else
				return false;
		}
		else 
			return false;
		
	} catch(SQLException e){
			e.printStackTrace();
			}
	return false;
	}
}



