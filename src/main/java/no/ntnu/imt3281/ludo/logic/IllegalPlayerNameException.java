package no.ntnu.imt3281.ludo.logic;

/**
 * If player name consists of four "*" i.e. "****"
 * there will be sent a IllegalPlayerNameException
 * saying that "That name is illegal"
 *
 */
public class IllegalPlayerNameException extends RuntimeException {
	
	/**
	 * Takes a String message which it sets as
	 * a specific runtime exception for Illegal player name
	 * @param message
	 */
	public IllegalPlayerNameException(String message) {
		super(message);
	}
}
