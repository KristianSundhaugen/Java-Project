package no.ntnu.imt3281.ludo.logic;

import java.util.Objects;

/**
	 * Dice Event contains a reference to Ludo game, what players are active
	 * and the value on dice. Listening to this event on the server makes it
	 * possible the get the dice value and send it to alle the players.
	 * Which then the clients can listen to and see the value of the dice thrown.
	 *
	 */
public class DiceEvent {

	private int player;
	private int dice;
	private Ludo ludo;
	
	/**
	 * Constructor for DiceEvent
	 * taking:
	 * @param ludo
	 * @param player
	 * @param dice
	 */
	public DiceEvent(Ludo ludo, int player, int dice) {
		this.dice = dice;
		this.player = player;
		this.setLudo(ludo);
	}
	
	/**
	 * Used to test if this object is the same as the one sent as a parameter
	 * @param obj the object to test against
	 */
	@Override
	public boolean equals(Object obj){
		if (obj == null)
		    return false;

		if (this.getClass() != obj.getClass())
		    return false;
		DiceEvent event = (DiceEvent)obj;
		return (event.getPlayer() == this.player && 
				event.getDice() == this.dice);
	}
    @Override
    public int hashCode() {
    	return Objects.hash(player, dice);
    }
	
	/**
	 * @return the player from the event
	 */
	public int getPlayer(){
		return player;
	}
	/**
	 * set the player int for the event
	 * @param player
	 */
	public void setPlayer(int player){
		this.player = player;
	}
	/**
	 * get the dice from the event 
	 * @return dice
	 */
	public int getDice(){
		return dice;
	}
	/**
	 * set the dice for the event
	 * @param dice
	 */
	public void setDice(int dice){
		this.dice = dice;
	}
	/**
	 * get the ludo object from the event
	 * @return
	 */
	public Ludo getLudo() {
		return ludo;
	}
	/**
	 * set the ludo object for the event
	 * @param ludo
	 */
	public void setLudo(Ludo ludo) {
		this.ludo = ludo;
	}
	

}
