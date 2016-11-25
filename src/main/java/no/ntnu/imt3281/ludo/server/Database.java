package no.ntnu.imt3281.ludo.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import no.ntnu.imt3281.ludo.client.Globals;

/**
 * Database holding a connection to the server, inserting and testing user information
 * @author Lasse Sviland
 *
 */
public class Database {
    private static Logger logger = Logger.getLogger(Globals.LOG_NAME);
	private static final String DBURL = "jdbc:derby:prosjektDB;create=true";
	private static Database instance = new Database();
	private Connection connection;
	/**
	 * Constructor making a connection to he derby database
	 */
	private Database(){
		try {
			this.connection = DriverManager.getConnection(DBURL);
		} catch (SQLException e) {
			logger.throwing(this.getClass().getName(), "Database", e);
		}
		if (connection != null)
			logger.info("Connected to database");
		createPlayerTable();
		createChatTable();
	}
	
	/**
	 * Returning the database instance
	 * @return
	 */
	public static Database getInstance() {
		return instance;
	}
	
	/**
	 * Creates the player table has username as primary key
	 */
	public void createPlayerTable(){
		try (Statement stmt = connection.createStatement()) {
			stmt.execute("CREATE TABLE player ("
				+ "username varchar(128) NOT NULL, "
	            + "hashpassword varchar(128) NOT NULL, "
	            + "PRIMARY KEY  (username))");
		} catch (SQLException e) {
			logger.throwing(this.getClass().getName(), "createPlayerTable", e);
		}
	}
	
	/**
	 * Creates Chat Table in database, has id as primary key
	 */
	public void createChatTable(){
		try (Statement stmt = connection.createStatement()) {
			stmt.execute("CREATE TABLE chat ("
					+ "id INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
					+ "username varchar(128) NOT NULL, "
		            + "message TEXT NOT NULL, "
		            + "PRIMARY KEY (id))");
		} catch (SQLException e) {logger.throwing(this.getClass().getName(), "createChatTable", e);}
		
	}
	
	/**
	 * Execute a query with up to 2 arguments
	 * @param sql the sql query
	 * @param arg1 the first argument
	 * @param arg2 the second argument or null
	 * @return the result set or null
	 */
	private ResultSet excecute(String sql, String arg1, String arg2) {
		ResultSet result = null;
		try (PreparedStatement stmt = connection.prepareStatement(sql)) { 
			stmt.setString(1, arg1);
			if (arg2 != null)
				stmt.setString(2, arg2);
			if(stmt.execute())
				result = stmt.getResultSet();
			
		} catch(SQLException e) {logger.throwing(this.getClass().getName(), "excecute", e);}
		return result;
	}
	
	/**
	 * Adding a username and chat message to chat table 
	 * @param username the username of the player sending the chat message
	 * @param message the message that was sent to the chat
	 */
	public void logChat(String username, String message) {
		String sql = "INSERT INTO chat (username, message) VALUE(?, ?)";
		excecute(sql, username, message);
	}

	/** 
	 * Adds a new user to the database
	 * @param username the username that should be added
	 * @param password the password of the user that will be hashed
	 */
	public void addUser(String username, String password) {
		String sql = "INSERT INTO player (username, hashpassword) VALUES(?, ?)";
		excecute(sql, username, Hash.md5(password));
	}

	/**
	 * Test if a username is already in the database
	 * @param username the username to test
	 * @return boolean telling if username is taken
	 */
	public boolean isUsernameTaken(String username) {
		String sql = "SELECT username FROM player WHERE username=?";
		ResultSet result = excecute(sql, username, null);
		try {
			while (result.next()) {
				if (result.getString(1).equals(username))
					return true;
			}
		} catch (SQLException e) {logger.throwing(this.getClass().getName(), "isUsernameTaken", e);}
		return false;
	}
	
	/**
	 * Checks if login infromaton is correct
	 * @param username the username to test
	 * @param password the password to test
	 * @return boolean telling if the username and password matches a set in the database
	 */
	public boolean isCorrectLogin(String username, String password) {
		String sql = "SELECT username FROM player WHERE username=? AND hashpassword=?"; //NOSONAR
		ResultSet result = excecute(sql, username, Hash.md5(password));
		try {
			while (result.next()) {
				if (result.getString(1).equals(username))
					return true;
			}
		} catch (SQLException e) {logger.throwing(this.getClass().getName(), "isCorrectLogin", e);}
		return false;

	}
}



