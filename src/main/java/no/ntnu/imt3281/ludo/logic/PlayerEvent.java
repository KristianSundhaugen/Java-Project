package no.ntnu.imt3281.ludo.logic;

import java.util.Objects;

/**
 * Player event will check what state the current active player is in, 
 * has he won by having all pieces at the finish, is he still at home with all pieces
 * meaning he needs another throw, is he on the board playing, has he left 
 * the game or just waiting.
 * @author Kristian
 *
 */
public class PlayerEvent {

	public static final int WAITING = 0;
	public static final int PLAYING = 1;
	public static final int WON = 2;
	public static final int LEFTGAME = 3;
	
	private int player;
	private int state;
	private Ludo ludo;
	
	/**
	 * Constructor for player event
	 * @param ludo
	 * @param player
	 * @param state
	 */
	public PlayerEvent(Ludo ludo, int player, int state) {
		this.setLudo(ludo);
		this.player = player;
		this.state = state;	
	}
	/**
	 * Checking that current player event object is equals/true
	 */
	@Override
	public boolean equals(Object obj){
		if (obj == null)
		    return false;

		if (this.getClass() != obj.getClass())
		    return false;
		PlayerEvent event = (PlayerEvent)obj;
		return (event.getPlayer().equals(this.getPlayer()) && event.getState() == this.getState());
	}
	
    @Override
    public int hashCode() {
    	return Objects.hash(player, state);
    }
    
	/**
	 * get player string
	 * @return player
	 */
	public String getPlayer(){
		return String.valueOf(this.player);
	}
	/**
	 * get player string
	 * @return player
	 */
	public int getPlayerNumber(){
		return this.player;
	}
	/**
	 * set player to be activePlayer int 
	 * @param activePlayer
	 */
	public void setPlayer(int activePlayer){
		this.player = activePlayer;
	}
	/**
	 * get state
	 * @return state
	 */
	public int getState(){
		return state;
	}
	/**
	 * set state int
	 * @param state
	 */
	public void setState(int state){
		this.state = state;
	}
	/**
	 * gets the ludo object
	 * @return ludo
	 */
	public Ludo getLudo() {
		return ludo;
	}
	/**
	 * set the ludo object
	 * @param ludo
	 */
	public void setLudo(Ludo ludo) {
		this.ludo = ludo;
	}

}
