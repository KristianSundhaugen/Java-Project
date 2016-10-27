package no.ntnu.imt3281.ludo.logic;

public class PlayerEvent {

	public static final int WAITING = 0;
	public static final int PLAYING = 1;
	public static final int WON = 2;
	public static final int LEFTGAME = 3;
	
	private int activePlayer;
	private int state;
	
	public PlayerEvent(Ludo ludo, int bLUE, Object pLAYING2) {
		// TODO Auto-generated constructor stub
	}
	
	public PlayerEvent(Object obj){
		
	}
	
	public PlayerEvent(Object obj, int i, int j){
		
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
