package no.ntnu.imt3281.ludo.logic;

public class DiceEvent {

	private int player;
	private int dice;
	private Ludo ludo;
	
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
		DiceEvent event = (DiceEvent)obj;
		return (event.getPlayer() == this.player && 
				event.getDice() == this.dice);
	}
	
	/**
	 * @return the player from the event
	 */
	public int getPlayer(){
		return player;
	}
	
	public void setPlayer(int player){
		this.player = player;
	}
	
	public int getDice(){
		return dice;
	}
	
	public void setDice(int dice){
		this.dice = dice;
	}

	public Ludo getLudo() {
		return ludo;
	}

	public void setLudo(Ludo ludo) {
		this.ludo = ludo;
	}
	

}
