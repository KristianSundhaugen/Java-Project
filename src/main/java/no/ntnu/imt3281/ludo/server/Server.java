package no.ntnu.imt3281.ludo.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

/**
 * Class for holding the server, letting clients send messages, and pass messages to the clients
 * @author Lasse Sviland
 *
 */
public class Server {
	private Vector<ServerClient> clients  = new Vector<>();
	private Vector<Game> games = new Vector<>();
    private ServerMessageReader reader;
	private boolean stop = false;
    private static Server server;
    private Database database;
    private boolean isLoggedIn;
    
    /**
     * Main function to start the program
     * @param args
     */
	public static void main(String[] args) {
        try {
        	System.out.println("Server Starting");
			server = new Server();
		} catch (IOException e) {System.out.println(e);}
        Runtime.getRuntime().addShutdownHook(new Thread() {            
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
     * Constructor for the server, Creating a socket and accepting new clients
     * @throws IOException
     */
    public Server() throws IOException {
    	this.reader = new ServerMessageReader(this);
        new Thread(reader).start();

    	@SuppressWarnings("resource")
		ServerSocket listener = new ServerSocket(9090);
        while(!stop) {
        	Socket socket = listener.accept();
        	clients.add(new ServerClient(socket));
        }
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
		if (msg.isGame() && msg.getGameMessage().isNewGameRequest())
			joinNewRandomGame(msg.getGameMessage());
		else if (msg.isGame() && msg.getGameMessage().isNewGameRequest())
			joinNewRandomGame(msg.getGameMessage());
		else if (msg.isGame() && msg.getGameMessage().isPlayerListRequest())
			sendPlayerList(msg.getGameMessage());
		else if (msg.isChat() && msg.getChatMessage().isListRequest())
			sendChatList(msg.getChatMessage());
		else if (msg.isChat() && msg.getChatMessage().isNewChatJoin())
			joinNewChat(msg.getChatMessage());
		else if (msg.isChat() && msg.getChatMessage().isNewChat())
			createNewChat(msg.getChatMessage());
		else if(msg.isUser() && msg.getUserMessage().isLoginRequest())
			userLogin(msg.getUserMessage());
		else if(msg.isRegister() && msg.getUserMessage().isRegisterRequest())
			userRegister(msg.getUserMessage());
		else 
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
		String msg = "";
		for (Game game : games) {
			if (game.isOpen() || game.isChat())
				msg += ":" + game.getId() + "-" + game.getChatterNumber() + "-" + game.getName();
		}
		cmsg.getClient().sendMessage(new Message("LIST_ROOMS_RESPONSE" + msg, "CHAT", "-1").toString());	
	}
	
	/**
	 * @param cmsg sending list over chats the user can join
	 */
	private void sendPlayerList(GameMessage cmsg) {
		String msg = "";
		for (ServerClient client : clients)
			if (!cmsg.getClient().equals(client))
				msg += ":" + client.getUsername();
		cmsg.getClient().sendMessage(new Message("PLAYER_LIST_RESPONSE" + msg, "GAME", "-1").toString());	
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
	 * Username and password is checked in the database
	 * If they are found and are correct the user will be logged in
	 * If not the user will get a notification
	 * @param lmessage, message recived from client
	 */
	private void userLogin(UserMessage lmessage){
		UserMessage um = new UserMessage(lmessage);
		if(database.checkLogin(um.stringPart(1), um.stringPart(2))){
			lmessage.getClient().sendMessage(new Message("LOGGIN_RESPONSE:1", "USER", "-1").toString());
		} else {
			lmessage.getClient().sendMessage(new Message("LOGGIN_RESPONSE:0", "USER", "-1").toString());
		}
	}
	
	/**
	 * Check if the username already exist in the database
	 * If not the user is registered and can log in
	 * If not the user will get a notification
	 * @param rmessage, message recived from client
	 */
	private void userRegister(UserMessage rmessage){
		UserMessage um = new UserMessage(rmessage);
		if(!um.stringPart(1).startsWith("****") || !um.stringPart(1).contains(":")){
			if(!database.checkUsername(um.stringPart(1))){
				rmessage.getClient().sendMessage(new Message("REGISTER_RESPONSE:1", "USER", "-1").toString());
			} else {
				rmessage.getClient().sendMessage(new Message("REGISTER_RESPONSE:0", "USER", "-1").toString());
			}
		} else {
			//say that the username contains invalid letters
			//rmessage.getClient().sendMessage(new Message("REGISTER_RESPONSE:0", "USER", "-1").toString());
		}
	}
	
	/**
	 * @return a vector with all connected clients
	 */
	public Vector<ServerClient> getClients() {
		return clients;
	}

}
