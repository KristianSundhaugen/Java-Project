package no.ntnu.imt3281.ludo.logic;

public class PieceEvent {
	Ludo ludo;
	int player;
	int piece;
	int from;
	int to;

	public PieceEvent(Ludo ludo, int player, int piece, int fromPos, int toPos) {
		this.ludo = ludo;
		this.player = player;
		this.piece = piece;
		this.from = fromPos;
		this.to = toPos;
	}
	
	public PieceEvent(Object obj){
		
	}


	public int getPlayer(){
		return player;
	}
	
	public void setPlayer(int player){
		this.player = player;
	}
	
	public int getPiece(){
		return piece;
	}
	
	public void setPiece(int piece){
		this.piece = piece;
	}
	
	public int getFrom(){
		return from;
	}
	
	public void setfrom(int from){
		this.from = from;
	}
	
	public int getTo(){
		return to;
	}
	
	public void setTo(int to){
		this.to = to;
	}
	
}
