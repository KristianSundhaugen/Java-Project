/**
 * 
 */
package no.ntnu.imt3281.ludo.logic;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author okolloen
 *
 */
public class LudoTest {

	@Test
	public void initialTest() {
		// Player 1 should be red, player 2 should be blue, player 3 should be yellow 
		// and player 4 should be green
		assertEquals(0, Ludo.RED, 0);
		assertEquals(1, Ludo.BLUE, 0);
		assertEquals(2, Ludo.YELLOW, 0);
		assertEquals(3, Ludo.GREEN, 0);
		
	}
}
