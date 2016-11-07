package no.ntnu.imt3281.ludo.server;

public class Message {
	private String message;
	private ServerClient client;
	private String type;
	private String id;
	
	/**
	 * To be used by client to generate messages
	 * @param message the message to send to the server
	 * @param Type the type of message to send CHAT/GAME
	 */
	public Message(String message, String type, String id) {
	    this.message = message;
	    this.type = type;
	    this.id = id;
	}
	
	/**
	 * For the server to parse a message from the client
	 * @param fullMessage the hole string sent from the client
	 * @param client the client that sent the message
	 */
	public Message(String fullMessage, ServerClient client) {
		this.client = client;
		this.type = fullMessage.split(":")[0];
		this.id = fullMessage.split(":")[1];
		this.message = fullMessage.substring(type.length() + id.length() + 2);
	}
	
	
	/**
	 * Test to see if message is a game message
	 * @return true/false depending on the type
	 */
    public boolean isDisconnected() {
    	return type.equals("STATUS") && message.equals("DISCONNECTED");
    }
    
	/**
	 * Test to see if message is a game message
	 * @return true/false depending on the type
	 */
    public boolean isGame() {
    	return type.equals("GAME");
    }	
    
	/**
	 * Test to see if message is a chat message
	 * @return true/false depending on the type
	 */
    public boolean isChat() {
    	return type.equals("CHAT");
    }
    
	/**
	 * @return the type and message as a string
	 */
	public String toString() {
		return type + ":" + id + ":" + message;
	}
	
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * @return the id of the game or chat the message belongs to
	 */
	public String getId(){
		return id;
	}

	/**
	 * @return the client
	 */
	public ServerClient getClient() {
		return client;
	}
	
}