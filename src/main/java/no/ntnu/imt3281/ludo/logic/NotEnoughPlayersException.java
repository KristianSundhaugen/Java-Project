package no.ntnu.imt3281.ludo.logic;

/**
 * It's needed at least two players to start a game
 * if there is less than that NotEnoughPlayers will be sent
 *
 */
public class NotEnoughPlayersException extends RuntimeException {
	/**
	 * Takes a String message which it sets as
	 * a specific runtime exception for not enough players
	 * @param message
	 */
	public NotEnoughPlayersException(String message) {
        super(message);
    }
}
