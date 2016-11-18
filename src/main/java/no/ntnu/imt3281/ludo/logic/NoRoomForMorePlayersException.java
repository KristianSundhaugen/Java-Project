package no.ntnu.imt3281.ludo.logic;

/**
 * Trying to add more players to a game than four, will
 * result in NoRoomForMorePlayers to be sent. 
 * 
 */

public class NoRoomForMorePlayersException extends RuntimeException {	
	
	/**
	 * Takes a String message which it sets as
	 * a specific runtime exception for no room for more players
	 * @param message
	 */
	public NoRoomForMorePlayersException(String message){
		super(message);
	}
}