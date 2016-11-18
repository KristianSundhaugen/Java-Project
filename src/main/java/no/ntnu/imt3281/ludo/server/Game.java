package no.ntnu.imt3281.ludo.server;

import java.util.Vector;

import no.ntnu.imt3281.ludo.logic.DiceEvent;
import no.ntnu.imt3281.ludo.logic.DiceListener;
import no.ntnu.imt3281.ludo.logic.Ludo;
import no.ntnu.imt3281.ludo.logic.PieceEvent;
import no.ntnu.imt3281.ludo.logic.PieceListener;
import no.ntnu.imt3281.ludo.logic.PlayerEvent;
import no.ntnu.imt3281.ludo.logic.PlayerListener;

public class Game implements PieceListener, PlayerListener, DiceListener {
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
	public void runMessage(GameMessage msg) {
        if(msg.isType("PLAYER_EVENT")) {
        	String[] parts = msg.getMessage().split(":");
        	String state = parts[1];
			String player = parts[2];
		} else if (msg.isType("PIECE_EVENT")) {
			String[] parts = msg.getMessage().split(":");
        	int from = Integer.parseInt(parts[1]);
			int to = Integer.parseInt(parts[2]);
			int player = Integer.parseInt(parts[4]);
			ludoGame.movePiece(player, from, to);
			forwardMessage(msg.getMessage(), msg.getClient());
		} else if (msg.isType("DICE_EVENT")) {
			String[] parts = msg.getMessage().split(":");
        	int dice = Integer.parseInt(parts[1]);
			int player = Integer.parseInt(parts[2]);
		} else if (msg.isType("DICE_THROW")) {
			ludoGame.throwDice();
		}
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
	 * Sending a game message to all players in the game
	 * except the player that triggered the message
	 * @param message the content of the message to send
	 */
	public void forwardMessage(String message, ServerClient client) {
		for (ServerClient player : players) {
			if (player != client)
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

	/**
	 * Called when a piece have been moved, 
	 * sending message to the clients with the information from the event
	 * @param event the event that was called
	 */
	@Override
	public void pieceMoved(PieceEvent event) {
		//sendMessage("PIECE:" + event.getFrom() 
		//+ ":"  + event.getTo() 
		//+ ":" + event.getPiece() 
		//+ ":" + event.getPlayer() );		
	}

	/**
	 * Called when a dice is thrown,
 	 * sending message to the clients with the information from the event
	 * @param event the event that was called from the server
	 */
	@Override
	public void diceThrown(DiceEvent event) {
		sendMessage("DICE:" + event.getDice() 
		+ ":" + event.getPlayer() );
	}
	/**
	 * Called when the state of a player is changed, 
	 * sending message to the clients with the new state
	 */
	@Override
	public void playerStateChanged(PlayerEvent event) {
		sendMessage("PLAYER:" + event.getState() 
		+ ":" + event.getPlayer() );
	}
}
