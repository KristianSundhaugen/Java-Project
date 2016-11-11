/**
 * hva er userGridToPlayerGrid
 * hva inneholder getUserToPlayGrid
 * hva gjÃ¸r getStatus
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
	private String status;
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
		{
			//try {
				//int a = 5/0;
			}//catch (Exception e) {
				//throw new NotEnoughPlayersException("Not Enough Players");	
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
		int currentActivePlayers = 0;
		for(int i = 0; i < players.size(); i++){
			if(!players.get(i).startsWith("Inactive: ")){
				currentActivePlayers++;
			}
		}
		return currentActivePlayers;
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
		if(players.get(player).startsWith("Inactive: ")){
			return "Inactive: " + players.get(player);
		}else{
			return players.get(player);			
		}
	}

	public void addPlayer(String name) {
		//if (nrOfPlayers() > 3)
	//		throw new NoRoomForMorePlayersException("No Room For More Players");
		if (name != null)
			players.add(name);
	}


	public void removePlayer(String playerName) {
		int index = players.indexOf(playerName);
		players.remove(index);
		players.add(index, "Inactive: " + playerName);
	}
	/**
	 * Return a given piece position of a given player
	 * @param player
	 * @param piecePosition
	 * @return playerPieces[player][piecePosition]
	 */
	public int getPosition(int player, int piecePosition) {
		return playerPieces[player][piecePosition];
	}

	public int activePlayer() {
		return activePlayer;
	}
	/**
	 * Used on the server when a user throws
	 * a dice and generate a dice value between 1 and 6
	 * @return dice valu to client
	 */
	public int throwDice() {
		int dice = (int)(Math.random()*6) + 1;
		return dice;
	}
	/**
	 * @param The value generated from throwDice()
	 * @return Dice value of throwDice()?
	 */
	public int throwDice(int i) {
		return i;
	}
	
	public boolean movePiece(int player, int piece, int diceValue) {
		return false;
	}
	/**
	 * Meant to return the current status of the game.
	 * @return "Created" until a player is added to game.
	 * @return "Initiated" until a dice is thrown in game.
	 * @return "Started" until a player has won the game.
	 * @return "Finished" when a player has won the game.
	 */
	public String getStatus() {
		if (nrOfPlayers() == 0)
		return "Created";
		
		else if(nrOfPlayers() > 0)
		return "Initiated";
		//Finne ut hva slags if statement man kan lage som gjør at den oppdaterer status til "Started"
		else if()
		return "Started";
		
		for (int i = 0; i < 4; i++){
		if(playerPieces[i][59] == 4)
		return "Finished";
		}
		return "No game created";
	}
/**
 * Returns who has won (-1 is returned until 
 * a player has won). When a player has won
 * @return RED,BLUE,YELLOW or GREEN depending on who won
 */
	public int getWinner() {
		for (int i = 0; i < players.size(); i++){
		if(playerPieces[i][59] == 4)
		return i;
		}
		return -1;
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
	    return userGridToPlayerGrid;		
	}
	/**
	 * A boolen methode that sends back true
	 * if all active players are at their start position
	 * @return true || false
	 */
	boolean allHome() {
		
		for ( int i = 0; i < 4; i++){
			playerPieces[i][0] = 4;
			for ( int j = 0; j < 16; j++)
			{
				userGridToPlayerGrid[j/4][j] = 1;
				return true;
			}
		}
		return false;
	}
	/**
	 * Get next player who is throwing dice
	 * if at max player, go back to first player
	 */
	void nextPlayer() {
		activePlayer++;
		if (activePlayer > 3)
			activePlayer = 0;
		
	}
	/**
	 * Finds out if a piece can move from its position, example
	 * if it's from the start position or getting into finish
	 * @return true
	 */
    boolean canMove() {
    	
    	return false;
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
