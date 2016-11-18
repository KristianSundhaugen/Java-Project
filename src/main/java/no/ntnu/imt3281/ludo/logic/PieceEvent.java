package no.ntnu.imt3281.ludo.logic;

public class PieceEvent {
	private Ludo ludo;
	private int player;
	private int piece;
	private int from;
	private int to;

	public PieceEvent(Ludo ludo, int player, int piece, int fromPos, int toPos) {
		this.setLudo(ludo);
		this.player = player;
		this.piece = piece;
		this.from = fromPos;
		this.to = toPos;
	}
	
	public PieceEvent(Object obj){
		
	}
	
	@Override
	public boolean equals(Object obj){
		PieceEvent event = (PieceEvent)obj;
		return (event.getPlayer() == this.player && 
				event.getFrom() == this.from && 
				event.getTo() == this.to && 
				event.getPiece() == this.piece);
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

	public Ludo getLudo() {
		return ludo;
	}

	public void setLudo(Ludo ludo) {
		this.ludo = ludo;
	}
	
}
