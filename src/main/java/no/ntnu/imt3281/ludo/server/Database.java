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
				stmt.execute("CREATE TABLE IF NOT EXISTS players ("
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
	 * Creates Chat Table in database, has username as primary key
	 */
	public void CreateChatTable(){
		try{
			Connection conn = DriverManager.getConnection(dbURL);
			//create a statement
			Statement stmt = conn.createStatement();
			stmt.execute("CREATE TABLE IF NOT EXISTS chat ("
					+ "id INT NOT NULL AUTO_INCREMENT,"
					+ "username varchar(128) NOT NULL, "
		            + "message TEXT NOT NULL, "
		            + "PRIMARY KEY  (id))");
			conn.close();
			System.out.println("Table 'chat' created");
		} catch (SQLException sqle) {
			sqle.printStackTrace();
	}
}
	/**
	 * Adding a username and chatmessage to chat table 
	 * @param username
	 */
	public void addUserChatTable(String username, String message){

					String query = "INSERT INTO chat "
							       +"(username, message)"
							       +"VALUE(?,?)";
				
				try{
					Connection conn = DriverManager.getConnection(dbURL);
					PreparedStatement stmt = conn.prepareStatement(query);
					stmt.setString(1, username);
					stmt.setString(2, message);
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
	 * Adds a new user to the database
	 * @param username
	 * @param password
	 */
	public void addUser(String username, String password){
			
				String query = "INSERT INTO players "
						       +"(username, hashpassword)"
						       +"VALUES(?, ?)";
			
			try{
				Connection conn = DriverManager.getConnection(dbURL);
				PreparedStatement stmt = conn.prepareStatement(query);
				stmt.setString(1, username);
				char[] charPas = password.toCharArray();
				String hashPas = Password.hash(charPas).toString();
				stmt.setString(2, hashPas);
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
	public boolean checkUsername(String uname){
		try{
			Connection conn = DriverManager.getConnection(dbURL);
			Statement stmt = conn.createStatement();
			String query = "SELECT * FROM players WHERE username="+
			"\""+uname+"\""+";";
			ResultSet rs = stmt.executeQuery(query);
			String checkUser = rs.getString(1);
			if(checkUser.equals(uname)){
					return false;
				}
			else
				return true;
		}catch(SQLException e){
			e.printStackTrace();
			return false;
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
			char[] charPas = pword.toCharArray();
			String hashPas = Password.hash(charPas).toString();
			query = "SELECT * FROM players WHERE hashpassword="+
			"\""+hashPas+"\""+";";
			ResultSet rsp = stmt.executeQuery(query);
			String hashDBpwd = rsp.getString(2);
			byte[] hashDBpwdByte = hashDBpwd.getBytes();
			charPas = hashPas.toCharArray();
			if(Password.isExpectedPassword(charPas, hashDBpwdByte) == true)
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



