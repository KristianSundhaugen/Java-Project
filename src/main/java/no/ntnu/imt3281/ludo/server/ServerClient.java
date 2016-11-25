package no.ntnu.imt3281.ludo.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.invoke.MethodHandles;
import java.net.Socket;
import java.util.logging.Logger;

import no.ntnu.imt3281.ludo.client.Globals;

public class ServerClient {
	private static Logger logger = Logger.getLogger(Globals.LOG_NAME);

	private static final int PING_DELAY = 2000;
	private PrintWriter output;
	private BufferedReader input;
	private long lastMessageTime = System.currentTimeMillis() + PING_DELAY;
	private String status = "CONNECTED";
	private String username;
	private boolean isLoggedIn = false;

	/**
	 * Constructor for the server client
	 * holding infomration about a client on the server
	 * sending and recieving messages from the client
	 * @param socket the socket to the client
	 */
	public ServerClient(Socket socket) {
		try {
			this.output = new PrintWriter(socket.getOutputStream(), true);
			this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			logger.throwing(this.getClass().getName(), "ServerClient", e);
		}
	}

	/**
	 * Sending a message to the client
	 * @param message the message to send
	 */
	public void sendMessage(String message) {
		System.out.println("Server sending: " + message);
		output.println(message);
	}

	/**
	 * Getting a message from the connection
	 * @return returnign the message recived or a status message
	 */
	public Message getMessage(){
		if (hasConnection()){
			try {
				if (!input.ready())
					return null;
				String msg = input.readLine();
				if (msg != null) {
					lastMessageTime = System.currentTimeMillis();
					return new Message(msg, this);
				}
			} catch (IOException e) {
				logger.throwing(this.getClass().getName(), "getMessage", e);
			}
			return null;
		} else {
			return new Message("STATUS:" + status, this);
		}
	}

	/**
	 * telling if the client have a conenction
	 * doing ping if its more than ping delay time since last connection
	 * @return boolean if the client has a connection
	 */
	public boolean hasConnection() {
		if (status == "DISCONNECTED")
			return false; 

		if (lastMessageTime < System.currentTimeMillis() - PING_DELAY){
			output.println("PING");
			this.lastMessageTime = System.currentTimeMillis();
		}

		if (output.checkError())
			this.status = "DISCONNECTED";
			
		return !output.checkError();
	}

	/**
	 * @return the username for the client
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Setting the user as logged in, and storing the username of the player
	 * @param username
	 */
	public void setLoggedIn(String username) {
		this.username = username;
		this.isLoggedIn = true;
	}

	/**
	 * @return boolean telling if the user is logged in
	 */
	public boolean isLoggedIn() {
		return this.isLoggedIn;
	}
}
