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
    
    /**
     * Main function to start the program
     * @param args
     */
	public static void main(String[] args) {
        try {
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
			joinNewGame(msg.getGameMessage());
		else if (msg.isChat() && msg.getChatMessage().isListRequest())
			sendChatList(msg.getChatMessage());
		else if (msg.isChat() && msg.getChatMessage().isNewChat())
			joinNewChat(msg.getChatMessage());
		else if(msg.isLogin() && msg.getLoginMessage().isLoginRequest())
			userLogin(msg.getLoginMessage());
		else
			for (Game game : games)
				if (game.getId().equals(msg.getGameMessage().getId()))
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
	 * @param cmsg sending list over chats the user can join
	 */
	private void sendChatList(ChatMessage cmsg) {
		String msg = "";
		for (Game game : games) {
			if (game.isOpen())
				msg += ":" + game.getId() + "-" + game.getChatterNumber();
		}
		cmsg.getClient().sendMessage(new Message("LIST_ROOMS_RESPONSE" + msg, "CHAT", "-1").toString());	
	}

	/**
	 * Adding player to a new game, creating a new one if there is none to join
	 * @param gmsg the game message received from the client
	 */
	private void joinNewGame(GameMessage gmsg) {
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
	 * Player gets logged in
	 * @param lmessage, message recived from client
	 */
	private void userLogin(UserMessage lmessage){
		
	}
	
	/**
	 * @return a vector with all connected clients
	 */
	public Vector<ServerClient> getClients() {
		return clients;
	}

}
