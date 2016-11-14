

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
	
	/**
	 * Converting a player position to a LudoBord position
	 * @param player the player to convert from
	 * @param position the position for the player
	 * @return the position on the LudoBoard
	 */
	public int userGridToLudoBoardGrid(int player, int position) {
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
	public Object getPlayerName(int player) {
		// TODO Videre vil getPlayerName metoden returnere "Inactive: " foran navnet. Metoden kalles med navnet på spilleren som parameter og dersom denne spilleren ikke eksisterer så vil en NoSuchPlayerException bli kastet.
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
		// TODO Navnet til en spiller kan ikke starte med fire "*" (stjerner), dersom en forsøker å registrere en spiller som har et navn som starter med "****" så vil det bli kastet en IllegalPlayerNameException.
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
	 * @param player the player that owns the piece
	 * @param the piece number
	 * @return the position on the board where the piece is located
	 */
	public int getPosition(int player, int piece) {
		int pieces = 0;
		for(int pos = 0; pos < playerPieces[player].length; pos++) {
			pieces += playerPieces[player][pos];
			if (pieces >= piece)
				return pos;
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
		// TODO Metoden throwDice uten parametre skal brukes på serveren når en bruker kaster en terning. 
		// Metoden vil generere en ny terningverdi (mellom 1 og 6) for aktiv spiller. 
		// Verdien som returneres fra denne metoden kan så overføres til klientene og 
		// der kalles metoden throwDice som tar en parameter (den verdien som du fikk på serveren). 
		// På denne måten kan du være sikker på at ingen jukser. 
		// Uansett hvilken metode som kalles så vil det bli generert en DiceEvent 
		// som sendes til alle registrerte DiceListener objekter. 
		// Denne DiceEvent'en inneholder en referanse til Ludo spillet og 
		// hvilken spiller som er aktiv og verdien på terningen. 
		// På serveren kan en lytte på denne for å sende verdien på terningen til alle spillerne. 
		// På hver klient så kan en lytte på denne meldingen for å vise verdien på terningen som ble kastet.
		int dice = (int)(Math.random()*6) + 1;
		return dice;
	}
	
	/**
	 * @param The value generated from throwDice()
	 * @return Dice value of throwDice()?
	 */
	public int throwDice(int i) {
		// TODO Metoden throwDice uten parametre skal brukes på serveren når en bruker kaster en terning. 
		// Metoden vil generere en ny terningverdi (mellom 1 og 6) for aktiv spiller. 
		// Verdien som returneres fra denne metoden kan så overføres til klientene og 
		// der kalles metoden throwDice som tar en parameter (den verdien som du fikk på serveren). 
		// På denne måten kan du være sikker på at ingen jukser. 
		// Uansett hvilken metode som kalles så vil det bli generert en DiceEvent 
		// som sendes til alle registrerte DiceListener objekter. 
		// Denne DiceEvent'en inneholder en referanse til Ludo spillet og 
		// hvilken spiller som er aktiv og verdien på terningen. 
		// På serveren kan en lytte på denne for å sende verdien på terningen til alle spillerne. 
		// På hver klient så kan en lytte på denne meldingen for å vise verdien på terningen som ble kastet.
		if (this.status == "Initiated")
			this.status = "Started";
		this.dice = i;
		this.diceTrows++;
		System.out.println(i);
		System.out.println(diceTrows);
		
		if ((diceTrows == 3 && (dice != 6 && allHome())) || diceTrows > 3)
			nextPlayer();
		return i;
	}
	
	/**
	 * @param player, player to move
	 * @param position, player position
	 * @param diceValue, value of the dice
	 * @return true or false. If a player is not blocked or blocked
	 */
	public boolean movePiece(int player, int fromPos, int toPos) {
		/* TODO
		Metoden movePiece tar tre parametre, hvilken spiller en skal flytte brikken for, 
		hvor den skal flyttes fra og hvor den skal flyttes til. 
		
		NB, hvor den skal flyttes fra og hvor den skal flyttes til er sett fra spillerens synspunkt. 
		
	
		For å finne hvor dette er på spillebrettet se over. Reglene for spillet håndheves i denne metoden. 
		
		
		Metoden returnerer true dersom brikken blir flyttet og false dersom brikken ikke blir flyttet. 
		Brikken blir ikke flytter dersom det ikke er en brikke på fra posisjonen eller at til-fra ikke 
		er det samme som antall øyne på terningen. 

		Når en brikke flyttes fra start (0) til første spillefelt (1) så må en ha seks øyne på terningen. 
		For å komme i mål (59) 
		må en ha nøyaktig antall øyne på terningen. Dersom en motspiller står med to eller flere brikker på 
		hverandre mellom fra og til (inklusive) så kan det heller ikke flyttes.
		Dersom en brikke kan flyttes så vil det bli generert en PieceEvent som forteller hvilken spiller 
		som har flyttet hvilken av sine brikker og hvor den er flyttet fra og til. 
		Dersom brukeren har vunnet spillet så genereres en PlayerEvent med state lik WON. 
		Dersom brukeren ikke har vunnet og 
		ikke har kastet en sekser og brikken ikke ble flyttet ut fra start (og dermed brukeren skal få et ekstra kast) 
		så vil det bli generert to PlayerEvents (som når det kastes en terning) for å bytte aktiv spiller.

		
		*/
		
		if (isValidMove(player, fromPos, toPos)) {
			playerPieces[player][fromPos]--;
			playerPieces[player][toPos]++;
			checkUnfortionateOpponents(player, toPos);
			checkWinner();
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
		if (playerPieces[player][fromPos] == 0)
			return false;
	
		/**
		 * IF BLOCKED
		 */
		if ( blocked(player, fromPos, toPos - fromPos) )
			return false;
		
		/**
		 * IF MISMATCH DICE/MOVE LENGTH
		 */
		if (toPos - fromPos != dice)
			return false;
		
		/**
		 * FROM POS NEED TO BE BIGGER THAN TO POS
		 */
		if (fromPos <= toPos)
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
	
	/**
	 * Meant to return the current status of the game.
	 * @return "Created" until a player is added to game.
	 * @return "Initiated" until a dice is thrown in game.
	 * @return "Started" until a player has won the game.
	 * @return "Finished" when a player has won the game.
	 */
	public String getStatus() {
		// TODO Metoden getStatus returnerer status for selve spillet. status er Created inntil det 
		// er lagt til spillere i spillet. Når det er lagt til spillere så er status Initiated inntil 
		// en spiller har kastet en terning.
		// Når den første terningen er kastet så er status for spillet Started helt frem til en spiller vinner spillet, 
		// da går status over til å være Finished.
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
	 * A boolean function that sends back true
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
		dice = 0;
		diceTrows = 0;
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

    	int active = activePlayer();
    	
    	/*
    	//check all 4 pieces for the active player. If distance to goal equals dice value you can move to goal
    	for(int piece = 0; piece < 4; piece++){
    		int pos = getPosition(active, piece);
    		int distance = 59 - pos;
    		if(distance == dice){
    			return true;
    		}
    	}
    	//can move from start. Must have piece to move, dice must be 6 and position 1 can't be blocked
    	if(playerPieces[active][0] > 0 && dice == 6 && !blocked(active, 1, 1)){
			return true;
		}*/
    	
    	
    	//just use dice or get dice value some other way?
    	for (int piece = 0; piece < 4; piece++) {
    		int pos = getPosition(active, piece);
    		if (!blocked(active, pos, dice)) {
    			return true;
    		}
    	}
    	
    	
    	return false;
    }
    
    /**
     * Checks each position for block, from the current player position 
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
			if( playerNum != player && board[playerNum][pos] != 0)
				moveBack(playerNum, pos);
    }
    
    /**
     * Moving back a players piece based on the board position
     * @param playerNum the player to move back
     * @param boardPos the position on the board
     */
    private void moveBack(int playerNum, int boardPos) {
    	playerPieces[playerNum][0]++;
		int playerPos = boardPosToPlayerPos(playerNum, boardPos);
		
		/** 
		 * if there is a piece on the position
		 */
		if (playerPieces[playerNum][playerPos] != 0)
			playerPieces[playerNum][playerPos]--;
		
		/**
		 * Case where the position is on the second round 
		 * and we need to add one round to the array
		 */
		else if (playerPieces[playerNum][playerPos + 52] != 0)
			playerPieces[playerNum][playerPos + 52]--;
    }
    
    /**
     * Updating status to finished if one of the players has won the game
     */
    private void checkWinner() {
    	for (int i = 0; i < 4; i++){
    		if(playerPieces[i][59] == 4)
    			this.status = "Finished";
    	}
    }

}
