

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
		
		Ludo ludo = new Ludo();
		randomGenerator = new Random();
		dice = randomGenerator.nextInt(6) + 1;
		diceThrows++;
		
		DiceEvent diceThrow = new DiceEvent(ludo, activePlayer, dice);
		
		for(int i = 0; i < diceListenerers.size(); i++){
			diceListenerers.get(i).diceThrown(diceThrow);
		}
		
		//if you can't move any pieces
		if(!canMove()){
			nextPlayer();
		}
		//when you have thrown three times
		if(diceThrows > 3){
			nextPlayer();
		}
		//Throw the dice 3 times until you get a six and move a piece out
		if(dice != 6 && allHome() && diceThrows < 3){
			nextPlayer();
		}
		//a piece can be moved out and you can throw again
		if(dice == 6 && allHome()){
			diceThrows = 0;
		}
		//you can't throw more than three times, even if you get a six the third time
		if(diceThrows == 3 && dice == 6){
			nextPlayer();
		}
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
		this.diceThrows++;
		System.out.println(i);
		System.out.println(diceThrows);
		
		if ((diceThrows == 3 && (dice != 6 && allHome())) || diceThrows > 3)
			nextPlayer();
		return i;
	}
	
	/**
	 * @param player, player to move
	 * @param position, player position
	 * @param diceValue, value of the dice
	 * @return true or false. If a player is not blocked or blocked
	 */
	public boolean movePiece(int player, int position, int diceValue) {
		/* TODO
		Metoden movePiece tar tre parametre, hvilken spiller en skal flytte brikken for, hvor den skal flyttes fra og hvor den skal flyttes til. 
		NB, hvor den skal flyttes fra og hvor den skal flyttes til er sett fra spillerens synspunkt. 
		For å finne hvor dette er på spillebrettet se over. Reglene for spillet håndheves i denne metoden. 
		Metoden returnerer true dersom brikken blir flyttet og false dersom brikken ikke blir flyttet. 
		Brikken blir ikke flytter dersom det ikke er en brikke på fra posisjonen eller at til-fra ikke er det samme som antall øyne på terningen. 

		Når en brikke flyttes fra start (0) til første spillefelt (1) så må en ha seks øyne på terningen. For å komme i mål (59) 
		må en ha nøyaktig antall øyne på terningen. Dersom en motspiller står med to eller flere brikker på hverandre mellom fra og til (inklusive) så kan det heller ikke flyttes.
		Dersom en brikke kan flyttes så vil det bli generert en PieceEvent som forteller hvilken spiller som har flyttet hvilken av sine brikker og hvor den er flyttet fra og til. 
		Dersom brukeren har vunnet spillet så genereres en PlayerEvent med state lik WON. Dersom brukeren ikke har vunnet og 
		ikke har kastet en sekser og brikken ikke ble flyttet ut fra start (og dermed brukeren skal få et ekstra kast) 
		så vil det bli generert to PlayerEvents (som når det kastes en terning) for å bytte aktiv spiller.

		Dersom brikken som ble flyttet havnet på toppen av en enkelt av en motspillers brikker så skal denne brikken sendes tilbake til start. 
		Dette gjøres ved at det i så tilfelle genereres en PieceEvent hvor player er satt til den spilleren som skal få sin brikke sendt tilbake til start. 
		piece forteller hvilken brikke det er, og from og to forteller hvor brikken flyttes fra og hvor den flyttes til.
		*/
		boolean moved = false;
		if ( !blocked(player, position, diceValue) ) {
			int pieceNumber = playerPieces[player][position];
			playerPieces[player][position] = 0;
			try {
				playerPieces[player][position + diceValue] = pieceNumber;
				moved = true;
			} catch (Exception e) {}
		}
		if (dice != 6 )
			nextPlayer();
		return moved;
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
		diceThrows = 0;
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
    	for(int piece = 0; piece < 4; piece++){
    		int pos = getPosition(active, piece);
    		//if position is 0 dice has to be six. If position is 59 you can't move
    		if(pos == playerPieces[active][59]){
    			return false;
    		}
    		else if(pos == playerPieces[active][0]){
    			if(dice == 6){
    				return true;
    			}
    		}
    		if(blocked(active, pos, dice)){
    			return false;
    		}
    	}
    	
    	
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
