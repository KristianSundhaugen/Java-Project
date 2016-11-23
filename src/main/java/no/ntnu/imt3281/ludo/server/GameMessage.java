package no.ntnu.imt3281.ludo.server;

/**
 * Class holding a game message that is used to get the game content when messages is passed between client and server
 * @author Lasse Sviland
 *
 */
public class GameMessage {
	private Message msg;
	private String gameMessageType;
	private String gameMessageValue;
	
	/**
	 * constructor parsing a msg object and getting the relevant parts for the game message
	 * @param msg the message that contains the game message
	 */
	public GameMessage(Message msg) {
		this.msg = msg;
		String[] messageParts = msg.getMessage().split(":");
		this.gameMessageType = messageParts[0];
		if(messageParts.length > 1)
			this.gameMessageValue = messageParts[1];
	}
	
	/** 
	 * @return the id of the game sent from the server
	 */
	public String getId() {
		return msg.getId();
	}
	/** 
	 * @return the id of the game sent from the server
	 */
	public String getMessageValue() {
		return gameMessageValue;
	}
	
	/** 
	 * @return the client that sent the message
	 */
	public ServerClient getClient() {
		return msg.getClient();
	}
	
	/**
	 * @return boolean telling if it is a new game message
	 */
	public boolean isNewGame() {
		return gameMessageType.equals("NEW_JOINED_GAME");
	}

	public boolean isType(String type) {
		return gameMessageType.equals(type);
	}

	/**
	 * @return telling if it is a request to the server to start a new game
	 */
	public boolean isNewGameRequest() {
		return gameMessageType.equals("NEW_RANDOM_GAME_REQUEST");
	}
	/**
	 * @return the content of the message
	 */
	public String getMessage(){
		return this.msg.getMessage();
	}

	/**
	 * 
	 * @return the message type
	 */
	public String getType() {
		return gameMessageType;
	}
	
	/**
	 * Returning a certain part of the message out from a index
	 * @param index the index of the part
	 * @return the part at the given index
	 */
	public int part(int index) {
		String[] parts = getMessage().split(":");
    	return Integer.parseInt(parts[index]);
	}
	
}
