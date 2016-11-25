package no.ntnu.imt3281.ludo.server;

import java.util.Vector;

import no.ntnu.imt3281.ludo.logic.DiceEvent;
import no.ntnu.imt3281.ludo.logic.DiceListener;
import no.ntnu.imt3281.ludo.logic.Ludo;
import no.ntnu.imt3281.ludo.logic.PieceEvent;
import no.ntnu.imt3281.ludo.logic.PieceListener;
import no.ntnu.imt3281.ludo.logic.PlayerEvent;
import no.ntnu.imt3281.ludo.logic.PlayerListener;

public class Game implements PlayerListener, DiceListener, PieceListener {
	private static int idCounter = 0;
	private Vector<ServerClient> players = new Vector<>(4);
	private Vector<ServerClient> chatters = new Vector<>();
	private Vector<ServerClient> invitedPlayers = new Vector<>(3);
	private Vector<String> chatMessages = new Vector<>();
	private String status = "WAITING";
	private String type = "OPEN";
	private String id;
	private Ludo ludoGame;
	private String name = null;
	
	/**
	 * Creating a new game with the default type open
	 */
    public Game() {
    	this.id = String.valueOf(idCounter++);
    	this.name = "Game " + id;
    	ludoGame = new Ludo();
    	ludoGame.addDiceListener(this);
    	ludoGame.addPlayerListener(this);
    	ludoGame.addPieceListener(this);
    }
    
    /**
     * Creating a new game where the type is sent with as a parameter
     * @param type the type of the game
     */
    public Game(String type) {
    	this.type = type;
    	this.id = String.valueOf(idCounter++);
    	this.name = "Game " + id;
    	ludoGame = new Ludo();
    }
    
    /**
     * Creating a new game where the type is sent with as a parameter
     * @param type the type of the game
     */
    public Game(String type, String chatName) {
    	this.type = type;
    	this.id = String.valueOf(idCounter++);
    	this.name = chatName;
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
    	if (this.status.equals("STARTED"))
    		return false;
    	else if (players.indexOf(client) != -1)
    		return false;
    	else if (isOpen() && players.size() <= 4)
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
    	sendMessageToClient("NEW_JOINED_GAME:" + players.size(), client);
    	for (ServerClient player : players) {
    		sendMessageToClient("PLAYER_JOINED:" + player.getUsername(), client);
		}
    	players.add(client);
    	ludoGame.addPlayer(client.getUsername());
    	sendMessage("PLAYER_JOINED:" + client.getUsername());
    }
	
	/**
	 * Adding the client as a chatter if the client is not already in the chat
	 * @param client
	 */
	public void addChatter(ServerClient client) {
		if (!inChat(client))
			chatters.add(client);
	}
	
	/**
	 * 
	 */
	public void clientLeave(ServerClient client) {
		this.status = "STARTED";
		ludoGame.removePlayer(client.getUsername());
		players.remove(client);
		chatters.remove(client);
	}
	
	/**
	 * Telling if the client is a chatter or a player in the game
	 * @param client the client to test
	 * @return boolean telling the result
	 */
	private boolean inChat(ServerClient client) {
		for (ServerClient chatter : chatters)
			if(chatter.equals(client))
				return true;
		for (ServerClient player : players)
			if(player.equals(client))
				return true;
		return false;
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
	public void runMessage(Message msg) {
		if (msg.isChat())
			runChatMessage(msg.getChatMessage());
		else if (msg.isGame())
			runGameMessage(msg.getGameMessage());
	}
	
	/**
	 * Responding to a chat message, logging it to the server and sending message to other clients
	 * @param cmsg the chat message received
	 */
	public void runChatMessage(ChatMessage cmsg) {
		sendChatMessage(cmsg.getClient().getUsername() + ":" + cmsg.getMessageContent());
		chatMessages.add(cmsg.getClient().getUsername() + ":" + cmsg.getMessageContent());
		logChat(cmsg.getClient().getUsername(), cmsg.getMessageContent());
	}
	/**
	 * Logging a chat message to the database
	 * @param username the user that sent the message
	 * @param messageContent the content of the message
	 */
	private void logChat(String username, String messageContent) {
		Database.getInstance().logChat(username, messageContent);
	}

	/**
	 * Responding to a game message
	 * @param gmsg the game message received
	 */
	public void runGameMessage(GameMessage gmsg) {
		switch (gmsg.getType()) {
		case "DICE_THROW": 
			if (status.equals("WAITING"))
				startGame();
			else
				ludoGame.throwDice();
			break;
		case "PIECE_CLICK":
        	int piece = gmsg.intPart(1);
			int player = gmsg.intPart(2);
			if (ludoGame.activePlayer() == player) {
				int from = ludoGame.getPosition(player, piece);
				int to = ludoGame.getPosition(player, piece) + ludoGame.getDice();
				if (from == 0)
					to = 1;
				ludoGame.movePiece(player, from, to);
			}
			break;
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
	 * Sending a game message to the client specified as a parameter
	 * @param message the content of the message to send
	 * @param client the client to send the message to
	 */
	public void sendMessageToClient(String message, ServerClient client) {
		client.sendMessage(new Message(message, "GAME", this.id).toString());
	}
	
	/**
	 * Sending a chat message to everyone in the game, or only in the chat, except for the 
	 * @param message the message to send to the chat
	 */
	public void sendChatMessage(String message) {
		for (ServerClient chatter : chatters)
			chatter.sendMessage(new Message(message, "CHAT", this.id).toString());
		for (ServerClient player : players)
			player.sendMessage(new Message(message, "CHAT", this.id).toString());	
	}
	
	/**
	 * @return if the game is full
	 */
	public boolean isFull() {
		return players.size() == 4;
	}
	
	/**
	 * @return the number of players that have been in the game
	 */
	public int getPlayers() {
		return players.size();
	}
	
	/**
	 * Starting the game
	 */
	public void startGame() {
    	sendMessage("START_GAME:" + 0);
    	this.status = "STARTED";
	}
	
	/**
	 * @return number of chatters in the game
	 */
	public int getChatterNumber() {
		return players.size() + chatters.size();
	}
	
	public String getName() {
		return this.name;
	}

	/**
	 * Called when a dice is thrown,
 	 * sending message to the clients with the information from the event
	 * @param event the event that was called from the server
	 */
	@Override
	public void diceThrown(DiceEvent event) {
		sendMessage("DICE_EVENT:" + event.getDice() 
		+ ":" + event.getPlayer() );
	}
	
	/**
	 * Called when the state of a player is changed, 
	 * sending message to the clients with the new state
	 */
	@Override
	public void playerStateChanged(PlayerEvent event) {
		sendMessage("PLAYER_EVENT:" + event.getState() 
		+ ":" + event.getPlayer() );
	}
	
	/**
	 * Triggered when a piece is moved
	 * sending messages to the players about the move
	 */
	@Override
	public void pieceMoved(PieceEvent event) {
		sendMessage("PIECE_EVENT:" + event.getFrom() 
				+ ":" + event.getTo()
				+ ":" + event.getPiece()
				+ ":" + event.getPlayer());
	}

	public boolean isChat() {
		return type.equals("CHAT");
	}

	public void invitePlayer(ServerClient client, ServerClient inviter) {
		sendMessageToClient("GAME_INVITE:" + inviter.getUsername() , client);
		invitedPlayers.add(client);
	}
	
}
