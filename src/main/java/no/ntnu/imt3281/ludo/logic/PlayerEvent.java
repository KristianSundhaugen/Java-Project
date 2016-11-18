package no.ntnu.imt3281.ludo.logic;

public class PlayerEvent {

	public static final int WAITING = 0;
	public static final int PLAYING = 1;
	public static final int WON = 2;
	public static final int LEFTGAME = 3;
	
	private int player;
	private int state;
	private Ludo ludo;
	
	public PlayerEvent(Ludo ludo, int player, int state) {
		this.setLudo(ludo);
		this.player = player;
		this.state = state;	
	}
	
	@Override
	public boolean equals(Object obj){
		PlayerEvent event = (PlayerEvent)obj;
		return (event.getPlayer().equals(this.getPlayer()) && event.getState() == this.getState());
	}
	
	public String getPlayer(){
		return String.valueOf(this.player);
	}
	
	public void setPlayer(int activePlayer){
		this.player = activePlayer;
	}
	
	public int getState(){
		return state;
	}

	public void setState(int state){
		this.state = state;
	}

	public Ludo getLudo() {
		return ludo;
	}

	public void setLudo(Ludo ludo) {
		this.ludo = ludo;
	}

}
