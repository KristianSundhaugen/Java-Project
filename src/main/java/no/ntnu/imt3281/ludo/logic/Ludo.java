

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
	private int diceThrows = 0;
	private String status = "Created";
	private Random randomGenerator;
	public int[][] playerPieces;
	private int[][] userGridToPlayerGrid;
	
	private Vector<DiceListener> diceListenerers = new Vector<>();
	private Vector<PieceListener> pieceListenerers = new Vector<>();
	private Vector<PlayerListener> playerListenerers = new Vector<>();
	public void debug(){
    	System.out.println("userGridToPlayerGrid");
    	int[][] board = playerPieces;
    	//int[][] board = getUserToPlayGrid();
		for ( int player = 0; player < 4; player++){
			for ( int position = 0; position < board[0].length; position++)
    			System.out.print(board[player][position] + " : ");
			System.out.println("");
		}
		System.out.println("");

    }
	/**
	 * Constructor creating new game with players
	 * @param player1 the user name of the first player, can be null if there is no player
	 * @param player2 the user name of the second player, can be null if there is no player
	 * @param player3 the user name of the third player, can be null if there is no player
	 * @param player4 the user name of the fourth player, can be null if there is no player
	 * @throws NotEnoughPlayersException throws exception if less than two of the player variables is used
	 */
	public Ludo(String player1, String player2, String player3, String player4) throws NotEnoughPlayersException {
		setupGame();
		addPlayer(player1);
		addPlayer(player2);
		addPlayer(player3);
		addPlayer(player4);

		if(nrOfPlayers() < 2) {
			throw new NotEnoughPlayersException("Not Enough Players");	
		}
	}
			
	/**
	 * Constructor that creates new games with no players
	 */
	public Ludo() {
		setupGame();
	}
	
	/**
	 * Setting up the game, creating players vector, setting up pieces to be in the start positions
	 */
	public void setupGame() {
		players = new Vector<>();
		playerPieces = new int[4][4];
		userGridToPlayerGrid = new int[4][92];
		for ( int player = 0; player < 4; player++){
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
	
	/**
	 * Converting a player position to a LudoBord position
	 * @param player the player to convert from
	 * @param position the position for the player
	 * @return the position on the LudoBoard
	 */
	public int userGridToLudoBoardGrid(int player, int position) {
		if (position == 0)
			return player*4;
		
		int newPos = position - 22 - 15 + player*13;
		if (position > 53)
			newPos +=  (52 - 1 - 7 * player);
	    else if (newPos - 16 < 0)
	    	newPos += 52;
		return newPos;
	}
	
	/** 
	 * @return the numbers of players that have been in the game
	 */
	public int nrOfPlayers() {
		return players.size();
	}
	
	/**
	 * Gets the name of a player
	 * @param player, which player to get name for
	 * @return name of player
	 */
	public Object getPlayerName(int player) throws NoSuchPlayerException {
		try {
			if (players.get(player) == null)
				throw new NoSuchPlayerException("No player by this name");
			else
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
	public void addPlayer(String name) throws NoRoomForMorePlayersException, IllegalPlayerNameException {
		if (nrOfPlayers() > 3)
			throw new NoRoomForMorePlayersException("No Room For More Players");
		if (name != null) {
			if (name.startsWith("****"))
				throw new IllegalPlayerNameException("Illegal Player Name");
		
			players.add(name);
			setStatus("Initiated");
			PlayerEvent playerChange = new PlayerEvent(this, players.indexOf(name), PlayerEvent.PLAYING);
			for (int i = 0; i < playerListenerers.size(); i++) {
				playerListenerers.get(i).playerStateChanged(playerChange);
			}
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
		PlayerEvent playerChange = new PlayerEvent(this, activePlayer(), PlayerEvent.LEFTGAME);
		for (int i = 0; i < playerListenerers.size(); i++) {
			playerListenerers.get(i).playerStateChanged(playerChange);
		}
		
		if( activePlayer() == index)
			nextPlayer();
	}
	
	/**
	 * Return a given piece position of a given player
	 * @param player the player that owns the piece
	 * @param the piece number
	 * @return the position on the board where the piece is located
	 */
	public int getPosition(int player, int piece) {
		return playerPieces[player][piece];
	}
	
	/**
	 * Finding out what piece is on the requested position
	 * @param player the player that owns the piece
	 * @param position the position that is requested
	 * @return the piece on the position
	 */
	public int getPiece(int player, int position) {
		for (int i = 0; i < 4; i++) {
			if (playerPieces[player][i] == position) 
				return i;
		}
		return -1;
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
		if (getStatus() == "Initiated")
			setStatus("Started");
		
		randomGenerator = new Random();
		dice = randomGenerator.nextInt(6) + 1;
		diceThrows++;
		
		DiceEvent diceThrow = new DiceEvent(this, activePlayer(), dice);
		for (int i = 0; i < diceListenerers.size(); i++) {
			diceListenerers.get(i).diceThrown(diceThrow);
		}
		
		if (shouldGoToNextPlayer() || (!canMove() && !allHome()))
			nextPlayer();
		
		return dice;
	}
	

	/**
	 * @param The value generated from throwDice()
	 * @return Dice value of throwDice()?
	 */
	public int throwDice(int diceValue) {
		if (getStatus() == "Initiated")
			setStatus("Started");
		
		this.dice = diceValue;
		diceThrows++;
		
		DiceEvent diceThrow = new DiceEvent(this, activePlayer(), diceValue);
		for (int i = 0; i < diceListenerers.size(); i++) {
			diceListenerers.get(i).diceThrown(diceThrow);
		}
		if (shouldGoToNextPlayer() || (!canMove() && !allHome()))
			nextPlayer();
		
		return diceValue;
	}
	
	/**
	 * Testing if the turn should go to the next player
	 * @return if the turn should change
	 */
	public boolean shouldGoToNextPlayer(){
		
		/**
		 * Should not go to next player if the game is finished
		 */
		if ( this.status == "Finished"){
			return false;
		}
			
		/**
		 * When you have thrown three times
		 */
		if (diceThrows > 3){
			return true;
		}
		
		/**
		 * Throw the dice 3 times until you get a six and move a piece out
		 */
		if (dice != 6 && allHome() && diceThrows >= 3){
			return true;
		}
		
		/**
		 *  You can't throw more than three times, even if you get a six the third time
		 */
		if (diceThrows == 3 && dice == 6 && !allHome()){
			return true;
		}
		
		return false;
	}
	
	
	/**
	 * @param player, player to move
	 * @param position, player position
	 * @param diceValue, value of the dice
	 * @return true or false. If a player is not blocked or blocked
	 */
	public boolean movePiece(int player, int fromPos, int toPos) {
		int piece = getPiece(player, fromPos);
		if (isValidMove(player, fromPos, toPos)) {
			playerPieces[player][piece] = toPos;
						
			PieceEvent pieceMove = new PieceEvent(this, activePlayer, piece, fromPos, toPos);
			for(int i = 0; i < pieceListenerers.size(); i++){
				pieceListenerers.get(i).pieceMoved(pieceMove);
			}
			
			checkUnfortionateOpponents(player, toPos);
			checkWinner();
			if(shouldGoToNextPlayer() || fromPos == 0 || dice != 6)
				nextPlayer();

			return true;
		}
		
		return false;

	}

	
	/**
	 * Boolean testing if the conditions of a move is valid, need move have to be the same length as the dice value,
	 * there need to be a piece on the from position and the piece can not be blocked, the positions have to be valid values
	 * @param player the player that is trying to move
	 * @param fromPos the position the player is trying to move from
	 * @param toPos the position the player is trying to move to
	 * @return boolean telling if the tests passed
	 */
	private boolean isValidMove(int player, int fromPos, int toPos) {
		/**
		 * IF NO PIECE
		 */
		if (getPiece(player, fromPos) == -1)
			return false;
		
	
		/**
		 * IF BLOCKED
		 */
		if ( blocked(player, fromPos, toPos) )
			return false;
		
		
		/**
		 * IF MISMATCH DICE/MOVE LENGTH
		 */
		if (toPos - fromPos != dice && toPos != 1)
			return false;
		
		
		
		/**
		 * FROM POS NEED TO BE BIGGER THAN TO POS
		 */
		if (toPos <= fromPos) 
			return false;
		
		
		/**
		 * POSITIONS NEED TO BE ON THE BOARD
		 */
		if ( fromPos > 59 || toPos > 59)
			return false;
		
		
		/**
		 * NEED VALUE 6 ON DICE TO MOVE FROM HOME
		 */
		if (fromPos == 0 && dice != 6)
			return false;
		
		
		/**
		 * WHEN MOVING FROM HOME YOU CAN ONLY MOVE 1
		 */
		if (fromPos == 0 && toPos != 1 )
			return false;
		
		
		/**
		 * EVERYTHING GOOD
		 */
		return true;
	}
	
	public void setStatus(String status)
	{
		this.status = status;
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
		for (int player = 0; player < players.size(); player++){
			int pieces = 0;
        	for (int piece = 0; piece < 4; piece++){
        		if(playerPieces[player][piece] == 59)
        			pieces++;
        	}
        	if (pieces == 4)
				return player;
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
			for(int piece = 0; piece < 4; piece++) {
				board[player][userGridToLudoBoardGrid(player,playerPieces[player][piece])]++;
			}
		}
		return board;		
	}
	
	/**
	 * A boolean function that sends back true
	 * if all active players are at their start position
	 * @return true || false
	 */
	boolean allHome() {
		int homeCount = 0;
		for (int i = 0; i < 4; i++) {
			if (playerPieces[activePlayer()][i] == 0)
				homeCount++;
		}
		return (homeCount == 4 );
	}
	
	/**
	 * Get next player who is throwing dice
	 * if at max player, go back to first player
	 */
	void nextPlayer() {
		PlayerEvent playerChange = new PlayerEvent(this, activePlayer(), PlayerEvent.WAITING);
		for (int i = 0; i < playerListenerers.size(); i++) {
			playerListenerers.get(i).playerStateChanged(playerChange);
		}
		
		dice = 0;
		diceThrows = 0;

		activePlayer++;
		while (!isActive(activePlayer)) {
			activePlayer++;
			if (activePlayer > nrOfPlayers())
				activePlayer = 0;
		}

		playerChange = new PlayerEvent(this, activePlayer(), PlayerEvent.PLAYING);
		for (int i = 0; i < playerListenerers.size(); i++) {
			playerListenerers.get(i).playerStateChanged(playerChange);
		}
	}
	
	/**
	 * Finds out if a piece can move from its position, example
	 * if it's from the start position or getting into finish
	 * @return true
	 */
    boolean canMove() {
    	for (int piece = 0; piece < 4; piece++) {
    		if (pieceCanMove(activePlayer(), piece))
    			return true;
    	}
    	return false;
    }
    
    /**
     * Testing if a specific piece can move
     * @param player the owner of the piece
     * @param position the position of the piece
     * @return if the piece can move
     */
    boolean pieceCanMove(int player, int piece) {
    	if (playerPieces[player][piece] == 59)
    		return false;
    	
    	if (playerPieces[player][piece] == 0 && dice != 6)
    		return false;
    	
    	if (blocked(player, playerPieces[player][piece], playerPieces[player][piece] + dice))
    		return false;
    	
    	if (playerPieces[player][piece] + dice > 59)
    		return false;
    	return true;
    }
    
    /**
     * Checks each position for block, from the current player position 
     * to the position it is supposed to move
     * @param player, which player
     * @param position, player position
     * @param diceValue, number of steps to move
     * @return true/false, if block or not blocked
     */
    boolean blocked(int player, int fromPos, int toPos) {
    	for (int i = 1; i <= toPos - fromPos; i++) {
    		if ( checkBlockAt(player, fromPos + i) )
    			return true;
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
    boolean checkBlockAt(int player, int position) {
    	int[][] board = getUserToPlayGrid();
		int boardPosition = userGridToLudoBoardGrid(player, position);
		for (int testPlayer = 0; testPlayer < 4; testPlayer++) {
			if (board[testPlayer][boardPosition] > 1 && testPlayer != player )
				return true;
		}
    	return false;
    }
    
    /**
     * Converts a position on the board to a player position, 
     * when 1 is returned the position might be both 1 and 53
     * @param player the player number to convert to
     * @param pos the board position to convert from
     * @return the player position
     */
    int boardPosToPlayerPos(int player, int pos) {
    	int diff = pos - 15 - player * 13;
    	if (diff < 1) 
    		diff += 52;
   		return diff;
    }
    
    /**
     * Checks a position for opponent pieces. If there are any
     * and the player is moving there, the opponent will be returned 
     * to start
     * @param player, which player to check
     * @param position, which position to check
     */
    private void checkUnfortionateOpponents(int player, int position) {
    	int[][] board = getUserToPlayGrid();
		int pos = userGridToLudoBoardGrid(player, position);
		/**
		 * Testing each of the players, if they have a piece in the position
		 * using moveBack if there is a piece there
		 */
		for(int playerNum = 0; playerNum < 4; playerNum++)
			if( playerNum != player && board[playerNum][pos] != 0){
				moveBack(playerNum, pos);
    
			}}
    
    /**
     * Moving back a players piece based on the board position
     * @param playerNum the player to move back
     * @param boardPos the position on the board
     */
    private void moveBack(int player, int position) {
		int playerPos = boardPosToPlayerPos(player, position);
		int piece = getPiece(player, playerPos);

		if (piece == -1)
			piece = getPiece(player, position + 52);

		if (piece != -1)
			if (userGridToLudoBoardGrid(player, playerPieces[player][piece]) == position) {
				PieceEvent pieceMove = new PieceEvent(this, player, piece, playerPieces[player][piece], 0);
				for (int i = 0; i < pieceListenerers.size(); i++) {
					pieceListenerers.get(i).pieceMoved(pieceMove);
				}
				playerPieces[player][piece] = 0;
			
			}
    }
    
    /**
     * Updating status to finished if one of the players has won the game
     */
    private void checkWinner() {
    	for (int player = 0; player < 4; player++){
    		int pieces = 0;
        	for (int piece = 0; piece < 4; piece++){
        		if(playerPieces[player][piece] == 59)
        			pieces++;
        	}
        	if (pieces == 4) {
        		PlayerEvent playerChange = new PlayerEvent(this, activePlayer(), PlayerEvent.WON);
    			for (int i = 0; i < playerListenerers.size(); i++) {
    				playerListenerers.get(i).playerStateChanged(playerChange);
    			}
    			setStatus("Finished");;
        	}
    	}
    }
    
    /**
     * Is active are returning a true, if a player is not Inactive or not there.
     * @return true || false
     */
    private boolean isActive(int player) {	
    	String playerName;
    	try {
    		playerName = players.get(player);
		} catch (Exception e) {
    		return false;
		}

    	if (playerName.startsWith("Inactive: ")) {		
    		return false;
    	}
    	return true;
    }

}
