package no.ntnu.imt3281.ludo.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Logger;

import no.ntnu.imt3281.ludo.client.Globals;

/**
 * Class for holding the server, letting clients send messages, and pass messages to the clients
 * @author Lasse Sviland
 *
 */
public class Server {
    private static Logger logger = Logger.getLogger(Globals.LOG_NAME);
	private ArrayList<ServerClient> clients  = new ArrayList<>();
	private ArrayList<Game> games = new ArrayList<>();
    private ServerMessageReader reader;
	private boolean stop = false;
    private static Server server;
    private Database database;
    
	/**
     * Constructor for the server, Creating a socket and accepting new clients
     * @throws IOException
     */
    public Server() throws IOException {
    	this.database = Database.getInstance();
    	this.reader = new ServerMessageReader(this);
        new Thread(reader).start();

		ServerSocket listener = new ServerSocket(Globals.SERVER_PORT);
        while(!stop) {
        	Socket socket = listener.accept();
        	clients.add(new ServerClient(socket));
        }
        listener.close();
    }
    
    /**
     * Main function to start the program
     * @param args
     */
	public static void main(String[] args) {
        try {
        	logger.info("Server Starting");
			server = new Server();
		} catch (IOException e) {
			logger.throwing(Server.class.getClass().getName(), "main", e);
		}
        Runtime.getRuntime().addShutdownHook(new Thread() {            
        	@Override
        	public void run() { 
        		server.stop();
        	}        
        });        
	}
    
    /**
     * Stopping the reading from reading messages from the connected clients
     */
    protected void stop() {
		if(reader != null)
			reader.stop();
		this.stop = true;
	}

	/**
	 * Removing a client that have disconnected
	 * @param client the client to remove
	 */
	public void removeClient(ServerClient client) {
		for (Game game : games) {
			game.clientLeave(client);
		}
		clients.remove(client);
	}
	
	/**
	 * Sending a message object to the game the message belongs to
	 * @param message the message object to send
	 */
	public void parseMessage(Message msg) {
		boolean messageParsed = false;
		if (msg.isUser()){
			parseUserMessage(msg.getUserMessage());
		} else if (msg.getClient().isLoggedIn()){
			if (msg.isChat())
				messageParsed = parseChatMessage(msg.getChatMessage());
			else if (msg.isGame())
				messageParsed = parseGameMessage(msg.getGameMessage());
			if (!messageParsed)
				parseSpecifficGameMessage(msg);
		}
	}
	
	/**
	 * Parsing cases for a game message
	 * @param msg the game message to parse
	 * @return boolean telling if the message was parsed
	 */
	private boolean parseGameMessage(GameMessage msg) {
		if (msg.isNewGameRequest())
			joinNewRandomGame(msg);
		else if (msg.isPrivateGameRequest())
			createPrivateGame(msg);
		else if (msg.isPlayerListRequest())
			sendPlayerList(msg);
		else if (msg.isAcceptInvite())
			acceptInvite(msg);
		else
			return false;
		return true;
	}
	
	/**
	 * Parsing cases for a chat message
	 * @param msg the chat message to parse
	 * @return boolean telling if the message was parsed
	 */
	private boolean parseChatMessage(ChatMessage msg) {
		if (msg.isListRequest())
			sendChatList(msg);
		else if (msg.isNewChatJoin())
			joinNewChat(msg);
		else if (msg.isNewChat())
			createNewChat(msg);
		else
			return false;
		return true;
	}
	
	/**
	 * Parsing cases for a user message
	 * @param msg the user message to parse
	 * @return boolean telling if the message was parsed
	 */
	private boolean parseUserMessage(UserMessage msg) {
		if(msg.isLoginRequest())
			userLogin(msg);
		else if(msg.isRegisterRequest())
			userRegister(msg);
		else
			return false;
		return true;
	}
	
	/**
	 * running a message for a specific game
	 * @param msg the message to run
	 */
	private void parseSpecifficGameMessage(Message msg) {
		for (Game game : games)
			if (game.getId().equals(msg.getId()))
				game.runMessage(msg);
	}
	
	/**
	 * @param cmsg adding the client to the requested chat
	 */
	private void joinNewChat(ChatMessage cmsg) {
		for (Game game : games) {
			if (game.getId().equals(cmsg.getId())) {
				game.addChatter(cmsg.getClient());
			}
		}		
	}
	/**
	 * @param cmsg adding the client to the requested chat
	 */
	private void createNewChat(ChatMessage cmsg) {
		Game game = new Game("CHAT", cmsg.stringPart(1));
		game.addChatter(cmsg.getClient());
		games.add(game);
		cmsg.getClient().sendMessage(new ChatMessage("CHAT_JOINED:" + cmsg.stringPart(1), game.getId()).toString());
	}
	
