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
}
