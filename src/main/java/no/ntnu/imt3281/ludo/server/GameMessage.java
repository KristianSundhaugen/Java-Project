package no.ntnu.imt3281.ludo.server;

import java.util.logging.Logger;

import no.ntnu.imt3281.ludo.client.Globals;

/**
 * Class holding a game message that is used to get the game content when messages is passed between client and server
 * @author Lasse Sviland
 *
 */
public class GameMessage extends Message {
    private static Logger logger = Logger.getLogger(Globals.LOG_NAME);

	private String gameMessageType;
	private String gameMessageValue;
	
	/**
	 * constructor parsing a msg object and getting the relevant parts for the game message
	 * @param msg the message that contains the game message
	 */
	public GameMessage(Message msg) {
		super(msg);
		this.gameMessageType = stringPart(0);
		try {
			this.gameMessageValue = stringPart(1);
		} catch (Exception e) {
			logger.throwing(this.getClass().getName(), "GameMessage", e);
		}
			
	}
	
	/** 
	 * @return the id of the game sent from the server
	 */
	public String getMessageValue() {
		return gameMessageValue;
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
	 * @return the message type
	 */
	public String getType() {
		return gameMessageType;
	}
	
	/**
	 * @return if it is a request to the server for a player list
	 */
	public boolean isPlayerListRequest() {
		return gameMessageType.equals("PLAYER_LIST");
	}
	
	/**
	 * @return if it is a response from the server with the player list
	 */
	public boolean isPlayerListResponse() {
		return gameMessageType.equals("PLAYER_LIST_RESPONSE");
	}
	
	/**
	 * @return if it is a request for a private game
	 */
	public boolean isPrivateGameRequest() {
		return gameMessageType.equals("PRIVATE_GAME_REQUEST");
	}
	/**
	 * @return if it is a response for a private game
	 */
	public boolean isPrivateGameResponse() {
		return gameMessageType.equals("PRIVATE_GAME_RESPONSE");
	}
	/**
	 * @return if it is game invite
	 */
	public boolean isGameInvite() {
		return gameMessageType.equals("GAME_INVITE");
	}
	/**
	 * @return if game invite is accepted
	 */
	public boolean isAcceptInvite() {
		return gameMessageType.equals("INVITE_ACCEPT");

	}
	
}
