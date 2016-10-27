package no.ntnu.imt3281.ludo.logic;

public class DiceEvent {

	int player;
	int dice;
	
	public DiceEvent(Ludo ludo, int rED, int i) {
		// TODO Auto-generated constructor stub
	}
	
	public DiceEvent(Object obj){
		
	}
	
	//new names for parameters
	public DiceEvent(Object obj, int i, int j){
		
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
