package no.ntnu.imt3281.ludo.logic;

public class DiceEvent {

	private int player;
	private int dice;
	private Ludo ludo;
	
	public DiceEvent(Ludo ludo, int player, int dice) {
		this.dice = dice;
		this.player = player;
		this.ludo = ludo;
	}
	
	public DiceEvent(Object obj){
		
	}
	@Override
	public boolean equals(Object obj){
		DiceEvent event = (DiceEvent)obj;
		return (event.getPlayer() == this.player && 
				event.getDice() == this.dice);
	}
	
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
	

}
