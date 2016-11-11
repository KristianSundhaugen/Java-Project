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
	
	/**
	 * Get number of currently active players
	 * @return currentActivePlaye
	 */
	public int activePlayers() {
		int currentActivePlayers = 0;
		for(int i = 0; i < players.size(); i++){
			if(!players.get(i).startsWith("Inactive: ")){
				currentActivePlayers++;
			}
		}
		return currentActivePlayers;
	}
	
	public int userGridToLudoBoardGrid(int player, int position) {
		int newPos = position - 22 - 15 + player*13;
		if (position > 53)
			newPos +=  (52 - 1 - 7 * player);
	    else if (newPos - 16 < 0)
	    	newPos += 52;
		return newPos;
	}
	
	public int nrOfPlayers() {
		return players.size();
	}
	/**
	 * Gets the name of a player
	 * @param player, which player to get name for
	 * @return name of player
	 */
	public Object getPlayerName(int player) {
		return players.get(player);	
	}

	public void addPlayer(String name) {
		if (nrOfPlayers() > 3)
	//		throw new NoRoomForMorePlayersException("No Room For More Players");
		if (name != null)
			players.add(name);
	}

	/**
	 * Will set Inactive in front of the player name
	 * Removes a player from the vector and adds it again with Inactive
	 * @param playerName, which player to remove
	 */
	public void removePlayer(String playerName) {
		int index = players.indexOf(playerName);
		players.remove(index);
		players.add(index, "Inactive: " + playerName);
	}

	public int getPosition(int player, int piece) {
		int newPos = 1;
		return newPos;
	}

	/**
	 * Get the active player
	 * @return active player
	 */
	public int activePlayer() {
		return activePlayer;
	}
	/**
	 * Used on the server when a user throws
	 * a dice and generate a dice value between 1 and 6
	 * @return dice value to client
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
		i = dice;
		return dice;
	}
	
	/**
	 * 
	 * @param player, player to move
	 * @param position, player position
	 * @param diceValue, value of the dice
	 * @return true or false. If a player is not blocked or blocked
	 */
	public boolean movePiece(int player, int position, int diceValue) {
		
		if(blocked(player, position, diceValue)){
			return false;
		}else{
			return true;			
		}
	}
/**
 * Meant to return the current status of the game.
 * @return "Created" until a player is added to game.
 * @return "Initiated" until a dice is thrown in game.
 * @return "Started" until a player has won the game.
 * @return "Finished" when a player has won the game.
 */
	public String getStatus() {
		if (players.size()<0)
		return "Created";
		
		else if(players.size()>0)
		return "Initiated";
		
		else if(players.size()>0 && dice > 0)
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
		for (int i = 0; i < 4; i++){
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
		int[][] board = new int[4][91];
		for(int player = 0; player < 4; player++) {
			for(int position = 0; position < 59; position++) {
				if ( playerPieces[player][position] != 0) {
					int pos = userGridToLudoBoardGrid(player, position);
					board[player][pos] = playerPieces[player][position];
			}
		}
	}
    return board;		
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
    
    /**
     * Checks each position, for block, from the current player position 
     * to the position it is supposed to move
     * @param player, which player
     * @param position, player position
     * @param diceValue, number of steps to move
     * @return true/false, if block or not blocked
     */
    boolean blocked(int player, int position, int diceValue) {
    	int height = playerPieces[player][position];
    	
    	for(int i = 0; i <= diceValue; i++){
    		if(checkBlockAt(player, position + i, height)){
    			return true;
    		}
    	}
    	return false;
    }
    
    /**
     * Checks the board position if there are more than two player on it
     * @param player, which player 
     * @param position, position to check for block
     * @param height, number of players on that position
     * @return true/false, if the field is blocked
     */
    boolean checkBlockAt(int player, int position, int height) {
    	
    	int[][] board = getUserToPlayGrid();
		int pos = userGridToLudoBoardGrid(player, position);
		for(int i = 0; i < 4; i++){
			if(board[i][pos] >= height){
				return true;
			}
		}
    	return false;
    }
    void checkUnfortionateOpponents(int player, int position) {
    	int[][] board = getUserToPlayGrid();
		int pos = userGridToLudoBoardGrid(player, position);
		for(int i = 0; i < 4; i++){
			
		}
    	
    }
    void checkWinner() {
    	
    }

}
