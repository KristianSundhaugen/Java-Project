package no.ntnu.imt3281.ludo.logic;

public class PlayerEvent {

	public static final int WAITING = 0;
	public static final int PLAYING = 1;
	public static final int WON = 2;
	public static final int LEFTGAME = 3;
	
	private int activePlayer;
	private int state;
	private Ludo ludo;
	
	public PlayerEvent(Ludo ludo, int player, int state) {
		this.ludo = ludo;
		this.activePlayer = player;
		this.state = state;	
	}
	
	public PlayerEvent(Object obj){
		
	}
	
	public boolean Equals(Object obj){
		
		return true;
	}
	
	public int getActivePLayer(){
		return activePlayer;
	}
	
	public void setActivePlayer(int activePlayer){
		this.activePlayer = activePlayer;
	}
	
	public int getState(){
		return state;
	}

	public void setState(int state){
		this.state = state;
	}
}
