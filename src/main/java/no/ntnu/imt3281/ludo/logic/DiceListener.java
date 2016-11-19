package no.ntnu.imt3281.ludo.logic;
/**
 * Listen to the value gotten from active 
 * players throw in that ludo game 
 * @author Kristian
 *
 */
public interface DiceListener {

	public void diceThrown(DiceEvent event);

}
