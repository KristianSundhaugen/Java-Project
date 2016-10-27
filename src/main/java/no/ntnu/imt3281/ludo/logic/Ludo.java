package no.ntnu.imt3281.ludo.logic;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Ludo {

	public static int RED;
	public static int BLUE;
	public static int YELLOW;
	public static int GREEN;
	ArrayList<String> players = new ArrayList<>();

	public Ludo() {
		// TODO Auto-generated constructor stub
	}
	
	public Ludo(String player1, String player2, String player3, String player4) throws NotEnoughPlayersException {
		addPlayer(player1);
		addPlayer(player2);
		addPlayer(player3);
		addPlayer(player4);
		if(nrOfPlayers() < 2) 
			throw new NotEnoughPlayersException("Not Enough Players");
	}
	
	public int nrOfPlayers() {
		// TODO Auto-generated method stub
		return players.size();
	}

	public Object getPlayerName(int player) {
		// TODO Auto-generated method stub
		return null;
	}

	public void addPlayer(String name) {
		if (nrOfPlayers() > 3)
			throw new NoRoomForMorePlayersException("No Room For More Players");
		if (name != null)
			players.add(name);
		
	}

	public void removePlayer(String string) {
		// TODO Auto-generated method stub
		
	}

	public int activePlayers() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getPosition(int player, int piece) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int activePlayer() {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean movePiece(int bLUE2, int i, int j) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public int throwDice(int i) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public int throwDice() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getWinner() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int userGridToLudoBoardGrid(int rED2, int i) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void addDiceListener(DiceListener diceListener) {
		// TODO Auto-generated method stub
		
	}

	public void addPieceListener(PieceListener pieceListener) {
		// TODO Auto-generated method stub
		
	}

	public void addPlayerListener(PlayerListener playerListener) {
		// TODO Auto-generated method stub
		
	}


}
