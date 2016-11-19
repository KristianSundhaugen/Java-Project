package no.ntnu.imt3281.ludo.logic;
/**
 * If a piece can be moved a pieceEvent will be generated. It tell what player
 * that can move, which piece of that player and where it is moved from and to.
 *	@author Kristian
 */
public class PieceEvent {
	private Ludo ludo;
	private int player;
	private int piece;
	private int from;
	private int to;
	
	/**
	 * constructor for PieceEvent
	 * @param ludo
	 * @param player
	 * @param piece
	 * @param fromPos
	 * @param toPos
	 */
	public PieceEvent(Ludo ludo, int player, int piece, int fromPos, int toPos) {
		this.setLudo(ludo);
		this.player = player;
		this.piece = piece;
		this.from = fromPos;
		this.to = toPos;
	}
	
	
	/** 
	 * Check that variables are equals/true
	 * to current event object
	 */
	@Override
	public boolean equals(Object obj){
		PieceEvent event = (PieceEvent)obj;
		return (event.getPlayer() == this.player && 
				event.getFrom() == this.from && 
				event.getTo() == this.to && 
				event.getPiece() == this.piece);
	}
	/**
	 * gets player int
	 * @return player
	 */
	public int getPlayer(){
		return player;
	}
	/**
	 * Set the player int
	 * @param player
	 */
	public void setPlayer(int player){
		this.player = player;
	}
	/**
	 * Gets the peice int 
	 * @return piece
	 */
	public int getPiece(){
		return piece;
	}
	/**
	 * Set the piece int
	 * @param piece
	 */
	public void setPiece(int piece){
		this.piece = piece;
	}
	/**
	 * Gets the from int
	 * @return from
	 */
	public int getFrom(){
		return from;
	}
	/**
	 * set from int
	 * @param from
	 */
	public void setfrom(int from){
		this.from = from;
	}
	/**
	 * get the to int
	 * @return to
	 */
	public int getTo(){
		return to;
	}
	/**
	 * set the to int
	 * @param to
	 */
	public void setTo(int to){
		this.to = to;
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
