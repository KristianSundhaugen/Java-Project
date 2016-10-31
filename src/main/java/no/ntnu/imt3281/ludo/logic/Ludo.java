package no.ntnu.imt3281.ludo.logic;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

public class Ludo {

	public static int RED;
	public static int BLUE;
	public static int YELLOW;
	public static int GREEN;
	Vector<String> players = new Vector<>();
	int activePlayer;
	int dice;
	Random randomGenerator;
	int[][] playerPieces;
	int[][] userGridToPlayerGrid;
	Vector<DiceListener> diceListenerers = new Vector<>();
	Vector<PieceListener> pieceListenerers = new Vector<>();
	Vector<PlayerListener> playerListenerers = new Vector<>();
    
	public Ludo(String player1, String player2, String player3, String player4) throws NotEnoughPlayersException {
		addPlayer(player1);
		addPlayer(player2);
		addPlayer(player3);
		addPlayer(player4);
		if(nrOfPlayers() < 2) 
			throw new NotEnoughPlayersException("Not Enough Players");
	}
	public Ludo() {
		// TODO Auto-generated constructor stub
	}
	
	public int userGridToLudoBoardGrid(int noe, int noeAnnet) {
		return noeAnnet;
	}
	
	public int nrOfPlayers() {
		// TODO Auto-generated method stub
		return players.size();
	}
	
	public int activePlayers() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Object getPlayerName(int player) {
		// TODO Auto-generated method stub
		return null;
	}

	public void addPlayer(String name) {
		if (nrOfPlayers() > 3)
	//		throw new NoRoomForMorePlayersException("No Room For More Players");
		if (name != null)
			players.add(name);
	}

	public void removePlayer(String string) {
		// TODO Auto-generated method stub
		
	}

	public int getPosition(int player, int piece) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int activePlayer() {
		// TODO Auto-generated method stub
		return 0;
	}
	public int throwDice() {
		// TODO Auto-generated method stub
		int dice = (int)(Math.random()*6) + 1;
		return dice;
	}
	public int throwDice(int i) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public boolean movePiece(int bLUE2, int i, int j) {
		// TODO Auto-generated method stub
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
		
	}
    boolean canMove() {
    	return true;
    }
    boolean blocked(int noe, int noeAnnet, int endaNoeAnnet) {
    	return true;
    }
    boolean checkBlockAt(int noe, int noeAnnet, int endaNoeAnnet) {
    	return true;
    }
    void checkUnfortionateOpponents(int noe, int noeAnnet) {
    	
    }
    void checkWinner() {
    	
    }

}
