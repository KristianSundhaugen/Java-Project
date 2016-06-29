/**
 * 
 */
package no.ntnu.imt3281.ludo.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * @author okolloen
 *
 */
public class LudoTest {

	/**
	 * Tests constants and constructors for class Ludo.
	 * Tests the constants for players (RED=0, BLUE=1, YELLOW=2 and GREEN=3) and 
	 * that the empty constructors initiates a game with no players. 
	 * The constructor with parameters should require at least two players, or
	 * else throw an exception.
	 * 
	 * The method nrOfPlayers should return the number of players registered for the game.
	 * 
	 * The method getPlayerName should return the name of the given player.
	 */
	@Test
	public void initialTest() {
		// Player 1 should be red, player 2 should be blue, player 3 should be yellow 
		// and player 4 should be green.
		assertEquals(0, Ludo.RED, 0);
		assertEquals(1, Ludo.BLUE, 0);
		assertEquals(2, Ludo.YELLOW, 0);
		assertEquals(3, Ludo.GREEN, 0);
		
		// Create new game, should have no players.
		Ludo ludo = new Ludo();
		assertEquals(0, ludo.nrOfPlayers(), 0);
		
		// Create new game with three players
		try {
			ludo = new Ludo("Player1", "Player2", "Player3", null);
		} catch (NotEnoughPlayersException e) {
			assertEquals("No exception should be thrown", e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(3, ludo.nrOfPlayers(), 0);
		assertEquals("Player1", ludo.getPlayerName(Ludo.RED));
		assertEquals("Player2", ludo.getPlayerName(Ludo.BLUE));
		assertEquals("Player3", ludo.getPlayerName(Ludo.YELLOW));
		assertEquals(null, ludo.getPlayerName(Ludo.GREEN));
		
		// Try to create new game with one player
		boolean exceptionThrown = false;
		try {
			ludo = new Ludo("Player1", null, null, null);
		} catch (NotEnoughPlayersException e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
	}
	
	/**
	 * Test the methods addPlayer and removePlayer. It should be possible to add
	 * up to four players. Trying to add a fifth player should throw an exception.
	 * Removing a player should not actually remove the player but mark the player
	 * as inactive. I.e. the number of players returned by nrOfPlayers should not
	 * be affected. 
	 * 
	 * The method activePlayers should be used to return the number of 
	 * active players in a game.
	 * 
	 * The method getPlayerName should return the name of the player prepended with 
	 * the string "Inactive: " for players that have been removed from the game.
	 */
	@Test
	public void addingRemovingPlayers() {
		Ludo ludo = new Ludo();
		ludo.addPlayer("Player A");
		ludo.addPlayer("Player B");
		ludo.addPlayer("Player C");
		ludo.addPlayer("Player D");
		
		assertEquals(4,  ludo.nrOfPlayers(), 0);
		
		// Try to add a fifth player, should throw an exception
		boolean exceptionThrown = false;
		try {
			ludo.addPlayer("Player E");
		} catch (NoRoomForMorePlayersException e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
		
		// Removing a player should not remove the name
		// it should only set the player to inactive
		ludo.removePlayer("Player B");
		assertEquals(4, ludo.nrOfPlayers(), 0);
		assertEquals(3, ludo.activePlayers(), 0);
		assertEquals("Player D", ludo.getPlayerName(Ludo.GREEN));
		assertEquals("Inactive: Player B", ludo.getPlayerName(Ludo.BLUE));
	}
	
	/**
	 * Test initial positions for all pieces for all players.
	 * All pieces for all players should be placed at position 0 (start field).
	 * Player RED will always start, so playersTurn should return RED.
	 */
	@Test
	public void testInitialPositions() {
		Ludo ludo = new Ludo("Player1", "Player2", null, null);
		
		// All pieces should be placed at position 0
		for (int player=0; player<4; player++) {	// RED is 0, GREEN is 3
			for (int piece=0; piece<4; piece++) {
				assertEquals(0, ludo.getPosition(player, piece), 0);
			}
		}
		
		// It should be player RED's turn. This player can throw the dice and try to move
		// his/her pieces.
		assertEquals(Ludo.RED, ludo.activePlayer(), 0);
	}
	
	/**
	 * Player must throw a six to get out of the home position.
	 * Since all pieces are placed at the home position at the beginning 
	 * of the game it means that the player must throw a six to be able to move.
	 * With all pieces at the home position a player gets three attempts at getting a six.
	 * If player does not get a six, the turn moves to the next player.
	 * 
	 * If not able to move, the turn should go to the next player.
	 * 
	 * When a user throws a six, the player gets and extra turn. Note, 
	 * the player does NOT get an extra turn if that six is used to move out of the home position.
	 * 
	 * When the user has moved (and did not get a six) turn goes to the next player.
	 */
	@Test
	public void needASixToGetStarted() {
		Ludo ludo = new Ludo("Player1", "Player2", null, null);
		
		assertEquals(Ludo.RED, ludo.activePlayer(), 0);
		ludo.throwDice(1);
		assertEquals(Ludo.RED, ludo.activePlayer(), 0);
		ludo.throwDice(2);
		assertEquals(Ludo.RED, ludo.activePlayer(), 0);
		ludo.throwDice(3);
		assertEquals(Ludo.BLUE, ludo.activePlayer(), 0);
		ludo.throwDice(4);
		assertEquals(Ludo.BLUE, ludo.activePlayer(), 0);
		ludo.throwDice(5);
		assertEquals(Ludo.BLUE, ludo.activePlayer(), 0);
		ludo.throwDice(6);
		assertEquals(Ludo.BLUE, ludo.activePlayer(), 0);
		ludo.movePiece(Ludo.BLUE, 0, 1);	// Move players piece from start(0) to square 1
		assertEquals(1, ludo.getPosition(Ludo.BLUE, 0));
		
		// Since player moved out of start, the next player should get his/her turn(s)
		assertEquals(Ludo.RED, ludo.activePlayer(), 0);		
		ludo.throwDice(4);								// RED has not left start
		assertEquals(Ludo.RED, ludo.activePlayer(), 0);		
		ludo.throwDice(3);								// Should get three attempts
		assertEquals(Ludo.RED, ludo.activePlayer(), 0);		
		ludo.throwDice(2);
		
		// It should now be the other players turn
		assertEquals(Ludo.BLUE, ludo.activePlayer(), 0);
		int dice = ludo.throwDice();					// Let server generate a throw
		ludo.movePiece(Ludo.BLUE, 1, 1+dice);			// Move the piece
		assertEquals(1+dice, ludo.getPosition(Ludo.BLUE, 0));
		
		// It should now be the other players turn
		assertEquals(Ludo.RED, ludo.activePlayer(), 0);
		
	}
}
