package no.ntnu.imt3281.ludo.server;

import java.util.Vector;

public class Game {
	private static int idCounter = 0;
	private Vector<ServerClient> players = new Vector<>(4);
	private Vector<ServerClient> invitedPlayers = new Vector<>(3);
	private String type;
	private String id;
	
	/**
	 * Creating a new game with the default type open
	 */
    public Game() {
    	this.type = "OPEN";
    	this.id = String.valueOf(idCounter++);
    }
    
    /**
     * Creating a new game where the type is sent with as a parameter
     * @param type the type of the game
     */
    public Game(String type) {
    	this.type = type;
    	this.id = String.valueOf(idCounter++);
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
    }
}
