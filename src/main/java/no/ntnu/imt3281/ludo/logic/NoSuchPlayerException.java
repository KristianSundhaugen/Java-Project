package no.ntnu.imt3281.ludo.logic;

/**
 * If there is no player by that name in game,
 * NoSuchPlayer will be sent
 *
 */

public class NoSuchPlayerException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/**
	 * Takes a String message which it sets as
	 * a specific runtime exception for no such player 
	 * @param message
	 */
	public NoSuchPlayerException(String message) {
		super(message);
	}
}
