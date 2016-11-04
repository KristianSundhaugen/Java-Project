/**
 * hva er userGridToPlayerGrid
 * hva inneholder getUserToPlayGrid
 * hva gjør getStatus
 * 
 * 
 * - userGridToLudoBoardGrid
 */


package no.ntnu.imt3281.ludo.logic;

import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

public class Ludo {

	public static int RED = 0;
	public static int BLUE = 1;
	public static int YELLOW = 2;
	public static int GREEN = 3;
	private Vector<String> players = new Vector<>();
	private int activePlayer;
	private int dice;
	private Random randomGenerator;
	private int[][] playerPieces;
	private int[][] userGridToPlayerGrid;
	private Vector<DiceListener> diceListenerers = new Vector<>();
	private Vector<PieceListener> pieceListenerers = new Vector<>();
	private Vector<PlayerListener> playerListenerers = new Vector<>();
    public void debug(){
    	System.out.println("userGridToPlayerGrid");
		for ( int player = 0; player < 4; player++){
			System.out.println("");
			for ( int position = 0; position < 92; position++)
    			System.out.print(userGridToPlayerGrid[player][position]);

		}
    }
	public Ludo(String player1, String player2, String player3, String player4) {
		playerPieces = new int[4][60];
		userGridToPlayerGrid = new int[4][92];
		for ( int player = 0; player < 4; player++){
			playerPieces[player][0] = 4;
			for ( int position = 0; position < 16; position++)
				userGridToPlayerGrid[position/4][position] = 1;
		}
		
			
		
		addPlayer(player1);
		addPlayer(player2);
		addPlayer(player3);
		addPlayer(player4);
		if(nrOfPlayers() < 2) 
			try {
				int a = 5/0;
			}catch (Exception e) {
				throw new NotEnoughPlayersException("Not Enough Players");	
			}
			
	}
	public Ludo() {
		playerPieces = new int[4][60];
		userGridToPlayerGrid = new int[4][92];
		for ( int player = 0; player < 4; player++){
			playerPieces[player][0] = 4;
			for ( int position = 0; position < 16; position++)
				userGridToPlayerGrid[position/4][position] = 1;
		}
	}
	
	public int activePlayers() {
		return players.size();
	}
	
	public int userGridToLudoBoardGrid(int player, int possition) {
		int newPos = possition - 22 - 15 + player*13;
		if (possition > 53)
			newPos +=  (52 - 1 - 7 * player);
	    else if (newPos - 16 < 0)
	    	newPos += 52;
		return newPos;
	}
	
	public int nrOfPlayers() {
		return players.size();
	}

	public Object getPlayerName(int player) {
		return players.get(player);
	}

	public void addPlayer(String name) {
		if (nrOfPlayers() > 3)
	//		throw new NoRoomForMorePlayersException("No Room For More Players");
		if (name != null)
			players.add(name);
	}

	public void removePlayer(String playerName) {
		players.remove(playerName);		
	}

	public int getPosition(int player, int piece) {
		int newPos = 1;
		return newPos;
	}

	public int activePlayer() {
		return activePlayer;
	}
	public int throwDice() {
		int dice = (int)(Math.random()*6) + 1;
		return dice;
	}
	public int throwDice(int i) {
		return 0;
	}
	
	public boolean movePiece(int player, int piece, int diceValue) {
		return false;
	}

	public String getStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getWinner() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void addDiceListener(DiceListener diceListener) {
		diceListenerers.add(diceListener);
	}

	public void addPieceListener(PieceListener pieceListener) {
		pieceListenerers.add(pieceListener);
	}

	public void addPlayerListener(PlayerListener playerListener) {
		playerListenerers.add(playerListener);
	}
	int[][] getUserToPlayGrid() {
	    return null;		
	}
	boolean allHome() {
		return true;
	}
	void nextPlayer() {
		activePlayer++;
		if (activePlayer > 3)
			activePlayer = 0;
		
	}
    boolean canMove() {
    	return true;
    }
    boolean blocked(int possition, int height, int spiller) {
    	return true;
    }
    boolean checkBlockAt(int possition, int height, int spiler) {
    	return true;
    }
    void checkUnfortionateOpponents(int noe, int noeAnnet) {
    	
    }
    void checkWinner() {
    	
    }

}
