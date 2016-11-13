

package no.ntnu.imt3281.ludo.logic;

import java.util.Random;
import java.util.Vector;

public class Ludo {

	public static int RED = 0;
	public static int BLUE = 1;
	public static int YELLOW = 2;
	public static int GREEN = 3;
	private Vector<String> players;
	private int activePlayer;
	private int dice;
	private int diceTrows = 0;
	private String status = "Created";
	private Random randomGenerator;
	private int[][] playerPieces;
	private int[][] userGridToPlayerGrid;
	
	private Vector<DiceListener> diceListenerers = new Vector<>();
	private Vector<PieceListener> pieceListenerers = new Vector<>();
	private Vector<PlayerListener> playerListenerers = new Vector<>();
    
	public void debug(){
    	System.out.println("userGridToPlayerGrid");
		for ( int player = 0; player < 4; player++){
			for ( int position = 0; position < 92; position++)
    			System.out.print(userGridToPlayerGrid[player][position]);
		}
    }
	public Ludo(String player1, String player2, String player3, String player4) throws NotEnoughPlayersException {
		players = new Vector<>();
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

		if(nrOfPlayers() < 2) {
			throw new NotEnoughPlayersException("Not Enough Players");	
		}
	}
			
	
	public Ludo() {
		players = new Vector<>();
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
	 * @return currentActivePlayers
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
	
	/** 
	 * @return the length of the players vector
	 */
	public int nrOfPlayers() {
		return players.size();
	}
	
	/**
	 * Gets the name of a player
	 * @param player, which player to get name for
	 * @return name of player
	 */
	public Object getPlayerName(int player) {
		try {
			return players.get(player);
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}
	
	/**
	 * Add a player to the game
	 * @param name the user name of the player to add
	 * @throws NoRoomForMorePlayersException when there is no room for more players
	 */
	public void addPlayer(String name) throws NoRoomForMorePlayersException {
		if (nrOfPlayers() > 3)
			throw new NoRoomForMorePlayersException("No Room For More Players");
		if (name != null) {
			players.add(name);
			this.status = "Initiated";
		}
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
	
	/**
	 * Return a given piece position of a given player
	 * @param player
	 * @param piecePosition
	 * @return playerPieces[player][piecePosition]
	 */
	public int getPosition(int player, int piecePosition) {
		return playerPieces[player][piecePosition];
	}

	/**
	 * Get the active player
	 * @return active player number
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
		if (this.status == "Initiated")
			this.status = "Started";
		this.dice = i;
		this.diceTrows++;
		return i;
	}
	
	/**
	 * @param player, player to move
	 * @param position, player position
	 * @param diceValue, value of the dice
	 * @return true or false. If a player is not blocked or blocked
	 */
	public boolean movePiece(int player, int position, int diceValue) {
		
		if ( blocked(player, position, diceValue) ){
			return false;
		} else {
			int pieceNumber = playerPieces[player][position];
			playerPieces[player][position] = 0;
			playerPieces[player][position + diceValue] = pieceNumber;
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
		checkWinner();
		return status;
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
	
	/**
	 * A boolen methode that sends back true
	 * if all active players are at their start position
	 * @return true || false
	 */
	boolean allHome() {
		boolean allHome = true;
		for ( int i = 0; i < 4; i++){
			playerPieces[i][0] = 4;
			for ( int j = 0; j < 16; j++) {
				if(userGridToPlayerGrid[j/4][j] != 1) {
					allHome = false;
				}
				
			}
		}
		return allHome;
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
    
    /**
     * Checks a position for opponent pieces. If there are any
     * and the player is moving there, the opponent will be returned 
     * to start
     * @param player, which player to check
     * @param position, which position to check
     */
    void checkUnfortionateOpponents(int player, int position) {
    	
    	int[][] board = getUserToPlayGrid();
		int pos = userGridToLudoBoardGrid(player, position);
		for(int i = 0; i < 4; i++){
			if(board[i][pos] != 0 && i != activePlayer){
				playerPieces[i][0]++;
			}
		}
    }
    
    /**
     * Updating status to finished if one of the players has won the game
     */
    void checkWinner() {
    	for (int i = 0; i < 4; i++){
    		if(playerPieces[i][59] == 4)
    			this.status = "Finished";
    	}
    }

}
