package no.ntnu.imt3281.ludo.server;

import java.util.Vector;

import no.ntnu.imt3281.ludo.logic.Ludo;

public class Game {
	private static int idCounter = 0;
	private Vector<ServerClient> players = new Vector<>(4);
	private Vector<ServerClient> invitedPlayers = new Vector<>(3);
	private String type;
	private String id;
	private Ludo ludoGame;
	
	/**
	 * Creating a new game with the default type open
	 */
    public Game() {
    	this.type = "OPEN";
    	this.id = String.valueOf(idCounter++);
    	ludoGame = new Ludo();
    }
    
    /**
     * Creating a new game where the type is sent with as a parameter
     * @param type the type of the game
     */
    public Game(String type) {
    	this.type = type;
    	this.id = String.valueOf(idCounter++);
    	ludoGame = new Ludo();
    }
    
    /**
     * Test to see if it is a open game
     * @return
     */
    public boolean isOpen() {
    	return type.equals("OPEN");
    }
    
    /**
     * Test to see if a certain client is able to join a game
     * @param client the client to test
     * @return boolean saying if the client can join the game
     */
    public boolean isJoinableByClient(ServerClient client) {
    	if (isOpen() && players.size() <= 4)
    		return true;
    	else if (!isOpen() && isPayerInvited(client))
    		return true;
    	else 
    		return false;
    }
    
    /**
     * testing if a client is invited to a game
     * @param client the client to test
     * @return boolean telling if the client was invited
     */
    private boolean isPayerInvited(ServerClient client) {
		if (invitedPlayers.indexOf(client) > -1)
			return true;
		else
			return false;
	}
    
    /**
     * Adds a player to the game
     * @param client the client that should be added to the game
     */
	public void addPlayer(ServerClient client){
    	players.add(client);
    	client.sendMessage(new Message("NEW_JOINED_GAME", "GAME", this.id).toString());
    	sendMessage("PLAYER_JOINED:" + client.toString());
    }
	
	/**
	 * @return the id of the current game
	 */
	public String getId(){
		return id;
	}

	/**
	 * Running a game message sent from a client in this game
	 * @param message the message the client sent
	 */
	public void runMessage(GameMessage message) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Sending a game message to all players in the game
	 * @param message the content of the message to send
	 */
	public void sendMessage(String message) {
		for (ServerClient player : players) {
			player.sendMessage(new Message(message, "GAME", this.id).toString());
		}
	}
	
	/**
	 * @return if the game is full
	 */
	public boolean isFull() {
		return players.size() == 4;
	}
	
	/**
	 * Starting the game
	 */
	public void startGame() {
    	sendMessage("START_GAME:" + 0);
	}
}