	/**
	 * @param cmsg sending list over chats the user can join
	 */
	private void sendChatList(ChatMessage cmsg) {
		StringBuilder msg = new StringBuilder();
		for (Game game : games) {
			if (game.isOpen() || game.isChat())
				msg.append(":" + game.getId() + "-" + game.getChatterNumber() + "-" + game.getName());
		}
		cmsg.getClient().sendMessage(new Message("LIST_ROOMS_RESPONSE" + msg.toString(), "CHAT", "-1").toString());	
	}
	
	/**
	 * @param cmsg sending list over chats the user can join
	 */
	private void sendPlayerList(GameMessage cmsg) {
		StringBuilder msg = new StringBuilder();
		for (ServerClient client : clients)
			if (!cmsg.getClient().equals(client))
				msg.append(":" + client.getUsername());
		cmsg.getClient().sendMessage(new Message("PLAYER_LIST_RESPONSE" + msg.toString(), "GAME", "-1").toString());	
	}

	/**
	 * Adding player to a new game, creating a new one if there is none to join
	 * @param gmsg the game message received from the client
	 */
	private void joinNewRandomGame(GameMessage gmsg) {
		boolean gameFound = false;
		for (Game game : games) {
			if (game.isJoinableByClient(gmsg.getClient())) {
				game.addPlayer(gmsg.getClient());
				gameFound = true;
				if (game.isFull()) {
					game.startGame();
				}
			}
		}
		if (!gameFound){
			Game game = new Game();
			game.addPlayer(gmsg.getClient());
			games.add(game);
		}
	}
	
	/**
	 * Adding the player that accepted the game invite to the game
	 * @param gmsg the game message received from the client
	 */
	private void acceptInvite(GameMessage gmsg) {
		for (Game game : games) {
			if (game.isJoinableByClient(gmsg.getClient()) && game.getId().equals(gmsg.getId())) {
				game.addPlayer(gmsg.getClient());
				if (game.isFull()) {
					game.startGame();
				}
			}
		}
	}

	/**
	 * Creating a new private game, and inviting users
	 * @param gmsg the game message received from the client
	 */
	private void createPrivateGame(GameMessage gmsg) {
		Game game = new Game("CLOSED");
		game.addPlayer(gmsg.getClient());

		for (String player : gmsg.getMessage().split(":")) {
			if (getClient(player) != null)
				game.invitePlayer(getClient(player), gmsg.getClient());
		}
		games.add(game);
	}
	
	/**
	 * Username and password is checked in the database
	 * If they are found and are correct the user will be logged in
	 * If not the user will get a notification
	 * @param lmessage, message recived from client
	 */
	private void userLogin(UserMessage umsg){
		if(database.isCorrectLogin(umsg.stringPart(1), umsg.stringPart(2))) {
			umsg.getClient().sendMessage(new Message("LOGIN_RESPONSE:0", "USER", "-1").toString());
			umsg.getClient().setLoggedIn(umsg.stringPart(1));
		} else {
			umsg.getClient().sendMessage(new Message("LOGIN_RESPONSE:1", "USER", "-1").toString());
		}
	}
	
	/**
	 * Check if the username already exist in the database
	 * If not the user is registered and can log in
	 * If not the user will get a notification
	 * @param rmessage, message recived from client
	 */
	private void userRegister(UserMessage umsg){
		if(!umsg.stringPart(1).startsWith("****")){
			if(!database.isUsernameTaken(umsg.stringPart(1))) {
				umsg.getClient().sendMessage(new Message("REGISTER_RESPONSE:0", "USER", "-1").toString());
				database.addUser(umsg.stringPart(1), umsg.stringPart(2));
				umsg.getClient().setLoggedIn(umsg.stringPart(1));
			} else {
				umsg.getClient().sendMessage(new Message("REGISTER_RESPONSE:1", "USER", "-1").toString());
			}
		} else {
			umsg.getClient().sendMessage(new Message("REGISTER_RESPONSE:2", "USER", "-1").toString());
		}
	}
	
	/**
	 * @return a vector with all connected clients
	 */
	public ArrayList<ServerClient> getClients() {
		return clients;
	}
	
	/**
	 * Returns a client based on the clients username
	 * @param username the username we are looking for
	 * @return the client with the username
	 */
	private ServerClient getClient(String username) {
		for (ServerClient serverClient : clients) {
			if (serverClient.getUsername().equals(username))
				return serverClient;
		}
		return null;
	}

}
